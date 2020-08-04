package SortAlgorithms.QuickSortHybrid;

import DataModel.MyData;

import java.util.concurrent.RecursiveAction;

/**
 * Performs the quick sort hybrid sorting algorithm in parallel by extending the
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
public class MyDataQuickSortHybridParallelFork extends RecursiveAction {

    // cutting threshold for performing insertion sort instead of quick sort
    private static final int INSERTION_THRESHOLD = 10;

    private static final int totalThreads = Runtime.getRuntime().availableProcessors();
    private final int minPartitionSize;

    private MyData[] data;
    private int lowerBound;
    private int upperBound;

    public MyDataQuickSortHybridParallelFork(MyData[] data, int lowerBound, int upperBound){
        this.data = data;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.minPartitionSize = data.length / totalThreads;
    }

    @Override
    protected void compute() {
        quickSort(this.data, this.lowerBound, this.upperBound);
    }

    private int partition(MyData[] data, int low, int high){
        int pivot = data[high].getKey();
        int i = (low-1);
        for (int j=low; j<high; j++) {

            if (data[j].getKey() < pivot) {
                i++;

                // swap arr[i] and arr[j]
                MyData temp = data[i];
                data[i] = data[j];
                data[j] = temp;
            }
        }

        MyData temp = data[i+1];
        data[i+1] = data[high];
        data[high] = temp;

        return i+1;
    }

    private void insertionSort(MyData[] data, int lowerBound, int upperBound){
        for (int i = lowerBound+1; i <= upperBound; ++i) {
            MyData current = data[i];
            int j = i-1;
            while (lowerBound <= j && current.getKey() < data[j].getKey()) {
                data[j+1] = data[j--];
            }
            data[j+1] = current;
        }
    }

    private void quickSortLinear(MyData[] data, int lowerBound, int upperBound) {
        if (lowerBound < upperBound){
            int partitionIdx = partition(data, lowerBound, upperBound);
            quickSortLinear(data, lowerBound, partitionIdx-1);
            quickSortLinear(data, partitionIdx+1, upperBound);
        }
    }

    private void quickSort(MyData[] data, int lowerBound, int upperBound){
        if(lowerBound < upperBound){
            // check if sorting can be done directly with insertion sort
            if((upperBound - lowerBound) < INSERTION_THRESHOLD){
                insertionSort(data, lowerBound, upperBound);
            }else{
                int partitionIdx = partition(data, lowerBound, upperBound);
                // check if its worth paralleling the execution
                if(data.length > minPartitionSize ) {
                    invokeAll(new MyDataQuickSortHybridParallelFork(data, lowerBound, partitionIdx - 1),
                            new MyDataQuickSortHybridParallelFork(data, partitionIdx + 1, upperBound));
                }else{
                    quickSortLinear(data, lowerBound, partitionIdx - 1);
                    quickSortLinear(data, partitionIdx + 1, upperBound);
                }
            }
        }
    }


}
