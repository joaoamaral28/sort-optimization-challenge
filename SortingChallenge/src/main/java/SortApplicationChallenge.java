
import SortAlgorithms.MyDataMergeSortParallel;
import SortAlgorithms.MyDataQuickSortParallel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SortApplicationChallenge {

    public static void main(String[] args) {

        String csvFile = "src/main/resources/my_random_data.csv";
        String line = "";
        String csvSplitBy = ",";

        List<Model.MyData> key_data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String header = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] saleData = line.split(csvSplitBy);
                key_data.add(new Model.MyData(Integer.parseInt(saleData[0]), Integer.parseInt(saleData[1])));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Model.MyData[] data = key_data.toArray(new Model.MyData[key_data.size()]);
        //Arrays.stream(data).forEach(System.out::println);

        Instant start = Instant.now();

        // Algorithm goes here
        MyDataQuickSortParallel parallel = new MyDataQuickSortParallel(data, 0, data.length - 1);
        parallel.start();
        try {
            parallel.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Instant end = Instant.now();

        //Arrays.stream(data).forEach(System.out::println);

        System.out.println("Sort duration:" + Duration.between(start, end));

        // Lazy validation
        if(data[data.length-1].getReferenceVal() == 410291){
            System.out.println("SUCCESS!");
        }else{
            System.out.println("FAILED!");
        }

    }
}
