# README.md

## A. Project Overview
The purpose of this project is to implement, benchmark, and theoretically analyze classic divide-and-conquer algorithms to compare their mathematical time complexities against practical execution performance. 

The implemented algorithms include:
*   MergeSort (with auxiliary buffer and Insertion Sort cutoff)
*   QuickSort (with randomized pivot and 3-way in-place partitioning)
*   Deterministic Select (Median-of-Medians)
*   Closest Pair of Points (2D divide-and-conquer)

## B. Algorithm Analysis

### 1. MergeSort
*   **How it works:** The algorithm recursively divides an array into two halves until a base case or cutoff threshold is reached. It then linearly merges the sorted sub-arrays back together using a pre-allocated auxiliary buffer.
*   **Time/Space Complexity:** Time is Theta(n log n) in all cases. Space is Theta(n) due to the auxiliary array.
*   **Recurrence & Analysis:** 
    T(n) = 2T(n/2) + Theta(n)
    Using the Master Theorem, we have a=2, b=2, and f(n) = Theta(n). Since n^(log_b a) = n^(log_2 2) = n^1, this fits Case 2 of the Master Theorem where f(n) = Theta(n^(log_b a)). Therefore, the complexity is Theta(n log n).

### 2. QuickSort
*   **How it works:** A random pivot is selected, and the array is partitioned in-place into three segments (less than, equal to, and greater than the pivot). To restrict stack usage, the algorithm recurses only on the smaller partition and iteratively processes the larger one.
*   **Time/Space Complexity:** Expected Time is O(n log n), Worst-case Time is O(n^2). Space is strictly O(log n) due to the tail-recursion optimization on the smaller partition.
*   **Recurrence & Analysis:** 
    Expected case: T(n) = 2T(n/2) + Theta(n) 
    By the Master Theorem (Case 2), this resolves to O(n log n). 
    Worst case (highly unbalanced partitions): T(n) = T(n-1) + Theta(n) 
    This resolves to an arithmetic progression sum, resulting in O(n^2).

### 3. Deterministic Select (Median-of-Medians)
*   **How it works:** The input is divided into chunks of 5 elements. The median of each chunk is found, and the algorithm recursively finds the median of those medians to use as a guaranteed well-balanced pivot. It partitions the array and recurses only into the side containing the k-th element.
*   **Time/Space Complexity:** Time is Theta(n) worst-case. Space is Theta(log n) for the recursion stack.
*   **Recurrence & Analysis:** 
    T(n) <= T(n/5) + T(7n/10) + Theta(n)
    Using Akra-Bazzi intuition, the sum of the recurrence fractions is 1/5 + 7/10 = 9/10. Because 9/10 < 1, the linear work Theta(n) performed at each level dominates the decreasing subproblem sizes, resulting in a strict Theta(n) overall runtime.

### 4. Closest Pair of Points
*   **How it works:** Points are sorted by their X and Y coordinates. The set is split in half by a vertical line, and the closest pair is found recursively in both halves. A vertical "strip" spanning the minimum distance d across the dividing line is constructed, and points within the strip are compared to find any closer pairs crossing the boundary.
*   **Time/Space Complexity:** Time is Theta(n log n). Space is Theta(n) for the sorted arrays and strip generation.
*   **Recurrence & Analysis:** 
    T(n) = 2T(n/2) + Theta(n)
    The combine step is linear Theta(n) because each point in the strip is compared against a constant maximum of 7 neighbors. By the Master Theorem (Case 2), this yields Theta(n log n).

## C. Experimental Results

**Execution-Time & Recursion-Depth Tables**

| Algorithm | Input Type | Size (n) | Execution Time (ns) | Max Depth |
| :--- | :--- | :--- | :--- | :--- |
| MergeSort | Random | 100,000 | [Insert Data from CSV] | [Insert Data from CSV] |
| QuickSort | Duplicates | 100,000 | [Insert Data from CSV] | [Insert Data from CSV] |
| Det. Select | Sorted | 100,000 | [Insert Data from CSV] | [Insert Data from CSV] |
| Closest Pair | Random 2D | 100,000 | [Insert Data from CSV] | [Insert Data from CSV] |

**Plots**
*   <img width="1919" height="1079" alt="time_vs_n" src="https://github.com/user-attachments/assets/0edacc5e-a00e-4b85-aa10-cb76ff1cb3e8" />
*   <img width="1919" height="1079" alt="depth_vs_n" src="https://github.com/user-attachments/assets/bf12a068-2f7d-4394-8ad4-2b46971570fb" />


## D. Discussion

*   **Do the results match theoretical complexity?** Yes, both MergeSort and QuickSort scale at an expected n log n rate across random inputs. The 3-way partitioning prevents QuickSort from degrading to O(n^2) on duplicate-heavy arrays.
*   **How does input structure affect performance?** Fully sorted or reverse-sorted data heavily penalizes naive pivot selection. By utilizing randomized pivots, QuickSort maintains stable execution speeds regardless of initial input order.
*   **Why does smaller-first recursion help QuickSort?** It strictly limits the call stack depth. By transforming the second recursive call into a loop and always recursing on the partition smaller than n/2, the maximum recursion depth is mathematically bounded to O(log n), preventing StackOverflow errors on degenerate inputs.
*   **Why does Median-of-Medians guarantee O(n)?** It forces a highly balanced partition. The selected pivot is mathematically guaranteed to be greater than at least 3/10 of the elements and less than at least 3/10. This ensures the problem size shrinks by a constant fraction (at worst 7/10), preventing the O(n^2) degradation seen in standard QuickSelect.
*   **Why is divide-and-conquer Closest Pair faster than O(n^2) for large inputs?** A brute-force approach uselessly compares points located on opposite sides of the grid. Divide-and-conquer isolates the search space. In the combine phase, geometric constraints guarantee that each point in the boundary strip only needs to be checked against a maximum of 7 other points, turning an O(n^2) search into a linear O(n) merge.
*   **What practical factors affect performance?** Beyond pure Big-O theory, the JVM garbage collector (GC) heavily impacts execution times. Algorithms that rapidly allocate new objects (like generating Point arrays in Closest Pair) trigger frequent GC sweeps, causing performance spikes. Furthermore, QuickSort benefits from high CPU cache locality because it swaps primitives in place, whereas MergeSort suffers slightly due to constant out-of-place writes to the auxiliary buffer.

## E. Reflection
Navigating the transition into algorithm implementation highlighted distinct differences from writing structural Java code. While architectural patterns focus heavily on object creation and class hierarchies, this assignment required a much stricter focus on memory management and execution limits. Managing primitive arrays in MergeSort was straightforward, but handling the Point objects in the Closest Pair algorithm exposed the heavy toll of rapid object allocation on the Java Garbage Collector, causing noticeable performance spikes and fan-spin during benchmarking.

A primary implementation challenge was ensuring QuickSort strictly adhered to an O(log n) recursion depth. Converting the standard double-recursive structure into a tail-recursive loop that dynamically checks partition sizes required a shift in how I conceptualized stack frames. Additionally, bridging the gap between theoretical math—like manually calculating recurrences with the Master Theorem—and observing actual JVM execution times made the abstract concepts highly tangible.

## F. Screenshots
*   <img width="1919" height="1079" alt="program_output" src="https://github.com/user-attachments/assets/0fe2c99d-1af9-45e5-b8da-02ea8c47f6ea" />
*   <img width="1919" height="1079" alt="test_results" src="https://github.com/user-attachments/assets/fbd0bb53-7338-44d1-a240-55a7531a2582" />

