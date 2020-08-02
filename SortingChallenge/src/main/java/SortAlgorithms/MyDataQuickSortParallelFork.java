package SortAlgorithms;

import Model.MyData;

import java.util.concurrent.*;

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
        for (int j=low; j<high; j++)
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

        // swap arr[i+1] and arr[high] (or pivot)
        MyData temp = data[i+1];
        data[i+1] = data[high];
        data[high] = temp;

        return i+1;
    }

    private static void quickSortLinear(MyData[] data, int low, int high) {
        if (low < high){
            int partitionIdx = partition(data, low, high);
            quickSortLinear(data, low, partitionIdx-1);
            quickSortLinear(data, partitionIdx+1, high);
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
