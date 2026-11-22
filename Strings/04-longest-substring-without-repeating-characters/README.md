# 04 · Longest Substring Without Repeating Characters

`Strings` · `Medium` · [LeetCode #3](https://leetcode.com/problems/longest-substring-without-repeating-characters/) · **Pattern: Sliding window + last-seen position**

> **One-liner:** grow the window one character at a time with `right`. If `currentChar` already appears inside the window, jump `left` just past its last copy, using `Math.max` so `left` never moves back. `maxLength` keeps the widest window seen.

## Problem

Given a string `s`, return the length of the longest substring (a run of characters next to each other) with no repeated character. `0 ≤ s.length ≤ 5·10⁴`, and `s` holds English letters, digits, symbols and spaces.

## From brute force to optimal

The lesson builds three solutions in order. The earlier ones are folded below; the optimal one, my solution, is the rest of this page.

<details>
<summary><b>1. Brute force: check every substring</b> · O(n³) time · O(1) space</summary>

Try every substring `s[left..right]`, check it for a repeated character with a fresh `seen` array, and keep the longest one that has none.

```java
class Solution {
    public int lengthOfLongestSubstring(String s) {
        int maxLength = 0;
        for (int left = 0; left < s.length(); left++) {
            for (int right = left; right < s.length(); right++) {
                if (allUnique(s, left, right)) {
                    maxLength = Math.max(maxLength, right - left + 1);
                }
            }
        }
        return maxLength;
    }

    private boolean allUnique(String s, int left, int right) {
        boolean[] seen = new boolean[128];
        for (int i = left; i <= right; i++) {
            char c = s.charAt(i);
            if (seen[c]) {
                return false;
            }
            seen[c] = true;
        }
        return true;
    }
}
```

</details>

<details>
<summary><b>2. Better: grow from each start, stop at the first repeat</b> · O(n²) time · O(1) space</summary>

Brute force re-checks each longer substring from scratch, though it only added one character. Instead, fix `left`, grow `right` while remembering the characters in `seen`, and stop at the first repeat: no longer substring from this `left` can be repeat-free.

```java
class Solution {
    public int lengthOfLongestSubstring(String s) {
        int maxLength = 0;
        for (int left = 0; left < s.length(); left++) {
            boolean[] seen = new boolean[128];
            for (int right = left; right < s.length(); right++) {
                char c = s.charAt(right);
                if (seen[c]) {
                    break;
                }
                seen[c] = true;
                maxLength = Math.max(maxLength, right - left + 1);
            }
        }
        return maxLength;
    }
}
```

</details>

**3. Optimal: a sliding window that jumps past the repeat** · O(n) time · O(1) space, explained below.

## Optimal approach: keep the window, move only its left edge

When the better approach hits a repeat, it throws the whole window away and starts again from the next `left`. But the part after the old copy of the repeated character is still repeat-free. So keep the window and move only its left edge, straight past that old copy.

1. **Grow:** `right` takes the next character, `currentChar`.
2. **Repeat inside the window?** `lastSeen[currentChar]` is the position just after its last copy. If that is past `left`, the copy is inside the window, so `left` jumps there. `Math.max` keeps `left` where it is when the copy is older, already outside the window.
3. **Measure:** the window `left..right` has no repeat, so `maxLength = max(maxLength, right − left + 1)`.
4. **Remember:** `lastSeen[currentChar] = right + 1`.

On `"abccdea"`: `a`, `b`, `c` grow the window to `"abc"` (length 3). The second `c` has a copy inside the window, so `left` jumps past it, to index 3: the window is just `"c"`. Then `d` and `e` grow it to `"cde"`. The last `a` was seen at index 0, which is already outside the window, so `left` stays at 3 and the window becomes `"cdea"`. The answer is `4`.

## Why it works

The window `left..right` never holds a repeat: whenever `currentChar` has a copy inside it, `left` moves just past that copy, and every other character in the window was already unique. Every repeat-free substring ending at `right` starts at `left` or later, so the window is the longest one ending there, and `maxLength` checks it for every `right`. `left` and `right` only move forward, so each character is handled once.

## Watch out

- **`left` must never move back.** On `"abccdea"`, the last `a`'s old copy is at index 0, but `left` is already at 3. Setting `left = lastSeen['a']` would move it back to 1 and return 6 for `"bccdea"`, which repeats `c`. `Math.max(left, lastSeen[currentChar])` keeps it at 3. `"abba"` breaks the same way (3 instead of 2).
- **Read `lastSeen` before you overwrite it.** The jump needs the old position of `currentChar`; store `right + 1` only after measuring.
- **Store the position after the character, not its index.** With the plain index, a character at index 0 would look "not seen" (the array starts at 0), and `"aa"` would return 2. `right + 1` makes `0` mean "not seen yet" without filling the array with `-1`.
- **The empty string returns `0`:** the loop never runs.
- **`int[128]` covers ASCII,** which is all this problem uses. For any Unicode character, use a `Map<Character, Integer>` with the same logic.

## Complexity

| Approach | Time | Space |
|---|---|---|
| Brute force: check every substring | O(n³) | O(1): one `boolean[128]` at a time |
| Better: grow from each start, stop at the first repeat | O(n²) | O(1): `boolean[128]` |
| **Optimal: sliding window + `lastSeen`** (my solution) | **O(n)**: `right` visits each character once, `left` only jumps forward | **O(1)**: `int[128]`, whatever the length of `s` |

## Recognize it next time

- "Longest / shortest substring or subarray with some property" where the property breaks and then holds again as the left edge moves: a sliding window. Grow with `right`, fix the window by moving `left`.
- When the fix is "get past the last copy of something", remember the last position of each value, so `left` can jump instead of stepping one at a time.
- The step-by-step version (`while` the window has `currentChar`, remove `s[left]` and `left++`) is also O(n), just with up to two visits per character.

## My solution (optimal) · [`LongestSubstringWithoutRepeatingCharacters.java`](LongestSubstringWithoutRepeatingCharacters.java)

```java
class Solution {
    public int lengthOfLongestSubstring(String s) {
        // lastSeen[c] = index just after c's last occurrence (0 = not seen yet)
        int[] lastSeen = new int[128];
        int left = 0;
        int maxLength = 0;

        for (int right = 0; right < s.length(); right++) {
            char currentChar = s.charAt(right);
            left = Math.max(left, lastSeen[currentChar]);
            maxLength = Math.max(maxLength, right - left + 1);
            lastSeen[currentChar] = right + 1;
        }
        return maxLength;
    }
}
```
