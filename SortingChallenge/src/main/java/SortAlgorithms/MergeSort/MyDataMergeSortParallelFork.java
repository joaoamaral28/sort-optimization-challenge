package SortAlgorithms.MergeSort;

import DataModel.MyData;

import java.util.concurrent.RecursiveAction;

/**
 * Performs the merge sort sorting algorithm in parallel by extending the
 * RecursiveAction abstract class. By executing the class inside a
 * ForkJoinPool, this framework will handle all the thread scheduling.
 * The major benefit of using ForkJoinPool, when compared to regular
 * thread launch is that it reduces the number of times the thread
 * will have to go looking for work. In a standard implementation
 * the free threads will try to "steal" work from the queues of
 * running threads (known as "Work Stealing"). With this approach,
 * priority is given to the biggest available of chunks first.
 * Src: https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ForkJoinPool.html
 */
public class MyDataMergeSortParallelFork extends RecursiveAction{

    // total amount of threads the algorithm can run (nº of CPU cores)
    private static final int totalThreads = Runtime.getRuntime().availableProcessors();

    private MyData[] data;
    private int lowerBound;
    private int upperBound;

    // threshold specifying the minimum size an array must have for its parallelization processing to be worth it
    private final int minPartitionSize;

    public MyDataMergeSortParallelFork(MyData[] data, int lowerBound, int upperBound){
        this.data = data;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.minPartitionSize = data.length/totalThreads;
    }

    @Override
    protected void compute() {
        mergeSort(this.data, this.lowerBound, this.upperBound);
        //mergeSortLinear(this.data,this.lowerBound, this.upperBound);
    }


    public void mergeSort(MyData[] data, int lowerBound, int upperBound){

        if(lowerBound < upperBound) {

            int size = upperBound - lowerBound;

            //  check if its worth paralleling the execution
            if (size < minPartitionSize) {
                mergeSortLinear(data, lowerBound, upperBound);
            } else {
                int middleBound = (upperBound + lowerBound) / 2;

                // parallelize execution by submitting two new tasks to the current thread pool
                invokeAll(new MyDataMergeSortParallelFork(data, lowerBound, middleBound),
                        new MyDataMergeSortParallelFork(data, middleBound+1, upperBound));
                merge(data, lowerBound, middleBound, upperBound);
            }
        }
    }

    private static void mergeSortLinear(MyData[] data, int lowerBound, int upperBound){
        if(lowerBound < upperBound) {
            int middleBound = (lowerBound + upperBound) / 2;
            mergeSortLinear(data, lowerBound, middleBound);
            mergeSortLinear(data, middleBound + 1, upperBound);
            merge(data, lowerBound, middleBound, upperBound);
        }
    }

    private static void merge(MyData[] data, int lowerBound, int middleBound, int upperBound){

        int leftSize = middleBound - lowerBound + 1;
        int rightSize = upperBound - middleBound;

        /* temporary array for split data */
        MyData[] leftData = new MyData[leftSize];
        MyData[] rightData = new MyData[rightSize];

       for(int i = 0; i<leftSize; i++){
            leftData[i] = data[lowerBound + i];
        }

        for(int i = 0; i<rightSize;i++){
            rightData[i] = data[middleBound + i + 1];
        }

        int i = 0, j = 0, k = lowerBound;

        while (i < leftSize && j < rightSize) {
            if (leftData[i].getKey() <= rightData[j].getKey()) {
                data[k++] = leftData[i++];
            }
            else {
                data[k++] = rightData[j++];
            }
        }
        while (i < leftSize) {
            data[k++] = leftData[i++];
        }
        while (j < rightSize) {
            data[k++] = rightData[j++];
        }
    }
}
