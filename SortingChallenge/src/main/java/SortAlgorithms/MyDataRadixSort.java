package SortAlgorithms;

import Model.MyData;

import java.util.Arrays;

public class MyDataRadixSort {

    // A utility function to get maximum value in data[]
    static int getMax(MyData[] data, int n) {
        int mx = data[0].getKey();
        for (int i = 1; i < n; i++)
            if (data[i].getKey() > mx)
                mx = data[i].getKey();
        return mx;
    }

    // A function to do counting sort of data[] according to
    // the digit represented by exp.
    static void countingSort(MyData[] data, int n, int exp) {
        MyData output[] = new MyData[n]; // output array
        int i;
        int count[] = new int[10];
        Arrays.fill(count,0);

        // Store count of occurrences in count[]
        for (i = 0; i < n; i++)
            count[ (data[i].getKey()/exp)%10 ]++;

        // Change count[i] so that count[i] now contains
        // actual position of this digit in output[]
        for (i = 1; i < 10; i++)
            count[i] += count[i - 1];

        // Build the output array
        for (i = n - 1; i >= 0; i--)
        {
            output[count[ (data[i].getKey()/exp)%10 ] - 1] = data[i];
            count[ (data[i].getKey()/exp)%10 ]--;
        }

        // Copy the output array to data[], so that data[] now
        // contains sorted numbers according to current digit
        for (i = 0; i < n; i++)
            data[i] = output[i];
    }

    // The main function to that sorts array[] of size n using
    // Radix Sort
    public static void radixSort(MyData[] data, int n) {
        // Find the maximum number to know number of digits
        int m = getMax(data, n);

        // Do counting sort for every digit. Note that instead
        // of passing digit number, exp is passed. exp is 10^i
        // where i is current digit number
        for (int exp = 1; m/exp > 0; exp *= 10)
            countingSort(data, n, exp);
    }

}
