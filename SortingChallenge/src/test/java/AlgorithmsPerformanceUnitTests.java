import Model.MyData;
import SortAlgorithms.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class AlgorithmsPerformanceUnitTests {

    private static Model.MyData[] originalUnsortedData;
    private static Model.MyData[] originalSortedData;
    private Model.MyData[] testData;

    @BeforeAll
    static void load(){

        List<Model.MyData> data = new ArrayList<>();

        String csvFile = "src/main/resources/my_random_data.csv";
        String line = "";
        String splitCondition = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String header = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] saleData = line.split(splitCondition);
                data.add(new Model.MyData(Integer.parseInt(saleData[0]), Integer.parseInt(saleData[1])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        originalUnsortedData = data.toArray(new Model.MyData[data.size()]);

        originalSortedData = new MyData[originalUnsortedData.length];
        System.arraycopy(originalUnsortedData, 0, originalSortedData, 0, originalUnsortedData.length);
        Arrays.sort(originalSortedData);

    }

    @BeforeEach()
    void setup(){
        //System.out.println("Cleaning up");
        testData = new MyData[originalUnsortedData.length];
        System.arraycopy(originalUnsortedData, 0, testData, 0, originalUnsortedData.length);
    }

    @Test
    void testCollectionArraySort(){
        Instant start = Instant.now();
        Arrays.sort(testData);
        Instant end = Instant.now();
        System.out.print("Arrays.sort duration: " + Duration.between(start, end));
        assertArrayEquals(originalSortedData, testData);
    }

    @Test
    void testQuickSortTime(){
        Instant start = Instant.now();
        MyDataQuickSort.quickSort(testData, 0, testData.length - 1);
        Instant end = Instant.now();
        System.out.print("Quick sort duration: " + Duration.between(start, end));

        assertArrayEquals(originalSortedData, testData);
    }

    @Test
    void testQuickSortParallel(){
        Instant start = Instant.now();

        MyDataQuickSortParallel parallel = new MyDataQuickSortParallel(testData, 0, testData.length - 1);
        parallel.start();
        try {
            parallel.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Instant end = Instant.now();

        System.out.print("Quick sort parallel duration: " + Duration.between(start, end));

        assertArrayEquals(originalSortedData, testData);
    }

    @Test
    void testQuickSortParallelFork(){
        Instant start = Instant.now();

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        MyDataQuickSortParallelFork quicksort = new MyDataQuickSortParallelFork(testData, 0, testData.length - 1);
        forkJoinPool.invoke(quicksort);
        forkJoinPool.shutdown();

        Instant end = Instant.now();

        System.out.print("Quick sort parallel runnable duration: " + Duration.between(start, end));

        assertArrayEquals(originalSortedData, testData);
    }

    @Test
    void testQuickSortHybrid(){
        Instant start = Instant.now();

        MyDataQuickSortHybrid.quickSort(testData, 0, testData.length - 1);

        Instant end = Instant.now();

        System.out.print("Quick hybrid sort duration: " + Duration.between(start, end));

        assertArrayEquals(originalSortedData, testData);
    }

    @Test
    void testMergeSort(){
        Instant start = Instant.now();
        MyDataMergeSort.mergeSort(testData, testData.length);
        Instant end = Instant.now();

        System.out.print("Merge sort duration: " + Duration.between(start, end));

        assertArrayEquals(originalSortedData, testData);
    }

    @Test
    void testMergeSortParallel(){
        Instant start = Instant.now();
        MyDataMergeSortParallel mergeSortParallel = new MyDataMergeSortParallel(testData, testData.length);
        mergeSortParallel.start();

        try {
            mergeSortParallel.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Instant end = Instant.now();

        System.out.print("Merge sort parallel duration: " + Duration.between(start, end));

        assertArrayEquals(originalSortedData, testData);
    }

    @Test
    void testRadixSort(){
        Instant start = Instant.now();
        MyDataRadixSort.radixSort(testData, testData.length);
        Instant end = Instant.now();
        System.out.print("Radix sort duration: " + Duration.between(start, end));
        assertArrayEquals(originalSortedData, testData);
    }

}
