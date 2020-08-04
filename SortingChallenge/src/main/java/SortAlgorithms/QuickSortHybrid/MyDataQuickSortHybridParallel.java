package SortAlgorithms.QuickSortHybrid;

import DataModel.MyData;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Performs the hybrid quick sort algorithm in parallel. Each call
 * the algorithm will check if the sub-array can be split and handled
 * in a separate thread in order to parallelize and speed-up the execution.
 * If, however, all the cpu cores are running a task, the algorithm will then
 * execute recursively without parallelization. Upon splitting the execution
 * the mother thread will then wait for its completion before proceeding.
 */
public class MyDataQuickSortHybridParallel extends Thread{

    private static final int totalThreads = Runtime.getRuntime().availableProcessors();
    private static AtomicInteger runningThreads = new AtomicInteger(0);

    // cutting threshold for performing insertion sort instead of quick sort
    private static final int INSERTION_THRESHOLD = 10;

    private MyData[] data;
    private int lowerBound;
    private int upperBound;

    public MyDataQuickSortHybridParallel(MyData[] data, int lowerBound, int upperBound){
        this.data = data;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public void run(){
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

    private void quickSort(MyData[] data, int lowerBound, int upperBound){
        if(lowerBound < upperBound){
            // check if sorting can be done directly with insertion sort
            if((upperBound - lowerBound) < INSERTION_THRESHOLD){
                insertionSort(data, lowerBound, upperBound);
            }else{
                int partitionIdx = partition(data, lowerBound, upperBound);

                // check if available cores
                MyDataQuickSortHybridParallel low = null;
                MyDataQuickSortHybridParallel high = null;
                if(runningThreads.get() < totalThreads){
                    low = new MyDataQuickSortHybridParallel(data, lowerBound, partitionIdx - 1);
                    runningThreads.incrementAndGet();
                    low.start();
                }else{
                    quickSort(data, lowerBound, partitionIdx - 1);
                }
                if(runningThreads.get() < totalThreads){
                    high = new MyDataQuickSortHybridParallel(data, partitionIdx+1, upperBound);
                    runningThreads.incrementAndGet();
                    high.start();
                }else{
                    quickSort(data, partitionIdx+1, upperBound);
                }

                try {
                    if (low != null) {
                        low.join();
                        runningThreads.decrementAndGet();
                    }
                    if (high != null) {
                        high.join();
                        runningThreads.decrementAndGet();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
