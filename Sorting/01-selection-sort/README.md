# 01 · Selection Sort

`Sorting` · `Easy` · [GeeksforGeeks](https://www.geeksforgeeks.org/problems/selection-sort/1) · **Pattern: Select the min, swap it to the front**

> **One-liner:** for each `i`, find `minIndex`, the smallest value in `arr[i .. n−1]`, and swap it into `arr[i]`. The sorted part on the left grows by one cell per pass.

## Problem

Given an array `arr[]`, sort it in increasing order using selection sort, in place. `1 ≤ arr.length ≤ 10³` and `1 ≤ arr[i] ≤ 10⁶`.

## Approach: one pass for each `i` from `0` to `n − 2`

1. **Find the smallest in `arr[i .. n−1]`:** start with `minIndex = i`. For every `j` after it, `if (arr[j] < arr[minIndex]) minIndex = j`.
2. **Swap it into `arr[i]`:** `if (minIndex != i) swap(arr, i, minIndex)`.
3. **`arr[i]` is final:** the sorted part `arr[0 .. i]` grew by one, so move on to `i + 1`.

## Why it works

Before pass `i`, `arr[0 .. i−1]` already holds the `i` smallest values, in order. Pass `i` picks the smallest of what's left. It is at least as big as everything before it, so placing it at `arr[i]` keeps `arr[0 .. i]` sorted, and no later pass touches it again. After pass `n − 2`, one value is left over, and it must be the largest.

## Watch out

- **Swap once, after the scan.** The inner loop only moves `minIndex`. Swapping every time you see a smaller value still sorts, but does far more writes. Selection sort does at most `n − 1` swaps, which is its one real strength.
- **`i < arr.length - 1`, not `i < arr.length`.** The last value needs no pass of its own: once `n − 1` cells hold the smallest values, the one left over is the largest.
- **Compare with `<`, not `<=`.** With `<=`, an equal value later in the array takes over `minIndex`, and you swap equal values for nothing.

## Edge case: equal values (not stable)

With `[3, 3, 1]`, the 2nd 3 never takes over `minIndex`, because equal isn't smaller. But `swap(arr, 0, 2)` sends the 1st 3 to the end, past its twin. The result `[1, 3, 3]` is sorted, yet the two 3s are now in reverse order. So selection sort is **not stable**. That only matters when you sort records by a key, e.g. people by age, where the original order of ties should be kept.

## Complexity

| Time | Space |
|---|---|
| **O(n²)** in every case: `(n−1) + (n−2) + … + 1 = n(n−1)/2` comparisons, even when `arr` is already sorted. At most `n − 1` swaps. | **O(1)**: sorts in place with just `minIndex` and `temp` |

## Recognize it next time

- "Sort using selection sort", or a problem where writes are expensive and you want the fewest swaps: selection sort needs at most `n − 1`.
- Nearby simple sorts: **bubble sort** swaps neighbours, is stable, and can stop early on sorted input with a `swapped` flag. **Insertion sort** is stable and runs in O(n) on nearly sorted input. Neither has selection sort's low swap count.
- **Heap sort** is the same "select the extreme of the rest" idea, but a heap finds each one in O(log n), giving O(n log n). In real code, use `Arrays.sort`.

## My solution · [`SelectionSort.java`](SelectionSort.java)

```java
class Solution {
    void selectionSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                swap(arr, i, minIndex);
            }
        }
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
```
