
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import DataModel.MyData;
import SortAlgorithms.QuickSortHybrid.MyDataQuickSortHybridParallelFork;

public class SortApplication {

    public static void main(String[] args) {

        String csvFile = "src/main/resources/my_random_data_sequential.csv";
        String line = "";
        String csvSplitBy = ",";

        List<MyData> key_data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String header = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] saleData = line.split(csvSplitBy);
                key_data.add(new MyData(Integer.parseInt(saleData[0]), Integer.parseInt(saleData[1])));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        MyData[] data = key_data.toArray(new MyData[key_data.size()]);
        //Arrays.stream(data).forEach(System.out::println);

        System.out.println("Sorting data using QuickSortHybridParallelFork...");

        Instant start = Instant.now();

        // Sort the parsed data using the optimal performant algorithm (hybrid quick sort using ForkJoinPool)
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        MyDataQuickSortHybridParallelFork mergeSortParallel = new MyDataQuickSortHybridParallelFork(data, 0, data.length - 1);
        forkJoinPool.invoke(mergeSortParallel);
        forkJoinPool.shutdown();

        Instant end = Instant.now();

        //Arrays.stream(data).forEach(System.out::println);

        System.out.println("Sorted " + data.length + " records in: " + Duration.between(start, end));

    }
}
