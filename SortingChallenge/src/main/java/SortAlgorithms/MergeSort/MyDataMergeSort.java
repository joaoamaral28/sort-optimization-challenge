package SortAlgorithms.MergeSort;

import DataModel.MyData;

/**
*   Sorts the input data by subsequently dividing it into two halves,
 *   sorting it and merging it into sorted sub arrays that in the
 *   end will form a final sorted array.
 * */
public class MyDataMergeSort {

    public static void mergeSort(MyData[] data, int upperBound){
        if (upperBound < 2) {
            return;
        }

        // middle point for data[] division
        int middleBound = upperBound / 2;

        MyData[] leftData = new MyData[middleBound];
        MyData[] rightData = new MyData[upperBound - middleBound];

        for (int i = 0; i < middleBound; i++) {
            leftData[i] = data[i];
        }
        for (int i = middleBound; i < upperBound; i++) {
            rightData[i - middleBound] = data[i];
        }

        // recursive calls to split left and right arrays
        mergeSort(leftData, middleBound);
        mergeSort(rightData, upperBound - middleBound);

        merge(data, leftData, rightData, middleBound, upperBound - middleBound);
    }

    // merges leftData and rightData sorted sub-arrays into data array
    private static void merge(MyData[] data, MyData[] leftData, MyData[] rightData, int lowerBound, int upperBound) {

        int i = 0, j = 0, k = 0;

        while (i < lowerBound && j < upperBound) {
            if (leftData[i].getKey() <= rightData[j].getKey()) {
                data[k++] = leftData[i++];
            }
            else {
                data[k++] = rightData[j++];
            }
        }
        while (i < lowerBound) {
            data[k++] = leftData[i++];
        }
        while (j < upperBound) {
            data[k++] = rightData[j++];
        }
    }

}
