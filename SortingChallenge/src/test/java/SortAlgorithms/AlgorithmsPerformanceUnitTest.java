package SortAlgorithms;

import DataModel.MyData;

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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@UnitTests
public class AlgorithmsPerformanceUnitTest {

    private static MyData[] originalUnsortedData;
    private static MyData[] originalSortedData;
    private MyData[] testData;

    @BeforeAll
    public static void load(){

        List<MyData> data = new ArrayList<>();

        String csvFile = "src/main/resources/my_random_data.csv";
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
    public void testCollectionArraySort(){
        Instant start = Instant.now();
        Arrays.sort(testData);
        Instant end = Instant.now();
        System.out.println("Arrays.sort duration: " + Duration.between(start, end));
        assertArrayEquals(originalSortedData, testData);
    }

    @Test
    public void testQuickSortTime(){
        Instant start = Instant.now();
        MyDataQuickSort.quickSort(testData, 0, testData.length - 1);
        Instant end = Instant.now();
        System.out.println("Quick sort duration: " + Duration.between(start, end));

        assertArrayEquals(originalSortedData, testData);
    }

    @Test
    public void testQuickSortParallel(){
        Instant start = Instant.now();

        MyDataQuickSortParallel parallel = new MyDataQuickSortParallel(testData, 0, testData.length - 1);
        parallel.start();
        try {
            parallel.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Instant end = Instant.now();

        System.out.println("Quick sort parallel duration: " + Duration.between(start, end));

        assertArrayEquals(originalSortedData, testData);
    }

    @Test
    public void testQuickSortParallelFork(){
        Instant start = Instant.now();

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        MyDataQuickSortParallelFork quicksort = new MyDataQuickSortParallelFork(testData, 0, testData.length - 1);
        forkJoinPool.invoke(quicksort);
        forkJoinPool.shutdown();

        Instant end = Instant.now();

        System.out.println("Quick sort parallel fork duration: " + Duration.between(start, end));

        assertArrayEquals(originalSortedData, testData);
    }

    @Test
    public void testQuickSortHybrid(){
        Instant start = Instant.now();

        MyDataQuickSortHybrid.quickSort(testData, 0, testData.length - 1);

        Instant end = Instant.now();

        System.out.println("Quick hybrid sort duration: " + Duration.between(start, end));

        assertArrayEquals(originalSortedData, testData);
    }

    @Test
    public void testQuickSortHybridParallel(){

        Instant start = Instant.now();

        MyDataQuickSortHybridParallel parallel = new MyDataQuickSortHybridParallel(testData, 0, testData.length - 1);
        parallel.start();
        try {
            parallel.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Instant end = Instant.now();

        System.out.println("Quick sort hybrid parallel duration: " + Duration.between(start, end));

        assertArrayEquals(originalSortedData, testData);
    }

    @Test
    public void testQuickSortHybridParallelFork(){

        Instant start = Instant.now();

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        MyDataQuickSortHybridParallelFork mergeSortParallel = new MyDataQuickSortHybridParallelFork(testData, 0, testData.length - 1);
        forkJoinPool.invoke(mergeSortParallel);
        forkJoinPool.shutdown();

        Instant end = Instant.now();

        System.out.println("Quick sort hybrid parallel fork duration: " + Duration.between(start, end));

        assertArrayEquals(originalSortedData, testData);

    }

    @Test
    public void testMergeSort(){
        Instant start = Instant.now();
        MyDataMergeSort.mergeSort(testData, testData.length);
        Instant end = Instant.now();

        System.out.println("Merge sort duration: " + Duration.between(start, end));

        assertArrayEquals(originalSortedData, testData);
    }

    @Test
    public void testMergeSortParallel(){
        Instant start = Instant.now();
        MyDataMergeSortParallel mergeSortParallel = new MyDataMergeSortParallel(testData, testData.length);
        mergeSortParallel.start();

        try {
            mergeSortParallel.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Instant end = Instant.now();

        System.out.println("Merge sort parallel duration: " + Duration.between(start, end));

        assertArrayEquals(originalSortedData, testData);
    }

    @Test
    public void testMergeSortParallelFork(){
        Instant start = Instant.now();

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        MyDataMergeSortParallelFork mergeSortParallel = new MyDataMergeSortParallelFork(testData, 0, testData.length - 1);
        forkJoinPool.invoke(mergeSortParallel);
        forkJoinPool.shutdown();

        Instant end = Instant.now();

        System.out.println("Merge sort parallel fork duration: " + Duration.between(start, end));

        assertArrayEquals(originalSortedData, testData);

    }

    @Test
    public void testRadixSort(){
        Instant start = Instant.now();
        MyDataRadixSort.radixSort(testData, testData.length);
        Instant end = Instant.now();
        System.out.println("Radix sort duration: " + Duration.between(start, end));
        assertArrayEquals(originalSortedData, testData);
    }

}
