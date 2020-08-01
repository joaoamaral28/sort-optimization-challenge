# sort-optimization-challenge
My solution of a hiring challenge regarding sort algorithm optimization

# Objective

The objective of this challenge was to develop and optimum sorting algorithm that would be capable to 
sort a very large dataset in the shortest amount of time possible. 

The only constraint is that the chosen dataset must have a unique real positive integer value, known as "key", through which
the sorting will be performed on. 

# Dataset structure

The dataset used for this test was the my_random_data.csv (15.7 MB) file, which contains 994920 records and the following columns:
* primary_key: a random unique real positive integer 
* reference_value: a real positive integer

The sorting algorithms are performed based on the primary_key value

Example: 

| Primary_key  | reference_value|
| ------------- | ------------- |
| 82010118 | 0  |
| 4510676  | 1  |
| 31698097 | 2 | 
| .... | ... |

# Algorthims tested

* Arrays.sort
* Radix sort
* Merge sort
* Merge sort with parallelization
* Quick sort
* Quick sort hybrid (with Insertion sort)
* Quick sort with parallelization

# Results

| Algorithm  | Avg. sorting time (s)|
| ------------- | ------------- |
| Arrays.sort |
| Radix sort  |  | 
| Merge sort  |   |
| Merge sort parallel  |   |
|Quick Sort|  |
| Quick sort hybrid|  |
| Quick sort parallel |  |
