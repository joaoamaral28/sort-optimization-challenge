package SortAlgorithms;

import SortAlgorithms.MergeSort.MyDataMergeSort;
import SortAlgorithms.MergeSort.MyDataMergeSortParallel;
import SortAlgorithms.MergeSort.MyDataMergeSortParallelFork;
import SortAlgorithms.QuickSort.MyDataQuickSort;
import SortAlgorithms.QuickSort.MyDataQuickSortParallel;
import SortAlgorithms.QuickSort.MyDataQuickSortParallelFork;
import SortAlgorithms.QuickSortHybrid.MyDataQuickSortHybrid;
import SortAlgorithms.QuickSortHybrid.MyDataQuickSortHybridParallel;
import SortAlgorithms.QuickSortHybrid.MyDataQuickSortHybridParallelFork;
import SortAlgorithms.RadixSort.MyDataRadixSort;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import DataModel.MyData;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@BenchmarkTests
public class AlgorithmsPerformanceBenchmarkTest {

    private static final int NUM_RUNS = 1000;

    private static MyData[] originalUnsortedData;
    private static MyData[] originalSortedData;
    private MyData[] testData;

    @BeforeAll
    public static void load(){

        List<MyData> data = new ArrayList<>();

        //String csvFile = "src/main/resources/my_random_data.csv";
        String csvFile = "src/main/resources/my_random_data_sequential.csv";

        String line = "";
        String splitCondition = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String header = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] saleData = line.split(splitCondition);
                data.add(new MyData(Integer.parseInt(saleData[0]), Integer.parseInt(saleData[1])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        originalUnsortedData = data.toArray(new MyData[data.size()]);

        originalSortedData = new MyData[originalUnsortedData.length];
        System.arraycopy(originalUnsortedData, 0, originalSortedData, 0, originalUnsortedData.length);
        Arrays.sort(originalSortedData);

    }

    @BeforeEach()
    public void setup(){
        //System.out.println("Cleaning up");
        testData = new MyData[originalUnsortedData.length];
        System.arraycopy(originalUnsortedData, 0, testData, 0, originalUnsortedData.length);
    }

    @Test
    public void benchmarkCollectionArraySort(){

        System.out.println("Benchmarking Collection Sort...");

        long avgTime;
        long totalTime = 0;

        for(int i=0;i<NUM_RUNS;i++){
            long start = System.currentTimeMillis();
            Arrays.sort(testData);
            long end = System.currentTimeMillis();
            totalTime += (end-start);
            assertArrayEquals(originalSortedData, testData);
            setup();
        }

        avgTime = totalTime / NUM_RUNS;

        System.out.println("Collection sort avg. time: " + avgTime + " ms");

    }

    @Test
    public void benchmarkQuickSortTime(){

        System.out.println("Benchmarking quick sort...");

        long avgTime;
        long totalTime = 0;

        for(int i=0;i<NUM_RUNS;i++){
            long start = System.currentTimeMillis();
            MyDataQuickSort.quickSort(testData, 0, testData.length - 1);
            long end = System.currentTimeMillis();
            totalTime += (end-start);
            assertArrayEquals(originalSortedData, testData);
            setup();
        }

        avgTime = totalTime / NUM_RUNS;

        System.out.println("Quick sort avg. time: " + avgTime + " ms");

    }

    @Test
    public void benchmarkQuickSortParallel(){

        System.out.println("Benchmarking quick sort parallel...");

        long avgTime;
        long totalTime = 0;

        for(int i=0;i<NUM_RUNS;i++){
            long start = System.currentTimeMillis();
            MyDataQuickSortParallel parallel = new MyDataQuickSortParallel(testData, 0, testData.length - 1);
            parallel.start();
            try {
                parallel.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            totalTime += (end-start);
            assertArrayEquals(originalSortedData, testData);
            setup();
        }

        avgTime = totalTime / NUM_RUNS;

        System.out.println("Quick sort parallel avg. time: " + avgTime + " ms");

    }

    @Test
    public void benchmarkQuickSortParallelFork(){

        System.out.println("Benchmarking quick sort parallel (ForkJoinPool)...");

        long avgTime;
        long totalTime = 0;

        for(int i=0;i<NUM_RUNS;i++){
            long start = System.currentTimeMillis();
            ForkJoinPool forkJoinPool = new ForkJoinPool();
            MyDataQuickSortParallelFork quicksort = new MyDataQuickSortParallelFork(testData, 0, testData.length - 1);
            forkJoinPool.invoke(quicksort);
            forkJoinPool.shutdown();
            long end = System.currentTimeMillis();
            totalTime += (end-start);
            assertArrayEquals(originalSortedData, testData);
            setup();
        }

        avgTime = totalTime / NUM_RUNS;

        System.out.println("Quick sort parallel (ForkJoinPool) avg. time: " + avgTime + " ms");

    }

    @Test
    public void benchmarkQuickSortHybrid(){

        System.out.println("Benchmarking quick sort hybrid (insertion sort)...");

        long avgTime;
        long totalTime = 0;

        for(int i=0;i<NUM_RUNS;i++){
            long start = System.currentTimeMillis();
            MyDataQuickSortHybrid.quickSort(testData, 0, testData.length - 1);
            long end = System.currentTimeMillis();
            totalTime += (end-start);
            assertArrayEquals(originalSortedData, testData);
            setup();
        }

        avgTime = totalTime / NUM_RUNS;

        System.out.println("Quick sort hybrid (insertion sort) avg. time: " + avgTime + " ms");

    }

    @Test
    public void benchmarkQuickSortHybridParallel(){

        System.out.println("Benchmarking quick sort hybrid parallel...");

        long avgTime;
        long totalTime = 0;

        for(int i=0;i<NUM_RUNS;i++){
            long start = System.currentTimeMillis();
            MyDataQuickSortHybridParallel parallel = new MyDataQuickSortHybridParallel(testData, 0, testData.length - 1);
            parallel.start();
            try {
                parallel.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            totalTime += (end-start);
            assertArrayEquals(originalSortedData, testData);
            setup();
        }

        avgTime = totalTime / NUM_RUNS;

        System.out.println("Quick sort hybrid parallel avg. time: " + avgTime + " ms");

    }

    @Test
    public void benchmarkQuickSortHybridParallelFork(){

        System.out.println("Benchmarking quick sort hybrid parallel (ForkJoinPool)...");

        long avgTime;
        long totalTime = 0;

        for(int i=0;i<NUM_RUNS;i++){
            long start = System.currentTimeMillis();
            ForkJoinPool forkJoinPool = new ForkJoinPool();
            MyDataQuickSortHybridParallelFork mergeSortParallel = new MyDataQuickSortHybridParallelFork(testData, 0, testData.length - 1);
            forkJoinPool.invoke(mergeSortParallel);
            forkJoinPool.shutdown();
            long end = System.currentTimeMillis();
            totalTime += (end-start);
            assertArrayEquals(originalSortedData, testData);
            setup();
        }

        avgTime = totalTime / NUM_RUNS;

        System.out.println("Quick sort hybrid parallel (ForkJoinPool) avg. time: " + avgTime + " ms");

    }

    @Test
    public void benchmarkMergeSort(){

        System.out.println("Benchmarking merge sort...");

        long avgTime;
        long totalTime = 0;

        for(int i=0;i<NUM_RUNS;i++){
            long start = System.currentTimeMillis();
            MyDataMergeSort.mergeSort(testData, testData.length);
            long end = System.currentTimeMillis();
            totalTime += (end-start);
            assertArrayEquals(originalSortedData, testData);
            setup();
        }

        avgTime = totalTime / NUM_RUNS;

        System.out.println("Merge sort avg. time: " + avgTime + " ms");

    }

    @Test
    public void benchmarkMergeSortParallel(){

        System.out.println("Benchmarking merge sort parallel...");

        long avgTime;
        long totalTime = 0;

        for(int i=0;i<NUM_RUNS;i++){
            long start = System.currentTimeMillis();
            MyDataMergeSortParallel mergeSortParallel = new MyDataMergeSortParallel(testData, testData.length);
            mergeSortParallel.start();

            try {
                mergeSortParallel.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            totalTime += (end-start);
            assertArrayEquals(originalSortedData, testData);
            setup();
        }

        avgTime = totalTime / NUM_RUNS;

        System.out.println("Merge sort parallel avg. time: " + avgTime + " ms");

    }

    @Test
    public void benchmarkMergeSortParallelFork() {
        System.out.println("Benchmarking merge sort parallel (ForkJoinPool)...");

        long avgTime;
        long totalTime = 0;

        for (int i = 0; i < NUM_RUNS; i++) {
            long start = System.currentTimeMillis();
            ForkJoinPool forkJoinPool = new ForkJoinPool();
            MyDataMergeSortParallelFork mergeSortParallel = new MyDataMergeSortParallelFork(testData, 0, testData.length - 1);
            forkJoinPool.invoke(mergeSortParallel);
            forkJoinPool.shutdown();
            long end = System.currentTimeMillis();
            totalTime += (end - start);
            assertArrayEquals(originalSortedData, testData);
            setup();
        }

        avgTime = totalTime / NUM_RUNS;

        System.out.println("Merge sort parallel (ForkJoinPool) avg. time: " + avgTime + " ms");
    }

    @Test
    public void benchmarkRadixSort(){

        System.out.println("Benchmarking radix sort...");

        long avgTime;
        long totalTime = 0;

        for(int i=0;i<NUM_RUNS;i++){
            long start = System.currentTimeMillis();
            MyDataRadixSort.radixSort(testData, testData.length);
            long end = System.currentTimeMillis();
            totalTime += (end-start);
            assertArrayEquals(originalSortedData, testData);
            setup();
        }

        avgTime = totalTime / NUM_RUNS;

        System.out.println("Radix sort avg. time: " + avgTime + " ms");

    }
}
