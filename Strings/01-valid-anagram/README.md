# 01 · Valid Anagram

`Strings` · `Easy` · [LeetCode #242](https://leetcode.com/problems/valid-anagram/) · **Pattern: Frequency count array**

> **One-liner:** count every letter **up** for `s` and **down** for `t` in one `int[26]`. If every count ends at `0`, the two strings use the same letters the same number of times.

## Problem

Given two strings `s` and `t`, return `true` if `t` is an anagram of `s`: the same letters, the same number of times, in any order. Both contain only lowercase English letters, and their lengths are between 1 and 5·10⁴. The follow-up asks what changes if the inputs can contain Unicode characters.

## From brute force to optimal

The lesson builds three solutions in order. The earlier ones are folded below; the optimal one, my solution, is the rest of this page.

<details>
<summary><b>1. Brute force: cross off letters</b> · O(n²) time · O(n) space</summary>

For each letter of `s`, scan `t` for a matching letter that isn't used yet and cross it off. If one is missing, it's not an anagram.

```java
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;

        boolean[] used = new boolean[t.length()];
        for (char letter : s.toCharArray()) {
            boolean found = false;
            for (int j = 0; j < t.length(); j++) {
                if (!used[j] && t.charAt(j) == letter) {
                    used[j] = true;
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }
}
```

</details>

<details>
<summary><b>2. Better: sort both and compare</b> · O(n log n) time · O(n) space</summary>

Every letter rescans `t`. Sorting both strings lines the letters up instead: anagrams become the same string, and `"anagram"` and `"nagaram"` both sort to `"aaagmnr"`.

```java
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;

        char[] sortedS = s.toCharArray(), sortedT = t.toCharArray();
        Arrays.sort(sortedS);
        Arrays.sort(sortedT);
        return Arrays.equals(sortedS, sortedT);
    }
}
```

</details>

**3. Optimal: count, don't sort** · O(n) time · O(1) space, explained below.

## Optimal approach: count up for `s`, down for `t`

Sorting puts every letter in order, but the order never mattered. Only how many of each letter does.

1. **Different lengths can't be anagrams:** return `false` straight away.
2. **Count `s` up:** `letter - 'a'` turns `a…z` into the slots `0…25`, and each letter of `s` adds one to its slot in `freq`. For `"anagram"`: `a` 3, `n` 1, `g` 1, `r` 1, `m` 1.
3. **Count `t` down:** each letter of `t` takes one away. `"nagaram"` brings every slot back to `0`.
4. **Check every count:** any slot that isn't `0` means a letter that one string has more of. `"rat"` and `"car"` end with `t` at `+1` and `c` at `−1`, so the answer is `false`.

## Why it works

Two strings are anagrams exactly when every letter appears the same number of times in both. After the two passes, `freq[x]` is (how many `x` in `s`) − (how many `x` in `t`), so all zeros means every letter's counts are equal.

## Watch out

- **Count, don't just compare the letters.** `"aab"` and `"abb"` use the same set of letters, but `a` ends at `+1` and `b` at `−1`. A `Set` would wrongly say `true`.
- **The length check is an early exit.** The counts would catch a mismatch anyway (`"a"` vs `"ab"` leaves `b` at `−1`), but it skips both loops and states the first rule of an anagram.
- **`letter - 'a'` only works for `a…z`.** That's the problem's guarantee. An uppercase or accented letter would land outside `0…25`, which is the follow-up below.

## Complexity

| Approach | Time | Space |
|---|---|---|
| Brute force: cross off letters | O(n²) | O(n) |
| Better: sort both and compare | O(n log n) | O(n) |
| **Optimal: count, don't sort** (my solution) | **O(n)**: one pass over each string, then 26 checks | **O(1)**: 26 counts, whatever the length |

## Recognize it next time

- "Same letters", "rearrange", "permutation of": compare letter counts, not orders.
- The same count shows up again in Ransom Note (one-sided: no count may go below `0`), Group Anagrams (the counts become a key) and Find All Anagrams in a String (the counts slide along a window).

## My solution (optimal) · [`ValidAnagram.java`](ValidAnagram.java)

```java
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;

        int[] freq = new int[26];
        for (char letter : s.toCharArray()) {
            freq[letter - 'a']++;
        }
        for (char letter : t.toCharArray()) {
            freq[letter - 'a']--;
        }

        for (int count : freq) {
            if (count != 0) return false;
        }
        return true;
    }
}
```

## Follow-up: Unicode characters · [`ValidAnagramUnicode.java`](ValidAnagramUnicode.java)

Unicode letters don't fit in 26 slots, so the array becomes a `Map<Character, Integer>`, with the same three loops. `getOrDefault(letter, 0)` starts a new letter at `0`, and a letter that only `t` has drops to `−1`. It's O(n) time and O(k) space for `k` distinct characters. A `char` is one UTF-16 unit, so for emoji (two `char`s each) you'd count `s.codePoints()` into a `Map<Integer, Integer>` instead.

```java
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;

        Map<Character, Integer> freq = new HashMap<>();
        for (char letter : s.toCharArray()) {
            freq.put(letter, freq.getOrDefault(letter, 0) + 1);
        }
        for (char letter : t.toCharArray()) {
            freq.put(letter, freq.getOrDefault(letter, 0) - 1);
        }

        for (int count : freq.values()) {
            if (count != 0) return false;
        }
        return true;
    }
}
```
