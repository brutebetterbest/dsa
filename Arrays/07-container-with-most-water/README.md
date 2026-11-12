# 07 · Container With Most Water

`Arrays` · `Medium` · [LeetCode #11](https://leetcode.com/problems/container-with-most-water/) · **Pattern: Two pointers from both ends**

> **One-liner:** start with the widest pair, `left = 0` and `right = n − 1`. The water is `width × waterHeight`, where `waterHeight` is the **shorter** line. Keep the best in `maxArea`, then move the shorter line inward.

## Problem

Given `height`, where line `i` goes from `(i, 0)` to `(i, height[i])`, pick two lines that, together with the x-axis, hold the most water, and return that area. The container can't be tilted. `2 ≤ height.length ≤ 10⁵` and `0 ≤ height[i] ≤ 10⁴`.

## From brute force to optimal

The lesson builds two solutions in order. The earlier one is folded below; the optimal one, my solution, is the rest of this page.

<details>
<summary><b>1. Brute force</b> · O(n²) time · O(1) space</summary>

Measure every pair of lines: width × the shorter line.

```java
class Solution {
    public int maxArea(int[] height) {
        int maxArea = 0;
        for (int left = 0; left < height.length; left++) {
            for (int right = left + 1; right < height.length; right++) {
                int waterHeight = Math.min(height[left], height[right]);
                maxArea = Math.max(maxArea, (right - left) * waterHeight);
            }
        }
        return maxArea;
    }
}
```

</details>

**2. Optimal: two pointers** · O(n) time · O(1) space, explained below.

## Optimal approach: the move, repeated while `left < right`

1. **Measure the water between them:** `width = right - left`, `waterHeight = Math.min(height[left], height[right])`.
2. **Keep the best area:** `maxArea = Math.max(maxArea, width * waterHeight)`.
3. **Move the shorter line inward:** `if (height[left] < height[right]) left++; else right--;`.

## Why it works

The shorter line caps the water. Say `height[left]` is the shorter one. Pairing it with any line closer than `right` gives a **narrower** container, and its water still can't rise above `height[left]`. So none of those pairs can beat the area just measured, and it's safe to drop `left` for good. In the lesson's example, step 1 measures `8 × 1 = 8`. Line 0 with any closer right holds at most `7 × 1 = 7`. Each step drops one line this way, so `n − 1` steps cover everything that could be the answer.

## Watch out

- **`waterHeight` is the shorter line**, so use `Math.min`. Water spills over the lower side.
- **Never move the taller line.** The width shrinks and the shorter line still caps the water, so the area can only go down.
- **Ties:** when `height[left] == height[right]`, moving either side is fine. Neither line can do better with any partner further in. The lesson's example hits a tie at step 4 (`8 == 8`), and the `else` sends it to `right--`.

## Complexity

| Approach | Time | Space |
|---|---|---|
| Brute force | O(n²) | O(1) |
| **Optimal: two pointers** (my solution) | **O(n)**: each step moves one pointer inward, so at most `n − 1` steps | **O(1)**: `left`, `right` and `maxArea` |

## Recognize it next time

- "Pick two positions to maximise something that depends on their **distance** and the **smaller** of their two values": two pointers from both ends, moving the limiting side.
- The brute force checks every pair, which is O(n²): 36 pairs for the lesson's 9 lines. Two pointers find the answer in 8 steps.
- Not the same as **Trapping Rain Water**, which adds up the water above every bar. That one uses a running max from each side.

## My solution (optimal) · [`ContainerWithMostWater.java`](ContainerWithMostWater.java)

```java
class Solution {
    public int maxArea(int[] height) {
        int maxArea = 0;
        int left = 0;
        int right = height.length - 1;

        while (left < right) {
            int width = right - left;
            int waterHeight = Math.min(height[left], height[right]);
            maxArea = Math.max(maxArea, width * waterHeight);

            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxArea;
    }
}
```
