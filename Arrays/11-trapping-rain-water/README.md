# 11 · Trapping Rain Water

`Arrays` · `Hard` · [LeetCode #42](https://leetcode.com/problems/trapping-rain-water/) · **Pattern: Two pointers + water level**

> **One-liner:** move the pointer on the lower bar one step inward. If the new bar is below `waterLevel`, it holds `waterLevel − height` of water; otherwise it's a new wall, and `waterLevel` becomes the lower of the two bars under the pointers.

## Problem

Given `n` non-negative bar heights, each bar 1 wide, return how much rain water is trapped between them after it rains. `1 ≤ n ≤ 2·10⁴` and `0 ≤ height[i] ≤ 10⁵`.

## From brute force to optimal

The lesson builds three solutions in order. The earlier ones are folded below; the optimal one, my solution, is the rest of this page.

<details>
<summary><b>1. Brute force: scan both sides for every bar</b> · O(n²) time · O(1) space</summary>

The water above a bar rises to the lower of the tallest bar on its left and the tallest bar on its right (each counting the bar itself). For every bar, scan left for `leftMax` and right for `rightMax`, and add `min(leftMax, rightMax) − height[i]`.

```java
class Solution {
    public int trap(int[] height) {
        int water = 0;
        for (int i = 0; i < height.length; i++) {
            int leftMax = 0, rightMax = 0;
            for (int j = 0; j <= i; j++) {
                leftMax = Math.max(leftMax, height[j]);
            }
            for (int j = i; j < height.length; j++) {
                rightMax = Math.max(rightMax, height[j]);
            }
            water += Math.min(leftMax, rightMax) - height[i];
        }
        return water;
    }
}
```

</details>

<details>
<summary><b>2. Better: <code>leftMax</code> and <code>rightMax</code> arrays</b> · O(n) time · O(n) space</summary>

Each bar's scans repeat the previous bar's scans plus one more bar. Keep the running maximums instead: one pass left to right fills `leftMax`, one pass right to left fills `rightMax`, and a third pass adds up the water.

```java
class Solution {
    public int trap(int[] height) {
        int n = height.length;
        int[] leftMax = new int[n], rightMax = new int[n];
        leftMax[0] = height[0];
        for (int i = 1; i < n; i++) {
            leftMax[i] = Math.max(leftMax[i - 1], height[i]);
        }
        rightMax[n - 1] = height[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i + 1], height[i]);
        }

        int water = 0;
        for (int i = 0; i < n; i++) {
            water += Math.min(leftMax[i], rightMax[i]) - height[i];
        }
        return water;
    }
}
```

</details>

**3. Optimal: two pointers and one water level** · O(n) time · O(1) space, explained below.

## Optimal approach: move the lower side, fill it to the water level

Each bar only uses the **smaller** of its two maximums. Once you know the other side has a wall at least as tall, its exact height never matters. So two pointers from both ends replace both arrays, and one number, `waterLevel`, replaces both maximums.

1. **Start at both ends:** `left = 0`, `right = n − 1`, and `waterLevel` is the lower of the two end bars.
2. **Move the pointer on the lower bar** one step inward. The other side already has a wall at least that tall.
3. **Below the level?** The new bar holds `waterLevel − height` of water.
4. **Otherwise it's a new wall:** `waterLevel` becomes the lower of the two bars under the pointers.
5. **Stop when the pointers meet.**

On `[4, 2, 0, 5, 0, 2]`: the level starts at `2`, the lower end. The right side is lower, so it goes first: the `0` at index 4 holds `2`. Then `right` reaches the `5`, a new wall, and the level becomes `min(4, 5) = 4`. Now the left side is lower: the `2` holds `2`, the `0` holds `4`, and the pointers meet on the `5`. Water: `2 + 2 + 4 = 8`.

## Why it works

We only ever move the pointer on the lower bar, so the taller of the two bars under the pointers is always the tallest bar seen so far. That makes `waterLevel` exactly the smaller of `leftMax` and `rightMax`, the same value the better approach reads from its arrays, and it never goes down. The moving side is the side with the lower maximum, so its water is decided by `waterLevel` alone.

## Watch out

- **Move the lower side, not either side.** On `[4, 2, 0, 5, 0, 2]`, the right end's `2` limits the level at the start, so the right side goes first. Moving the left side first with the level at `2` would give the `0` at index 2 only `2` units, but its real water is `4`.
- **A new wall raises the level to the lower of the two walls, not to itself.** When `right` reaches the `5`, the level becomes `4`, not `5`. With `5`, the bar at index 1 would get `3` units instead of `2`.
- **The end bars never hold water.** Each step moves the pointer first, then looks at the new bar, so index `0` and `n − 1` are only ever walls.
- **`int` is enough.** At most 19,998 bars hold water, each at most 10⁵ deep: 1,999,800,000 is just under `Integer.MAX_VALUE` (2,147,483,647).

## Complexity

| Approach | Time | Space |
|---|---|---|
| Brute force: scan both sides for every bar | O(n²) | O(1) |
| Better: `leftMax` and `rightMax` arrays | O(n) | O(n): two arrays |
| **Optimal: two pointers and one water level** (my solution) | **O(n)**: each bar is visited once | **O(1)**: two pointers, `water` and `waterLevel` |

## Recognize it next time

- Water between bars, "trapped", or "how much fits": two pointers from both ends, moving the lower side. [Container With Most Water](../07-container-with-most-water/) uses the same move.
- A monotonic stack is another O(n) way: it fills the water layer by layer, each time a taller bar closes a dip.

## My solution (optimal) · [`TrappingRainWater.java`](TrappingRainWater.java)

```java
class Solution {
    public int trap(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int water = 0;
        int waterLevel = Math.min(height[left], height[right]);

        while (left < right) {
            // move the pointer on the lower wall
            if (height[left] < height[right]) {
                left++;
                if (height[left] < waterLevel) {
                    water += waterLevel - height[left];
                } else {
                    waterLevel = Math.min(height[left], height[right]);
                }
            } else {
                right--;
                if (height[right] < waterLevel) {
                    water += waterLevel - height[right];
                } else {
                    waterLevel = Math.min(height[left], height[right]);
                }
            }
        }
        return water;
    }
}
```
