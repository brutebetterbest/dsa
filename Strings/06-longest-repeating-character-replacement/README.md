# 06 · Longest Repeating Character Replacement

`Strings` · `Medium` · [LeetCode #424](https://leetcode.com/problems/longest-repeating-character-replacement/) · **Pattern: Sliding window + most frequent letter**

> **One-liner:** grow the window with `right` and count its letters in `freq`. The letters to change are everything except the most common one, `windowLength − maxFreq`. If that's more than `k`, slide the window one step (`left++`) instead of shrinking it; otherwise the window works and is a new `maxLength`.

## Problem

Given a string `s` of uppercase English letters and an integer `k`, you can change any letter into any other uppercase letter, at most `k` times. Return the length of the longest substring of one repeated letter you can get. `1 ≤ s.length ≤ 10⁵` and `0 ≤ k ≤ s.length`.

## From brute force to optimal

The lesson builds two solutions in order. The earlier one is folded below; the optimal one, my solution, is the rest of this page.

<details>
<summary><b>1. Brute force: check every substring</b> · O(n²) time · O(1) space</summary>

Fix `left`, grow `right` and count the letters as they come in. A substring works when the letters other than its most common one, `windowLength − maxFreq`, are at most `k`. Keep the longest one that works.

```java
class Solution {
    public int characterReplacement(String s, int k) {
        int maxLength = 0;
        for (int left = 0; left < s.length(); left++) {
            int[] freq = new int[26];
            int maxFreq = 0;
            for (int right = left; right < s.length(); right++) {
                char currentChar = s.charAt(right);
                freq[currentChar - 'A']++;
                maxFreq = Math.max(maxFreq, freq[currentChar - 'A']);

                int windowLength = right - left + 1;
                if (windowLength - maxFreq <= k) {
                    maxLength = Math.max(maxLength, windowLength);
                }
            }
        }
        return maxLength;
    }
}
```

</details>

**2. Optimal: a sliding window that never shrinks** · O(n) time · O(1) space, explained below.

## Optimal approach: keep one window, and slide it when it needs too many changes

Brute force starts again from every `left` and counts the same letters over and over. Instead, keep one window and its counts. Grow it on the right, and when it needs more than `k` changes, move its left edge one step. We only want a window longer than the best one so far, so a window that's too long never has to shrink below that length: it just slides.

1. **Grow:** `right` takes `currentChar` and counts it in `freq`. `maxFreq` is the highest count of one letter seen in a window so far.
2. **Count the changes:** every letter in the window except the most common one, `windowLength − maxFreq`.
3. **Too many?** If that's more than `k`, take `s[left]` out of `freq` and `left++`. The window slides one step and keeps its length.
4. **Otherwise** the window works: `maxLength = max(maxLength, windowLength)`.

On `"AABABBA"` with `k = 1`, the window grows to `"AABA"`: one `B` to change, length 4. The next `B` makes `"AABAB"`, which needs two changes, so the window slides to `"ABAB"`. Every later window of length 5 also needs two changes, so it keeps sliding, and the answer is `4` (`"AABA"` → `"AAAA"`).

## Why it works

The window never shrinks: each step it either grows by one or slides, so its length is always the best found so far, `maxLength`. `maxFreq` is never lowered, so after a slide it can be higher than the real top count in the window. That can't give a wrong answer: once the window has had to slide, its length is `maxFreq + k`, so it can only grow again when some letter's count passes `maxFreq`, and that count is real, in the current window.

## Watch out

- **Don't lower `maxFreq` when `left` moves.** Finding the real top count again means looking through all 26 counts on every step. An out-of-date `maxFreq` only lets a too-long window slide; the window grows only when a letter really beats it.
- **Slide with `if`, not `while`.** Shrinking until the window works is also correct, but the window never needs to get shorter than the best one so far.
- **`k = 0`:** nothing can change, so the answer is the longest run of one letter: `"AABBB"` → `3`.
- **`k` at least the length of `s`:** every letter can change, so the answer is `s.length()`, and the window never slides.
- **Uppercase letters only:** `freq[currentChar - 'A']` fits `int[26]`. For any ASCII character, use `int[128]` and `freq[currentChar]`.

## Complexity

| Approach | Time | Space |
|---|---|---|
| Brute force: check every substring | O(n²) | O(1): one `int[26]` at a time |
| **Optimal: sliding window + `maxFreq`** (my solution) | **O(n)**: `right` visits each letter once, and `left` moves at most one step each time | **O(1)**: `int[26]`, whatever the length of `s` |

## Recognize it next time

- "Longest substring or subarray where you may change, flip or remove at most `k` things": a sliding window that's fine while the things to fix are at most `k`. Max Consecutive Ones III is the same idea with 0s and 1s.
- Here the cost of fixing a window is its length minus the count of its most common letter.
- When only the longest answer matters, a window that breaks can slide instead of shrinking.

## My solution (optimal) · [`LongestRepeatingCharacterReplacement.java`](LongestRepeatingCharacterReplacement.java)

```java
class Solution {
    public int characterReplacement(String s, int k) {
        int[] freq = new int[26];
        int left = 0;
        int maxFreq = 0;    // most copies of one letter in any window so far (never lowered)
        int maxLength = 0;

        for (int right = 0; right < s.length(); right++) {
            char currentChar = s.charAt(right);
            freq[currentChar - 'A']++;
            maxFreq = Math.max(maxFreq, freq[currentChar - 'A']);

            int windowLength = right - left + 1;
            if (windowLength - maxFreq > k) {
                freq[s.charAt(left) - 'A']--;
                left++;
            } else {
                maxLength = Math.max(maxLength, windowLength);
            }
        }
        return maxLength;
    }
}
```
