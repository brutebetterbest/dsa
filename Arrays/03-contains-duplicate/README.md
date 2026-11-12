# 03 · Contains Duplicate

`Arrays` · `Easy` · [LeetCode #217](https://leetcode.com/problems/contains-duplicate/) · **Pattern: Hash set of seen values**

> **One-liner:** for each `num`, try `seen.add(num)`. `false` means it was already there → duplicate. If the loop finishes, every value was new → `false`.

## Problem

Given `nums`, return `true` if any value appears **at least twice**, and `false` if every element is distinct.

## From brute force to optimal

The lesson builds three solutions in order. The earlier ones are folded below; the optimal one, my solution, is the rest of this page.

<details>
<summary><b>1. Brute force</b> · O(n²) time · O(1) space</summary>

Compare every pair, and stop as soon as two are equal.

```java
class Solution {
    public boolean containsDuplicate(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] == nums[j]) {
                    return true;
                }
            }
        }
        return false;
    }
}
```

</details>

<details>
<summary><b>2. Better: sort + check neighbours</b> · O(n log n) time · O(1) extra space</summary>

After sorting, a duplicate sits right next to its twin, so one scan of neighbours is enough. It sorts the input array in place.

```java
class Solution {
    public boolean containsDuplicate(int[] nums) {
        Arrays.sort(nums);
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1]) {
                return true;
            }
        }
        return false;
    }
}
```

</details>

**3. Optimal: hash set** · O(n) time · O(n) space, explained below.

## Optimal approach: the move, repeated for every `num`

1. **Try to add it:** `seen.add(num)` checks and stores in one call.
2. **Already there? Duplicate:** `add` returned `false` → `return true`.
3. **New → it's stored, keep going:** `add` returned `true`, so `num` is now in `seen`.

## Why one pass is enough

If a value appears twice, its **first** copy is already in `seen` when the scan reaches the **second**. Every duplicate is caught at its second copy, so we never look back.

## Watch out

- **`add` returns `false` if the value is already there.** So `!seen.add(num)` means "duplicate". It also saves the second hash lookup that `contains` + `add` would do.
- **`return false` goes after the loop, never inside it.** One new number proves nothing: an `else return false;` would answer `false` for `[2, 5, 2]` after looking at the first `2`.
- **No extra memory allowed?** Sort, then compare neighbours (`nums[i] == nums[i - 1]`): O(n log n) time with no set.

## Edge case: no value repeats

With `[2, 5, 1]`, every `seen.add` returns `true`, so the `if` never fires. Only when `i` reaches `nums.length` and the loop ends do we know there's no duplicate, which is why `return false` sits after the loop.

## Complexity

| Approach | Time | Space |
|---|---|---|
| Brute force | O(n²) | O(1) |
| Better: sort + check neighbours | O(n log n) | O(1) extra |
| **Optimal: hash set** (my solution) | **O(n)**: one pass, O(1) average `add` | **O(n)**: `seen` holds up to `n` values |

## Recognize it next time

- "Does anything repeat?", "first repeated element", "all unique?" → a hash set of what you've seen. It's the same `seen` idea as Two Sum, with a set instead of a map because you only need the values.
- If extra memory is not allowed, or the array is already sorted, compare neighbours instead.

## My solution (optimal) · [`ContainsDuplicate.java`](ContainsDuplicate.java)

```java
class Solution {
    public boolean containsDuplicate(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        for (int num : nums) {
            if (!seen.add(num)) {
                return true;
            }
        }
        return false;
    }
}
```
