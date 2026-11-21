# 03 · Longest Common Prefix

`Strings` · `Easy` · [LeetCode #14](https://leetcode.com/problems/longest-common-prefix/) · **Pattern: Shrinking prefix length**

> **One-liner:** the answer is a prefix of `strs[0]`. Keep `prefixLength`, how much of it every string so far shares. For each next string, count the `matched` leading characters (never past `prefixLength`), set `prefixLength = matched`, and return `""` as soon as it reaches `0`.

## Problem

Given an array of strings `strs`, return the longest prefix that all of them share, or `""` if there is none. `1 ≤ strs.length ≤ 200`, `0 ≤ strs[i].length ≤ 200`, and the strings hold only lowercase English letters.

## From brute force to optimal

The lesson builds three solutions in order. The earlier ones are folded below; the optimal one, my solution, is the rest of this page. Below, `n` is the number of strings and `m` is the length of `strs[0]`.

<details>
<summary><b>1. Brute force: try every prefix of the first string, longest first</b> · O(n·m²) time · O(m) space</summary>

The answer is a prefix of `strs[0]`. Try its prefixes from the longest down, and check that every string `startsWith` the candidate; the first one they all share is the answer.

```java
class Solution {
    public String longestCommonPrefix(String[] strs) {
        String first = strs[0];
        for (int length = first.length(); length > 0; length--) {
            String candidate = first.substring(0, length);
            boolean allMatch = true;
            for (String s : strs) {
                if (!s.startsWith(candidate)) {
                    allMatch = false;
                    break;
                }
            }
            if (allMatch) {
                return candidate;
            }
        }
        return "";
    }
}
```

</details>

<details>
<summary><b>2. Better: one prefix that only shrinks (<code>StringBuilder</code> + <code>startsWith</code>)</b> · O(n·m + m²) time · O(m) space</summary>

Brute force rechecks every string for every candidate. But a string that starts with `prefix` also starts with every shorter piece of it, so keep one `prefix`, walk the strings once, and delete its last character until the current string starts with it. I solved it this way too.

```java
class Solution {
    public String longestCommonPrefix(String[] strs) {
        StringBuilder prefix = new StringBuilder(strs[0]);
        for (int i = 1; i < strs.length; i++) {
            while (!strs[i].startsWith(prefix.toString())) {
                prefix.deleteCharAt(prefix.length() - 1);
            }
            if (prefix.length() == 0) {
                break;
            }
        }
        return prefix.toString();
    }
}
```

</details>

**3. Optimal: count the matching characters** · O(n·m) time · O(1) space, explained below.

## Optimal approach: count, don't copy

The better approach deletes one character, then checks the whole prefix again from the start. Instead, walk the string once and count how far it agrees with the prefix: that count is the new prefix length.

1. **Start with all of the first string:** `prefixLength = first.length()`.
2. **For each next string, count `matched`:** step forward while both strings still have characters, `matched` is below `prefixLength`, and the characters are equal.
3. **`prefixLength = matched`.** The prefix only ever shrinks.
4. **Zero means no common prefix:** return `""` right away. After the last string, return `first.substring(0, prefixLength)`.

On `["flower", "flow", "flight"]`: `prefixLength` starts at `6`. `"flow"` matches `f`, `l`, `o`, `w` and then runs out, so `prefixLength = 4`. `"flight"` matches `f`, `l`, then `o` vs `i` differ, so `prefixLength = 2`. The answer is `"fl"`.

## Why it works

A string that shares the first `k` characters with `first` also shares every shorter prefix, so shrinking the prefix never breaks a string that was already checked. After each string, `prefixLength` is exactly the longest prefix of all the strings so far. Each count stops at `prefixLength`, so a string costs at most `m` comparisons, and nothing is copied until the one `substring` at the end.

## Watch out

- **Check the length before `charAt`.** On `["ab", "a"]` the second string runs out after one character; `matched < strs[i].length()` stops the loop before `charAt(1)` would throw.
- **Never count past `prefixLength`.** On `["ab", "a", "ab"]`, the third string matches 2 characters of `first`, but only 1 is shared by all three. The bound keeps the count at 1; without it, `prefixLength` would grow back to 2 and return `"ab"`.
- **One string returns itself.** The loop never runs, and `substring(0, first.length())` is the whole string.
- **An empty string anywhere gives `""`.** `["", "a"]` starts at `prefixLength = 0`; `["a", ""]` matches nothing.
- **Stop as soon as nothing is shared.** `["dog", "racecar", "car"]` returns `""` after `"racecar"`, without reading `"car"`.

## Complexity

| Approach | Time | Space |
|---|---|---|
| Brute force: try every prefix, longest first | O(n·m²) | O(m): the candidate substring |
| Better: one shrinking `StringBuilder` + `startsWith` | O(n·m + m²) | O(m): the builder, and a new `String` for every check |
| **Optimal: count the matching characters** (my solution) | **O(n·m)**: each character of each string is compared at most once | **O(1)**: two counters, plus the result |

## Recognize it next time

- "Common prefix" of several strings: the answer is a prefix of any one of them, and it only shrinks as strings are added. Keep its length, not a copy.
- Column by column (character 0 of every string, then character 1, …) is the other O(n·m) way; it stops at the first column where any string differs.
- Sorting, then comparing only the first and last strings, also works, in O(n·m·log n).
- Many prefix questions about the same set of strings: build a trie.

## My solution (optimal) · [`LongestCommonPrefix.java`](LongestCommonPrefix.java)

```java
class Solution {
    public String longestCommonPrefix(String[] strs) {
        String first = strs[0];
        int prefixLength = first.length();

        for (int i = 1; i < strs.length; i++) {
            // count how many leading characters this string shares with the prefix so far
            int matched = 0;
            while (matched < prefixLength
                    && matched < strs[i].length()
                    && first.charAt(matched) == strs[i].charAt(matched)) {
                matched++;
            }
            prefixLength = matched;
            if (prefixLength == 0) {
                return "";
            }
        }
        return first.substring(0, prefixLength);
    }
}
```
