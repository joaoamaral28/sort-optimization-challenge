
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SampleDatasetCreation {

    public static void main(String[] args){

        final int num_rows = 1_000_000;
        final int max_pool = 100_000_000;

        Map<Integer, Integer> usedKeys = new HashMap<>();

        Random rand = new Random();

        File file = new File("src/main/resources/my_random_data.csv");

        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int key = 0;
        String referenceValue = "";

        FileWriter fileWriter;
        try {

            fileWriter = new FileWriter(file);

            fileWriter.write("Key,Reference Value\n");

            for (int i = 0; i < num_rows; i++) {

                key = rand.nextInt(max_pool + 1);

                while(usedKeys.containsKey(key)){
                    key = rand.nextInt(max_pool + 1);
                }
                usedKeys.put(key, i);
                fileWriter.write(key + "," + i + "\n");

            }

            fileWriter.close();

        }catch (IOException e){
            System.out.println("An error occurred while building file");
            e.printStackTrace();
        }

        //Arrays.stream(new int[num_rows]).forEach(System.out::println);

    }

}
