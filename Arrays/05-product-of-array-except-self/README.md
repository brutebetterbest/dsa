# 05 · Product of Array Except Self

`Arrays` · `Medium` · [LeetCode #238](https://leetcode.com/problems/product-of-array-except-self/) · **Pattern: Prefix × suffix product**

> **One-liner:** `answer[i]` = product of everything **left** of `i` × product of everything **right** of `i`. Pass 1 (→) stores the left products in `answer`, and pass 2 (←) multiplies in a running `suffixProduct`.

## Problem

Given `nums`, return `answer` where `answer[i]` is the product of every element except `nums[i]`. It must run in O(n) time **without using division**. Every prefix and suffix product fits in an `int`. Follow-up: O(1) extra space (the output array doesn't count).

## From brute force to optimal

The lesson builds three solutions in order. The earlier ones are folded below; the optimal one, my solution, is the rest of this page.

<details>
<summary><b>1. Brute force</b> · O(n²) time · O(1) extra space</summary>

For each index `i`, multiply every number except `nums[i]`.

```java
class Solution {
    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] answer = new int[n];
        for (int i = 0; i < n; i++) {
            int product = 1;
            for (int j = 0; j < n; j++) {
                if (j != i) {
                    product *= nums[j];
                }
            }
            answer[i] = product;
        }
        return answer;
    }
}
```

</details>

<details>
<summary><b>2. Better: prefix + suffix arrays</b> · O(n) time · O(n) space</summary>

Everything except `nums[i]` is its left part times its right part. `prefix[i]` holds the product of everything left of `i`, and `suffix[i]` the product of everything right of it.

```java
class Solution {
    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] prefix = new int[n], suffix = new int[n], answer = new int[n];

        prefix[0] = 1;                            // nothing is left of index 0
        for (int i = 1; i < n; i++) {
            prefix[i] = prefix[i - 1] * nums[i - 1];
        }
        suffix[n - 1] = 1;                        // nothing is right of the last index
        for (int i = n - 2; i >= 0; i--) {
            suffix[i] = suffix[i + 1] * nums[i + 1];
        }

        for (int i = 0; i < n; i++) {
            answer[i] = prefix[i] * suffix[i];
        }
        return answer;
    }
}
```

</details>

**3. Optimal: left products in `answer` + one `suffixProduct`** · O(n) time · O(1) extra space, explained below.

## Optimal approach: two passes over `nums`

1. **Pass 1, left → right: store the left product.** `answer[0] = 1`, then `answer[i] = answer[i - 1] * nums[i - 1]`. Now `answer[i]` is the product of `nums[0..i−1]`.
2. **Pass 2, right → left: multiply by the right product.** `answer[i] = answer[i] * suffixProduct`, where `suffixProduct` is the product of `nums[i+1..]`. Now `answer[i]` is final.
3. **Then grow the right product:** `suffixProduct *= nums[i]`, ready for `i − 1`.

## Why it works

Everything except `nums[i]` splits into two parts: the numbers left of `i` and the numbers right of `i`. Pass 1 computes every left part, and pass 2 carries the right part in one variable. Their product never includes `nums[i]`, and there's no division, so zeros cause no trouble.

## Watch out

- **`answer[0] = 1`, and `suffixProduct` starts at 1.** Nothing is left of index 0 (or right of the last index), and an empty product is 1, not 0.
- **Multiply first, then update `suffixProduct`.** If you swap the two lines, `nums[i]` lands in its own answer: `answer[3]` becomes `24 × 5 = 120` instead of `24`.
- **Zeros need no special case.** `[3, 0, 4]` → `[0, 12, 0]`: a 0 makes every left or right product that contains it 0, and only the zero's own slot skips it. The division approach needs separate cases for one zero and for two or more zeros.

## Complexity

| Approach | Time | Space |
|---|---|---|
| Brute force | O(n²) | O(1) extra |
| Better: prefix + suffix arrays | O(n) | O(n) |
| **Optimal: left products in `answer` + one `suffixProduct`** (my solution) | **O(n)**: two passes | **O(1) extra**: only `suffixProduct`. `answer` is the output, which doesn't count |

## Recognize it next time

- "For each `i`, combine everything **except** `i`" (product, sum, max) → precompute from the left and from the right, then combine at `i`.
- The same left/right idea shows up in Trapping Rain Water (max height on each side) and Candy (two passes).
- With division allowed it would be total product ÷ `nums[i]`, but zeros break that. That's why the problem forbids it.

## My solution (optimal) · [`ProductOfArrayExceptSelf.java`](ProductOfArrayExceptSelf.java)

```java
class Solution {
    public int[] productExceptSelf(int[] nums) {
        int[] answer = new int[nums.length];

        // answer[i] = product of all numbers left of i
        answer[0] = 1;
        for (int i = 1; i < nums.length; i++) {
            answer[i] = answer[i - 1] * nums[i - 1];
        }

        int suffixProduct = 1;                          // product of all numbers right of i
        for (int i = nums.length - 1; i >= 0; i--) {
            answer[i] = answer[i] * suffixProduct;      // left product * right product
            suffixProduct *= nums[i];                   // updating right product
        }
        return answer;
    }
}
```
