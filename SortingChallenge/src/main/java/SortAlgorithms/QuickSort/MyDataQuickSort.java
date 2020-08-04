package SortAlgorithms.QuickSort;

import DataModel.MyData;

/**
 * Sorts the input data by selecting an element as a pivot, followed by
 * its partition around the such pivot. The elements smaller than the pivot
 * are then placed on its left in the array and the elements greater on its
 * right. These steps are then applied for each resulting sub-array until
 * a single sorted array is obtained.
 */
public class MyDataQuickSort {

    // pick the last element of data[] as the pivot, puts it in its correct
    // position in the array and sorts by placing all the smaller elements on
    // its left side and all its greater elements on its right side.
    private static int partition(MyData[] data, int lowerBound, int upperBound) {
        int pivot = data[upperBound].getKey(); // pivot chosen as the array last element
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

        // swap arr[i+1] and arr[upperBound] (or pivot)
        MyData temp = data[i+1];
        data[i+1] = data[upperBound];
        data[upperBound] = temp;

        return i+1;
    }

    public static void quickSort(MyData data[], int lowerBound, int upperBound) {

        if (lowerBound < upperBound) {

            // partition current array and obtain the resulting index
            int partitionIdx = partition(data, lowerBound, upperBound);

            // Recursively sort elements before partition and after partition
            quickSort(data, lowerBound, partitionIdx-1);
            quickSort(data, partitionIdx+1, upperBound);
        }
    }


}
