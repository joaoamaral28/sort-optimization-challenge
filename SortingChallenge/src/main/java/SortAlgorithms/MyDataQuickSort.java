package SortAlgorithms;

import Model.MyData;

public class MyDataQuickSort {

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

    /* The main function that implements QuickSort()
    arr[] --> Array to be sorted,
    low  --> Starting index,
    high  --> Ending index */
    public static void quickSort(MyData data[], int low, int high) {
        if (low < high) {
            /* pi is partitioning index, arr[pi] is
              now at right place */
            int partitionIdx = partition(data, low, high);

            // Recursively sort elements before
            // partition and after partition
            quickSort(data, low, partitionIdx-1);
            quickSort(data, partitionIdx+1, high);
        }
    }


}
