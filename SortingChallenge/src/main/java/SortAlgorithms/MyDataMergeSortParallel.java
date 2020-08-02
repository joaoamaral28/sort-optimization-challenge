package SortAlgorithms;

import Model.MyData;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class MyDataMergeSortParallel extends Thread{

    private MyData[] data;
    private int n;

    private static final int totalThreads = Runtime.getRuntime().availableProcessors();
    private static AtomicInteger runningThreads = new AtomicInteger(0);

    public MyDataMergeSortParallel(MyData[] data, int n){
        this.data = data;
        this.n = n;
    }

    @Override
    public void run(){
        mergeSortParallel(this.data, this.n);
    }

    public static void mergeSortParallel(MyData[] data, int n){

        if (n < 2) {
            return;
        }

        int mid = n / 2;

        MyData[] l = new MyData[mid];
        MyData[] r = new MyData[n - mid];

        for (int i = 0; i < mid; i++) {
            l[i] = data[i];
        }

        for (int i = mid; i < n; i++) {
            r[i - mid] = data[i];
        }

        MyDataMergeSortParallel left = null;
        MyDataMergeSortParallel right = null;

        if(runningThreads.get() < totalThreads){
            left = new MyDataMergeSortParallel(l, mid);
            runningThreads.incrementAndGet();
            left.start();
        }else{
            mergeSortLinear(l,mid);
        }

        if(runningThreads.get() < totalThreads){
            right = new MyDataMergeSortParallel(r, n - mid);
            runningThreads.incrementAndGet();
            right.start();
        }else{
            mergeSortLinear(r, n - mid);
        }

        try {
            if (left != null) {
                left.join();
                runningThreads.decrementAndGet();
            }
            if (right != null) {
                right.join();
                runningThreads.decrementAndGet();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        merge(data, l, r, mid, n - mid);

    }


    public static void mergeSortLinear(MyData[] a, int n){
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
        mergeSortLinear(l, mid);
        mergeSortLinear(r, n - mid);

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
