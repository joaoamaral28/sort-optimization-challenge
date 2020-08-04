# Sort optimization challenge
My solution of a hiring challenge regarding sort algorithm optimization

# Objective

The objective of this challenge was to develop and optimum sorting algorithm that would be capable to 
sort a very large dataset in the shortest amount of time possible. 

The only constraint is that the chosen dataset must have a unique real positive integer value, known as "key", through which
the sorting will be performed on. 

# Dataset structure

The algorithms were tested against two data sets (my_random_data.csv and my_random_data_incremental.csv) both containing K=1000000 records and the following columns: 
* primary_key: a random unique real positive integer 
* reference_value: a real positive integer

In my_random_data.csv the keys are random integers within the interval [ 1, 10 * K ) [ , while in my_random_data_incremental.csv sequential keys have incremental diferences of one unit, that is, keys are within the interval [ 1, K [ . 

The sorting algorithms are performed based on the primary_key value. In a real scenario the dataset would be compsed by the primary_key field and multiple other data fields.
However, for simplicity purposes, we just use a integer placeholder field, the reference_value (which could also be of any other data type).  

Dataset structure example: 

| Primary_key  | reference_value|
| ------------- | ------------- |
| 82010118 | 0  |
| 4510676  | 1  |
| 31698097 | 2 | 
| .... | ... |

# Algorithms tested

* Arrays.sort (default java sort implementation, uses merge sort under the hood)
* Radix sort (using counting sort)
* Merge sort:
  * Merge sort recursive
  * Merge sort with parallelization (2 variants):
     * Manual thread scheduling
     * Automatic thread scheduling using ForkJoinPool (includes a minimum partition size tunable parameter that controls if a sub array is eligible for paralelization or not)
* Quick sort:
  * Quick sort recursive
  * Quick sort with parallelization (2 variants):
    * Manual thread scheduling
    * Automatic thread scheduling using ForkJoinPool
  
* Quick sort hybrid (using insertion sort):
  * Quick sort hybrid recursive
  * Quick sort hybrid with parallelization (2 variants):
     * Manual thread scheduling
     * Automatic thread scheduling using ForkJoinPool (simillar to merge sort parallel variant)


# Benchmarking results

Results of the average execution time of each algorithm after N=1000 trials

<!--
| Algorithm | Avg. sorting time | |
| ------------- | ------------- | - |
| Radix sort  | 1022 ms | |
| Arrays.sort | 392 ms| | |
| Merge sort  | 317 ms  | |
| Merge sort parallel  | 164 ms  | |
|Quick Sort| 345 ms | |
| Quick sort hybrid| 334 ms | |
| Quick sort parallel | 272 ms | | 
| Quick sort parallel2 | 159 ms| |
-->

<table>
  <tr>
    <th rowspan="2">Algorithm</th>
    <th colspan="2">Average time per dataset</th>
  </tr>
 <tr> 
  <td> my_random_data </td> 
  <td> my_random_data_incremental </td>
 </tr>
  <tr> 
  <td> Radix sort </td> 
  <td> 1022 ms </td>
  <td> 648 ms </td> 
 </tr>
 </tr>
  <tr> 
  <td> Arrays.sort </td> 
  <td> 392 ms </td>
  <td> 348 ms </td> 
 </tr>
   <tr> 
  <td> Merge sort </td> 
  <td> 317 ms </td>
  <td> 320 ms </td> 
 </tr>
   <tr> 
  <td> Merge sort parallel </td> 
  <td> 164 ms </td>
  <td> 128 ms </td> 
 </tr>
  <tr> 
  <td> Merge sort parallel fork </td> 
  <td> 156 ms </td>
  <td> 139 ms </td> 
 </tr>
    <tr> 
  <td> Quick sort </td> 
  <td> 345 ms </td>
  <td> 328 ms </td> 
 </tr>
 <tr> 
  <td> Quick sort parallel </td> 
  <td> 272 ms </td>
  <td> 176 ms </td> 
 </tr>
 <tr> 
  <td> Quick sort parallel fork </td> 
  <td> 159 ms </td>
  <td> 137 ms </td> 
 </tr>
 <tr> 
  <td> Quick sort hybrid </td> 
  <td> 334 ms </td>
  <td> 303 ms </td> 
 </tr>
 <tr> 
  <td> Quick sort hybrid parallel </td> 
  <td> 212 ms </td>
  <td> 154 ms </td> 
 </tr>
 <tr> 
  <td> Quick sort hybrid parallel fork </td> 
  <td> 118 ms </td>
  <td> 111 ms </td> 
 </tr>

</table>

# Results analysis

From the get go it is possible to observe that the java internal Arrays sort() method loses in performance against the implemented recursive merge sort and quick sort.
Additionally, and as expected since the keys are large integer values, radix sort is the worst performant algorithm. The rationale was then to try and optimize the quick sort and merge sort algorithms. 

Since they follow the approach of "divide and conquer", we can parallelize the algorithms recursive calls in order to speed up this process. As such, each algorithm contains two variants, one where the algorithms are implemented as a thread and the thread scheduling is done implicitly ([MergeSortParallel](https://github.com/joaoamaral28/sort-optimization-challenge/blob/master/SortingChallenge/src/main/java/SortAlgorithms/MergeSort/MyDataMergeSortParallel.java), [QuickSortParallel](https://github.com/joaoamaral28/sort-optimization-challenge/blob/master/SortingChallenge/src/main/java/SortAlgorithms/QuickSort/MyDataQuickSortParallel.java)) and other where the algorithms are implemented as a [RecursiveAction](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/RecursiveAction.html) and executed inside a [ForkJoinPool](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ForkJoinPool.html), where the scheduling is handled automatically ([MergeSortParallelFork](https://github.com/joaoamaral28/sort-optimization-challenge/blob/master/SortingChallenge/src/main/java/SortAlgorithms/MergeSort/MyDataMergeSortParallelFork.java), [QuickSortParallelFork](https://github.com/joaoamaral28/sort-optimization-challenge/blob/master/SortingChallenge/src/main/java/SortAlgorithms/QuickSort/MyDataQuickSortParallelFork.java)). 

Another approach was to mix the quick sort algorithm with the insertion sort ([QuickSortHybrid](https://github.com/joaoamaral28/sort-optimization-challenge/blob/master/SortingChallenge/src/main/java/SortAlgorithms/QuickSortHybrid/MyDataQuickSortHybrid.java)), a commonly used strategy to improve its performance. The way it works is that in the algorithm current recursive call if the data partition that is being processed is lower than a specified threshold (INSERTION_THRESHOLD=10), it is better to sort it using insertion sort as it performs fewer operations rather than to keep spliting them. The result is a slight performance boost compared to the regular recursive quick sort.

On the same way as merge and quick sort, the quick sort hybrid algorithm was also parallelized using two different approaches. This resulted in the algorithm with the fastest sorting performance ([QuickSortHybridParallelFork](https://github.com/joaoamaral28/sort-optimization-challenge/blob/master/SortingChallenge/src/main/java/SortAlgorithms/QuickSortHybrid/MyDataQuickSortHybridParallelFork.java)). 

# Build & Deploy 

### Pre-requisites

* [Docker](https://www.docker.com/get-started)

### Local deployment

Clone the repository to your local machine 

```shell
$ git clone https://github.com/joaoamaral28/sort-optimization-challenge.git
```

Open project directory

```shell
$ cd SortingChallenge
```

Build the docker image

```shell
$ docker build -f Dockerfile -t sorting-challenge ./
```

Run the image

```shell
$ docker run sorting-challenge
```

The application will then execute inside the docker container. It will parse the dataset and sort it using the fastest performant algorithm, in this case
the [QuicksortHybridParallelFork](https://github.com/joaoamaral28/sort-optimization-challenge/blob/master/SortingChallenge/src/main/java/SortAlgorithms/QuickSortHybrid/MyDataQuickSortHybridParallelFork.java)

### Running the benchmark test

The algorithms benchmark tests were implemented as JUnit tests which are present in the [AlgorithmsPerformanceBenchmarkTest](https://github.com/joaoamaral28/sort-optimization-challenge/blob/master/SortingChallenge/src/test/java/SortAlgorithms/AlgorithmsPerformanceBenchmarkTest.java) class. In the development stages the benchmark tests were run directly through the IDE (IntelliJ). All attempts of running the test directly in the terminal through maven were
unsuccessfull (most likely an issue with junit and maven-surefire-plugin):

```shell
$ mvn clean test -Dtest=SortingAlgorithms.AlgorithmsPerformanceBenchmarkTest
```

On the same note, the Dockerfile contains an environment variable (group) that can controll which tests, unit (default) or benchmark, are meant to be run when deploying the maven project inside the docker container. However, it also suffers from the same issue where maven cannot distinguish the two tests, which are properly tagged with their respective annotation ("unit" and "benchmark").

```shell
$ docker build -f Dockerfile --build-arg group=benchmark -t sorting-challenge ./
```

# Future work

* Adjust the algorithms tunable parameters of the parallel fork variant (minPartitionSize, INSERTION_THRESHOLD) in order to find the best values for this dataset and further optimize the algorithms if possible.
* Fix the maven build in order to allow modularity for running the benchmark tests.


# Sources

* [Parallel Merge Sort in Java](https://medium.com/@teivah/parallel-merge-sort-in-java-e3213ae9fa2c)
* [Performance analysis on multithreaded sorting algorithms](https://www.diva-portal.org/smash/get/diva2:839729/FULLTEXT02)
* [Hybrid quicksort algorithm](https://www.techiedelight.com/hybrid-quicksort/)
* [Merge sort with ForkJoinPool](https://dehasi.github.io/java/2017/06/06/merge-sort-with-fork-join.html)
