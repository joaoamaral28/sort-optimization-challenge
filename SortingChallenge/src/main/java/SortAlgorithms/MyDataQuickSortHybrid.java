package SortAlgorithms;

import Model.MyData;

public class MyDataQuickSortHybrid {

    // cutting threshold for performing insertion sort instead of quick sort
    private static final int INSERTION_THRESHOLD = 10;

    /* This function takes last element as pivot,
    places the pivot element at its correct
    position in sorted array, and places all
    smaller (smaller than pivot) to left of
    pivot and all greater elements to right
    of pivot */
    private static int partition(MyData[] data, int low, int high) {
        int pivot = data[high].getKey();
        int i = (low-1); // index of smaller element
        for (int j=low; j<high; j++) {
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
        data[i+1] = data[high];
        data[high] = temp;

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

    private static void selectionSort(MyData[] data) {
        int n = data.length;

        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n-1; i++) {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i+1; j < n; j++)
                if (data[j].getKey() < data[min_idx].getKey())
                    min_idx = j;

            // Swap the found minimum element with the first
            // element
            MyData temp = data[min_idx];
            data[min_idx] = data[i];
            data[i] = temp;
        }
    }

    public static void quickSort(MyData[] data, int lowerBound, int upperBound){

        while(lowerBound < upperBound){

            if((upperBound - lowerBound) < INSERTION_THRESHOLD){
                insertionSort(data, lowerBound, upperBound);
                //selectionSort(data);
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


                //quickSort(data, lowerBound, partitionIdx-1);
                //quickSort(data, partitionIdx+1, upperBound);
            }

        }
    }

}
