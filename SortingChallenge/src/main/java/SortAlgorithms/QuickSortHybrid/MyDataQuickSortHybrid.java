package SortAlgorithms.QuickSortHybrid;

import DataModel.MyData;

/**
 * A combination of regular quick sort algorithm with insertion sort for achieving slightly better
 * overall performance. The way it operates is that, if the number of elements in the current array
 * being processed is below a threshold ( INSERTION_THRESHOLD ), the algorithm will switch to
 * non-recursive sorting, this way performing fewer swaps, comparisons than normally, while also
 * lowering the recursive quick sort depth.
 */
public class MyDataQuickSortHybrid {

    // cutting threshold for performing insertion sort instead of quick sort
    private static final int INSERTION_THRESHOLD = 10;

    private static int partition(MyData[] data, int lowerBound, int upperBound) {
        int pivot = data[upperBound].getKey();
        int i = (lowerBound-1); // index of smaller element
        for (int j=lowerBound; j<upperBound; j++) {
            // If current element is smaller than the pivot
            if (data[j].getKey() < pivot) {
                i++;

                // swap arr[i] and arr[j]
                MyData temp = data[i];
                data[i] = data[j];
                data[j] = temp;
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        MyData temp = data[i+1];
        data[i+1] = data[upperBound];
        data[upperBound] = temp;

        return i+1;
    }

    private static void insertionSort(MyData[] data, int lowerBound, int upperBound) {
        for (int i = lowerBound+1; i <= upperBound; ++i) {
            MyData current = data[i];
            int j = i-1;
            while (lowerBound <= j && current.getKey() < data[j].getKey()) {
                data[j+1] = data[j--];
            }
            data[j+1] = current;
        }
    }

    public static void quickSort(MyData[] data, int lowerBound, int upperBound){

        while(lowerBound < upperBound){

            if((upperBound - lowerBound) < INSERTION_THRESHOLD){
                insertionSort(data, lowerBound, upperBound);
                break;
            }else {
                int partitionIdx = partition(data, lowerBound, upperBound);

                if (partitionIdx - lowerBound < upperBound - partitionIdx) {
                    quickSort(data, lowerBound, partitionIdx - 1);
                    lowerBound = partitionIdx + 1;
                }else{
                    quickSort(data,partitionIdx + 1, upperBound);
                    upperBound = partitionIdx - 1;
                }

            }

        }
    }

}
