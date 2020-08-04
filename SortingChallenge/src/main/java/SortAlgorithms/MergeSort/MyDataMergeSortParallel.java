package SortAlgorithms.MergeSort;

import DataModel.MyData;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Performs the merge sort algorithm in parallel. Each call
 * the algorithm will check if the sub-array can be split and handled
 * in a separate thread in order to parallelize and speed-up the execution.
 * If, however, all the cpu cores are running a task, the algorithm will then
 * execute recursively without parallelization. Upon splitting the execution
 * the mother thread will then wait for its completion before proceeding
 */
public class MyDataMergeSortParallel extends Thread{

    private MyData[] data;
    private int upperBound;

    private static final int totalThreads = Runtime.getRuntime().availableProcessors();
    private static AtomicInteger runningThreads = new AtomicInteger(0);

    public MyDataMergeSortParallel(MyData[] data, int upperBound){
        this.data = data;
        this.upperBound = upperBound;
    }

    @Override
    public void run(){
        mergeSortParallel(this.data, this.upperBound);
    }

    public static void mergeSortParallel(MyData[] data, int upperBound){

        if (upperBound < 2) {
            return;
        }

        int middleBound = upperBound / 2;

        MyData[] leftData = new MyData[middleBound];
        MyData[] rightData = new MyData[upperBound - middleBound];

        for (int i = 0; i < middleBound; i++) {
            leftData[i] = data[i];
        }

        for (int i = middleBound; i < upperBound; i++) {
            rightData[i - middleBound] = data[i];
        }

        MyDataMergeSortParallel left = null;
        MyDataMergeSortParallel right = null;

        if(runningThreads.get() < totalThreads){
            left = new MyDataMergeSortParallel(leftData, middleBound);
            runningThreads.incrementAndGet();
            left.start();
        }else{
            mergeSortLinear(leftData,middleBound);
        }

        if(runningThreads.get() < totalThreads){
            right = new MyDataMergeSortParallel(rightData, upperBound - middleBound);
            runningThreads.incrementAndGet();
            right.start();
        }else{
            mergeSortLinear(rightData, upperBound - middleBound);
        }

        try {
            if (left != null) {
                left.join();
                runningThreads.decrementAndGet();
            }
            if (right != null) {
                right.join();
                runningThreads.decrementAndGet();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        merge(data, leftData, rightData, middleBound, upperBound - middleBound);

    }


    public static void mergeSortLinear(MyData[] data, int upperBound){
        if (upperBound < 2) {
            return;
        }

        int middleBound = upperBound / 2;

        MyData[] leftData = new MyData[middleBound];
        MyData[] rightData = new MyData[upperBound - middleBound];

        for (int i = 0; i < middleBound; i++) {
            leftData[i] = data[i];
        }
        for (int i = middleBound; i < upperBound; i++) {
            rightData[i - middleBound] = data[i];
        }
        mergeSortLinear(leftData, middleBound);
        mergeSortLinear(rightData, upperBound - middleBound);

        merge(data, leftData, rightData, middleBound, upperBound - middleBound);
    }

    private static void merge(MyData[] data, MyData[] leftData, MyData[] rightData, int lowerBound, int upperBound) {

        int i = 0, j = 0, k = 0;

        while (i < lowerBound && j < upperBound) {
            if (leftData[i].getKey() <= rightData[j].getKey()) {
                data[k++] = leftData[i++];
            }
            else {
                data[k++] = rightData[j++];
            }
        }
        while (i < lowerBound) {
            data[k++] = leftData[i++];
        }
        while (j < upperBound) {
            data[k++] = rightData[j++];
        }
    }



}
