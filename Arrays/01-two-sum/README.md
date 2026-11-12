# 01 · Two Sum

`Arrays` · `Easy` · [LeetCode #1](https://leetcode.com/problems/two-sum/) · **Pattern: Hash map of seen values**

[![Two Sum: the lesson, from brute force to optimal](https://img.youtube.com/vi/r0tNiRyVwyI/maxresdefault.jpg)](https://youtu.be/r0tNiRyVwyI)

▶ **[Watch the lesson](https://youtu.be/r0tNiRyVwyI)** · Quick version: **[Short](https://youtube.com/shorts/oVb-YNIuttg)**

> **One-liner:** for each `x`, ask *"have I already seen `target − x`?"* Yes → answer found. No → remember `x` and move on.

## Problem

Given `nums` and `target`, return the **indices** of the two numbers that add up to `target`. Exactly one answer exists, and the same element can't be used twice.

## From brute force to optimal

The lesson builds three solutions in order. The earlier ones are folded below; the optimal one, my solution, is the rest of this page.

<details>
<summary><b>1. Brute force</b> · O(n²) time · O(1) space</summary>

Check every pair: fix `i`, try every `j` after it.

```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[] {i, j};
                }
            }
        }
        return new int[] {};
    }
}
```

</details>

<details>
<summary><b>2. Better: sort + two pointers</b> · O(n log n) time · O(n) space</summary>

Sort the numbers, keeping each one's original index. Then `left` starts at the smallest and `right` at the largest: too small → `left++`, too big → `right--`.

```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        // each pair is {value, original index}, sorted by value
        int[][] pairs = new int[nums.length][];
        for (int i = 0; i < nums.length; i++) {
            pairs[i] = new int[] {nums[i], i};
        }
        Arrays.sort(pairs, (a, b) -> Integer.compare(a[0], b[0]));

        int left = 0, right = nums.length - 1;
        while (left < right) {
            int sum = pairs[left][0] + pairs[right][0];
            if (sum == target) {
                return new int[] {pairs[left][1], pairs[right][1]};
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        return new int[] {};
    }
}
```

</details>

**3. Optimal: hash map** · O(n) time · O(n) space, explained below.

## Optimal approach: the move, repeated for every `x = nums[i]`

1. **Its partner is fixed:** `complement = target − x`, so there's nothing to search for.
2. **Already seen the complement?** If `seen` has it → `return [seen.get(complement), i]`.
3. **No → remember `x`:** `seen.put(x, i)` and move right.

## Why one pass is enough

Take the answer pair `(j, i)` with `j < i`. When the scan reaches `i`, `nums[j]` is already in `seen`, so every pair is caught at its **right** element. We never need to look back.

## Watch out

- **Check before you put.** An element can never pair with itself (`[3, 4]`, target `6` → `3` is not matched with `3`).
- **Duplicates work.** `[3, 3]`, target `6` → the first `3` is waiting in `seen` when the second one arrives.
- **Store `value → index`.** The answer is indices, not values.

## Complexity

| Approach | Time | Space |
|---|---|---|
| Brute force | O(n²) | O(1) |
| Better: sort + two pointers | O(n log n) | O(n) |
| **Optimal: hash map** (my solution) | **O(n)**: one pass, O(1) average lookup | **O(n)**: the map holds up to `n` values |

## Recognize it next time

- Find a **pair** with a given **sum or difference** in an **unsorted** array, and return **indices** → keep a hash map of what you've seen.
- If the array is **sorted** and you only need values, two pointers do it in O(1) space.

## My solution (optimal) · [`TwoSum.java`](TwoSum.java)

```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if(seen.containsKey(complement)) {
                return new int[] {seen.get(complement),i};
            }
            seen.put(nums[i],i);
        }
        return new int[]{};
    }
}
```
