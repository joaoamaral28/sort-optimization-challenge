package SortAlgorithms.QuickSort;

import DataModel.MyData;

import java.util.concurrent.*;

/**
 * Performs the quick sort sorting algorithm in parallel by extending the
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
public class MyDataQuickSortParallelFork extends RecursiveAction {

    private static final int totalThreads = Runtime.getRuntime().availableProcessors();

    private final int minPartitionSize;

    private MyData[] data;
    private int lowerBound;
    private int upperBound;

    public MyDataQuickSortParallelFork(MyData[] data, int lowerBound, int upperBound){
        this.data = data;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.minPartitionSize = data.length/totalThreads;
    }

    @Override
    protected void compute() {
        quickSortParallel(this.data, this.lowerBound, this.upperBound);
    }

    static int partition(MyData[] data, int low, int high) {
        int pivot = data[high].getKey();
        int i = (low-1); // index of smaller element
        for (int j=low; j<high; j++) {
            // if current element is smaller than the pivot
            if (data[j].getKey() < pivot) {
                i++;

                // swap data[i] and data[j]
                MyData temp = data[i];
                data[i] = data[j];
                data[j] = temp;
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        MyData temp = data[i+1];
        data[i+1] = data[high];
        data[high] = temp;

        return i+1;
    }

    private static void quickSortLinear(MyData[] data, int lowerBound, int upperBound) {
        if (lowerBound < upperBound){
            int partitionIdx = partition(data, lowerBound, upperBound);
            quickSortLinear(data, lowerBound, partitionIdx-1);
            quickSortLinear(data, partitionIdx+1, upperBound);
        }
    }

    private void quickSortParallel(MyData[] data, int lowerBound, int upperBound){

        if(lowerBound < upperBound){

            int partitionIdx = partition(data, lowerBound, upperBound);

            if(data.length > minPartitionSize ) {

                invokeAll(new MyDataQuickSortParallelFork(data, lowerBound, partitionIdx - 1),
                        new MyDataQuickSortParallelFork(data, partitionIdx + 1, upperBound));

            }else{
                quickSortLinear(data, lowerBound, partitionIdx - 1);
                quickSortLinear(data, partitionIdx + 1, upperBound);
            }
        }
    }


}
