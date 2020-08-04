package SortAlgorithms.RadixSort;

import DataModel.MyData;

import java.util.Arrays;

/**
 * Sorts the integer data based on the position of their digits rather
 * than comparing their value. The digits are sorted from the
 * Least Significant Digit (LSD) to the Most Significant Digit (MSD).
 * Uses counting sort for stable sorting
 */
public class MyDataRadixSort {

    // find the max value in data[]
    static int getMax(MyData[] data, int n) {
        int mx = data[0].getKey();
        for (int i = 1; i < n; i++)
            if (data[i].getKey() > mx)
                mx = data[i].getKey();
        return mx;
    }

    // Sort data[] using counting sort according to the digit exp
    static void countingSort(MyData[] data, int n, int exp) {
        MyData[] output = new MyData[n]; // output array
        int i;
        int[] count = new int[10];
        Arrays.fill(count,0);

        // calculate and store occurrences in count[]
        for (i = 0; i < n; i++) {
            count[(data[i].getKey() / exp) % 10]++;
        }
        // Change count[i] so that count[i] now contains
        // actual position of this digit in output[]
        for (i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        // Build the output array
        for (i = n - 1; i >= 0; i--) {
            output[count[ (data[i].getKey()/exp)%10 ] - 1] = data[i];
            count[ (data[i].getKey()/exp)%10 ]--;
        }

        // update the array with the sorted numbers according to current digit
        for (i = 0; i < n; i++)
            data[i] = output[i];
    }

    public static void radixSort(MyData[] data, int n) {
        // get the maximum number to know the number of digits
        int m = getMax(data, n);

        // apply counting sort for every digit
        // the digit exponent (10^i) is used
        for (int exp = 1; m/exp > 0; exp *= 10)
            countingSort(data, n, exp);
    }

}
