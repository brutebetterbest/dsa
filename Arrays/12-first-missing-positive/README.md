# 12 · First Missing Positive

`Arrays` · `Hard` · [LeetCode #41](https://leetcode.com/problems/first-missing-positive/) · **Pattern: Cyclic sort (index = value − 1)**

> **One-liner:** the answer is always in `1..n + 1`, so give every value `1..n` its home index `value − 1` by swapping it there, skipping values out of range and values whose home already holds them. Then the first index `i` that doesn't hold `i + 1` gives the answer; if every index does, it's `n + 1`.

## Problem

Given an unsorted integer array `nums`, return the smallest positive integer that isn't in it. The solution must run in O(n) time and use O(1) extra space. `1 ≤ n ≤ 10⁵` and `−2³¹ ≤ nums[i] ≤ 2³¹ − 1`.

## From brute force to optimal

The lesson builds three solutions in order. The earlier ones are folded below; the optimal one, my solution, is the rest of this page.

<details>
<summary><b>1. Brute force: try 1, 2, 3, … and scan for each</b> · O(n²) time · O(1) space</summary>

Try `candidate = 1, 2, 3, …` and scan the whole array for each one; the first candidate not found is the answer. With `n` numbers, at most `1..n` can all be present, so the answer is never above `n + 1`.

```java
class Solution {
    public int firstMissingPositive(int[] nums) {
        int n = nums.length;
        for (int candidate = 1; candidate <= n; candidate++) {
            boolean found = false;
            for (int num : nums) {
                if (num == candidate) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return candidate;
            }
        }
        return n + 1;
    }
}
```

</details>

<details>
<summary><b>2. Better: a <code>seen</code> array for 1..n</b> · O(n) time · O(n) space</summary>

Every candidate rescans the same array. Since the answer is in `1..n + 1`, only values `1..n` matter: mark them in a `boolean[] seen` of size `n + 1` in one pass, skipping values `≤ 0` or `> n`, then return the first value not marked.

```java
class Solution {
    public int firstMissingPositive(int[] nums) {
        int n = nums.length;
        boolean[] seen = new boolean[n + 1];
        for (int num : nums) {
            if (num > 0 && num <= n) {
                seen[num] = true;
            }
        }
        for (int value = 1; value <= n; value++) {
            if (!seen[value]) {
                return value;
            }
        }
        return n + 1;
    }
}
```

</details>

**3. Optimal: cyclic sort, the array is its own `seen` table** · O(n) time · O(1) space, explained below.

## Optimal approach: send every value home, then find the first empty home

The `seen` array has one slot per value `1..n`, and `nums` already has `n` slots. So use `nums` itself: value `v` lives at index `v − 1`, its home.

1. **Look at `nums[i]`.** If it's in `1..n` and its home `correctIndex = nums[i] − 1` doesn't already hold it, swap it there.
2. **Keep going at the same `i`.** The swap brought a new value to index `i`, and it needs its home too, so it's a `while`, not an `if`.
3. **Move on** when `nums[i]` is out of range (`≤ 0` or `> n`), already home, or a duplicate whose home already holds the same value.
4. **Scan for the first index `i` with `nums[i] != i + 1`.** That's the missing number, `i + 1`. If every index holds its value, the answer is `n + 1`.

On `[3, 4, −1, 1, 9, 3]` (`n = 6`): at `i = 0`, the `3` swaps home to index 2 and brings back the `−1`, which is out of range. At `i = 1`, the `4` swaps home to index 3 and brings back the `1`; the `1` swaps home to index 0 and brings back the `−1`. The `3` and `4` at indexes 2 and 3 are already home, the `9` is too big, and the last `3` finds a `3` already at its home, so it stays. The array is now `[1, −1, 3, 4, 9, 3]`: index 1 doesn't hold `2`, so the answer is `2`.

## Why it works

The answer is at most `n + 1`, because `n` numbers can fill at most the values `1..n`. After the first loop, index `i` holds `i + 1` exactly when `i + 1` is somewhere in the array, so the first index that doesn't gives the smallest missing positive. It's O(n) even with the `while` inside the `for`: every swap puts one value at its home, where it never moves again, so there are at most `n` swaps in the whole run.

## Watch out

- **`while`, not `if`.** The value swapped into index `i` needs placing too. With `if`, `[3, 4, −1, 1]` returns `1`: the `1` lands at index 1 and never goes home, so index 0 looks empty. The real answer is `2`.
- **Stop when the home already holds the value.** On `[1, 1]` the second `1`'s home already holds a `1`; swapping them would loop forever. `nums[nums[i] - 1] != nums[i]` stops it, and it also covers a value that's already home, so a separate `nums[i] != i + 1` check isn't needed.
- **Check the range before using the value as an index.** `nums[i] > 0 && nums[i] <= n` comes first in the condition, so `0`, negatives and values like `Integer.MAX_VALUE` never reach `nums[nums[i] - 1]`.
- **Every value `1..n` present means `n + 1`.** `[1, 2, 3]` returns `4`.
- **It rearranges `nums`.** That's what makes it O(1) extra space; copy the array first if the caller still needs it.

## Complexity

| Approach | Time | Space |
|---|---|---|
| Brute force: try 1, 2, 3, … and scan for each | O(n²) | O(1) |
| Better: a `seen` array for 1..n | O(n) | O(n): the `seen` array |
| **Optimal: cyclic sort** (my solution) | **O(n)**: at most `n` swaps, each sends a value home for good | **O(1)**: the array is its own `seen` table |

## Recognize it next time

- Values that should be `1..n` (or `0..n`), a missing or duplicate number, and O(1) extra space: cyclic sort, putting each value at index `value − 1`. Missing Number, Find All Numbers Disappeared in an Array and Find the Duplicate Number use the same move.
- If extra space is allowed, the `seen` array or a `HashSet` is simpler.
- Another O(1)-space way marks "seen" by making `nums[value − 1]` negative, after replacing values `≤ 0` with `n + 1`.

## My solution (optimal) · [`FirstMissingPositive.java`](FirstMissingPositive.java)

```java
class Solution {
    public int firstMissingPositive(int[] nums) {
        int n = nums.length;

        // put every value 1..n at its home index (value - 1)
        for (int i = 0; i < n; i++) {
            while (nums[i] > 0 && nums[i] <= n && nums[nums[i] - 1] != nums[i]) {
                int correctIndex = nums[i] - 1;
                int temp = nums[correctIndex];
                nums[correctIndex] = nums[i];
                nums[i] = temp;
            }
        }

        // the first index not holding index + 1 is the missing number
        for (int i = 0; i < n; i++) {
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }
        return n + 1;
    }
}
```
