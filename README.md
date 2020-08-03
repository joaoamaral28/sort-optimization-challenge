# sort-optimization-challenge
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

In my_random_data.csv the keys are random integers within the interval [ 1, (10*K )[, while in my_random_data_incremental.csv sequential keys have incremental diferences of one unit, that is, keys are within the interval [ 1, K [. 

The sorting algorithms are performed based on the primary_key value.

Dataset structure example: 

| Primary_key  | reference_value|
| ------------- | ------------- |
| 82010118 | 0  |
| 4510676  | 1  |
| 31698097 | 2 | 
| .... | ... |

# Algorithms tested

* Arrays.sort (default java sort implementation, uses merge sort under the hood)
* Radix sort
* Merge sort
* Merge sort with parallelization (2 variants)
  * Manual thread scheduling
  * Automatic thread scheduling using ForkJoinPool (includes a minimum partition size tunable parameter that controls if a sub array is eligible for paralelization or not)
* Quick sort
* Quick sort hybrid (using insertion sort)
* Quick sort with parallelization (2 variants)
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
    <th colspan="2">Average time using dataset</th>
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
  <td> XXX ms </td>
  <td> XXX ms </td> 
 </tr>
    <tr> 
  <td> Quick sort </td> 
  <td> 345 ms </td>
  <td> 328 ms </td> 
 </tr>
 <tr> 
  <td> Quick sort hybrid </td> 
  <td> 334 ms </td>
  <td> 303 ms </td> 
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
</table>

# Results analysis


From the get go it is possible to observe that the java internal Arrays sort() method loses in performance against the implemented recursive merge sort and quick sort.
Additionally, and as expected since the keys are large integer values, radix sort is the worst performant algorithm. 

The rationale was then to try and optimize both these algorithms. Since they are algorithms that follow the approach of "divide and conquer", we can parallelize the algorithm recursive calls in order to speed up this process. 

# Build & Deploy 

....
