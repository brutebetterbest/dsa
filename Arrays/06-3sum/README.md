# 06 · 3Sum

`Arrays` · `Medium` · [LeetCode #15](https://leetcode.com/problems/3sum/) · **Pattern: Sort + two pointers**

> **One-liner:** sort, then for each `nums[i]` walk `left` and `right` toward each other: `sum < 0` → `left++`, `sum > 0` → `right--`, `sum == 0` → add it. Skipping repeated values at `i`, `left` and `right` keeps every triplet unique.

## Problem

Given `nums`, return every triplet `[nums[i], nums[j], nums[k]]` with three different indices that sums to `0`. The answer must not contain the same triplet twice, and the order doesn't matter. `3 ≤ nums.length ≤ 3000` and `−10⁵ ≤ nums[i] ≤ 10⁵`.

## From brute force to optimal

The lesson builds three solutions in order. The earlier ones are folded below; the optimal one, my solution, is the rest of this page.

<details>
<summary><b>1. Brute force</b> · O(n³) time · O(1) extra space, besides the answers</summary>

Try every triplet `i < j < k`. Sort each zero-sum triplet before putting it in a set, so copies like `[-1, 0, 1]` and `[0, 1, -1]` collapse into one.

```java
class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        Set<List<Integer>> triplets = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                for (int k = j + 1; k < nums.length; k++) {
                    if (nums[i] + nums[j] + nums[k] == 0) {
                        List<Integer> triplet = Arrays.asList(nums[i], nums[j], nums[k]);
                        Collections.sort(triplet);
                        triplets.add(triplet);
                    }
                }
            }
        }
        return new ArrayList<>(triplets);
    }
}
```

</details>

<details>
<summary><b>2. Better: fix <code>nums[i]</code> + Two Sum with a hash set</b> · O(n²) time · O(n) space</summary>

Once `i` and `j` are picked, `k` can only be one value, `−(nums[i] + nums[j])`. So fix `nums[i]` and solve Two Sum on the rest with a hash set.

```java
class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        Set<List<Integer>> triplets = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            Set<Integer> seen = new HashSet<>();
            for (int j = i + 1; j < nums.length; j++) {
                int needed = -(nums[i] + nums[j]);
                if (seen.contains(needed)) {
                    List<Integer> triplet = Arrays.asList(nums[i], nums[j], needed);
                    Collections.sort(triplet);
                    triplets.add(triplet);
                }
                seen.add(nums[j]);
            }
        }
        return new ArrayList<>(triplets);
    }
}
```

</details>

**3. Optimal: sort + two pointers** · O(n²) time · O(log n) space, explained below.

## Optimal approach: sort, then the move for every `i`

1. **Skip a repeated `nums[i]`:** `if (i > 0 && nums[i] == nums[i - 1]) continue;`. The same first number would only find the same triplets again.
2. **Two pointers on the rest:** `left = i + 1`, `right = nums.length - 1`. If `sum < 0`, `left++` for a bigger number. If `sum > 0`, `right--` for a smaller one.
3. **`sum == 0`: add it, then skip repeats:** `result.add(...)`, move both pointers, then step past values equal to the ones just used.

## Why it works

After sorting, `left++` can only make the sum bigger and `right--` can only make it smaller. If `sum < 0`, even the biggest partner left, `nums[right]`, isn't enough for `nums[left]`, so `nums[left]` can't be in any triplet with this `i` and it's safe to drop. `sum > 0` drops `nums[right]` the same way. Every move rules out one number, so one sweep checks every pair for this `i` in O(n).

## Watch out

- **Skip repeats in three places: `i`, `left` and `right`.** In the lesson's example, `[-4, -1, -1, 0, 1, 2]` after sorting, the second `-1` would find `[-1, 0, 1]` again without the `i` skip. Without the pointer skips, `[-2, 0, 0, 1, 2, 2]` would add `[-2, 0, 2]` twice.
- **After a match, move both pointers, then skip.** Check `left < right` before reading `nums[left]` or `nums[right]`.
- **3Sum returns values, not indices**, so sorting is allowed. Two Sum has to return indices, which is why it uses a hash map instead.

## Complexity

| Approach | Time | Space |
|---|---|---|
| Brute force | O(n³) | O(1) extra, besides the answers |
| Better: fix `nums[i]` + Two Sum with a hash set | O(n²) | O(n) |
| **Optimal: sort + two pointers** (my solution) | **O(n²)**: an O(n log n) sort, then an O(n) two-pointer sweep for each `i` | **O(log n)**: the recursion stack of `Arrays.sort` on an `int[]`. `result` is the output, so it isn't counted |

## Recognize it next time

- "Find all **unique** pairs, triplets or quadruplets with a given sum": sort + two pointers, and skip repeats. 4Sum adds one more outer loop, for O(n³).
- 3Sum Closest is the same sweep, but it tracks the sum closest to the target instead of an exact 0.
- The inner loop on its own is Two Sum II (sorted input). Two Sum on unsorted input needs indices, so it uses a hash map instead.
- Optional speed-up: `if (nums[i] > 0) break;` right after the skip. Once the smallest number is positive, no triplet can reach 0.

## My solution (optimal) · [`ThreeSum.java`](ThreeSum.java)

```java
class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);

        for (int i = 0; i < nums.length - 2; i++) {
            // skip repeated number
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int left = i + 1;
            int right = nums.length - 1;

            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (sum == 0) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    left++;
                    right--;
                    while (left < right && nums[left] == nums[left - 1]) {
                        left++;
                    }
                    while (left < right && nums[right] == nums[right + 1]) {
                        right--;
                    }
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return result;
    }
}
```
