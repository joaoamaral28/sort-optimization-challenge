package SortAlgorithms;

import Model.MyData;

public class MyDataMergeSort {

    public static void mergeSort(MyData[] a, int n){
        if (n < 2) {
            return;
        }

        int mid = n / 2;

        MyData[] l = new MyData[mid];
        MyData[] r = new MyData[n - mid];

        for (int i = 0; i < mid; i++) {
            l[i] = a[i];
        }
        for (int i = mid; i < n; i++) {
            r[i - mid] = a[i];
        }
        mergeSort(l, mid);
        mergeSort(r, n - mid);

        merge(a, l, r, mid, n - mid);
    }

    private static void merge(MyData[] a, MyData[] l, MyData[] r, int left, int right) {

        int i = 0, j = 0, k = 0;

        while (i < left && j < right) {
            if (l[i].getKey() <= r[j].getKey()) {
                a[k++] = l[i++];
            }
            else {
                a[k++] = r[j++];
            }
        }
        while (i < left) {
            a[k++] = l[i++];
        }
        while (j < right) {
            a[k++] = r[j++];
        }
    }

}
