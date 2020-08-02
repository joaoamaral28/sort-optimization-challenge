package SortAlgorithms;

import Model.MyData;

import java.util.concurrent.atomic.AtomicInteger;

public class MyDataQuickSortParallel extends Thread{

    private MyData[] data;
    private int lowerBound;
    private int upperBound;

    private static final int totalThreads = Runtime.getRuntime().availableProcessors();
    private static AtomicInteger runningThreads = new AtomicInteger(0);

    public MyDataQuickSortParallel(MyData[] data, int lowerBound, int upperBound){
        this.data = data;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public void run(){
        quickSortParallel(this.data, this.lowerBound, this.upperBound);
    }

    public static void quickSortLinear(MyData data[], int lowerBound, int upperBound) {
        if (lowerBound < upperBound){
            int partitionIdx = partition(data, lowerBound, upperBound);
            quickSortLinear(data, lowerBound, partitionIdx-1);
            quickSortLinear(data, partitionIdx+1, upperBound);
        }
    }

    static int partition(MyData data[], int lowerBound, int upperBound) {
        int pivot = data[upperBound].getKey();
        int i = (lowerBound-1); // index of smaller element
        for (int j=lowerBound; j<upperBound; j++)
        {
            // If current element is smaller than the pivot
            if (data[j].getKey() < pivot)
            {
                i++;

                // swap data[i] and data[j]
                MyData temp = data[i];
                data[i] = data[j];
                data[j] = temp;
            }
        }

        // swap arr[i+1] and arr[upperBound] (or pivot)
        MyData temp = data[i+1];
        data[i+1] = data[upperBound];
        data[upperBound] = temp;

        return i+1;
    }

    public static void quickSortParallel(MyData[] data, int lowerBound, int upperBound){

        if(lowerBound < upperBound){

            int partitionIdx = partition(data, lowerBound, upperBound);

            // if there are cores available we multi-thread the execution
            // handle lowerBound partition
            MyDataQuickSortParallel quickSortParallelLow = null;
            MyDataQuickSortParallel quickSortParallelHigh = null;
            if(runningThreads.get() < totalThreads){
                quickSortParallelLow = new MyDataQuickSortParallel(data, lowerBound, partitionIdx - 1);
                runningThreads.incrementAndGet();
                quickSortParallelLow.start();
            }else{
                quickSortLinear(data, lowerBound, partitionIdx - 1);
            }

            // handle upperBound partition
            if(runningThreads.get() < totalThreads){
                quickSortParallelHigh = new MyDataQuickSortParallel(data, partitionIdx + 1, upperBound);
                runningThreads.incrementAndGet();
                quickSortParallelHigh.start();
            }else{
                // if no cores are available we use the old recursive implementation
                quickSortLinear(data, partitionIdx+1, upperBound);
            }

            try {
                if (quickSortParallelHigh != null) {
                    quickSortParallelHigh.join();
                    runningThreads.decrementAndGet();

                }
                if (quickSortParallelLow != null) {
                    quickSortParallelLow.join();
                    runningThreads.decrementAndGet();

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}
