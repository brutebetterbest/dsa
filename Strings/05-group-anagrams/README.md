# 05 · Group Anagrams

`Strings` · `Medium` · [LeetCode #49](https://leetcode.com/problems/group-anagrams/) · **Pattern: Hash map with a letter-count key**

> **One-liner:** anagrams have the same letter counts. For each word, count its letters into `freq[26]`, turn the counts into a `key` with `Arrays.toString(freq)`, and add the word to that key's list in `groups`. The map's lists are the answer.

## Problem

Given an array of strings `strs`, group the anagrams together (words made of the same letters, the same number of times) and return the groups in any order. `1 ≤ strs.length ≤ 10⁴`, `0 ≤ strs[i].length ≤ 100`, lowercase English letters only.

## From brute force to optimal

The lesson builds three solutions in order. The earlier ones are folded below; the optimal one, my solution, is the rest of this page. Below, `n` is the number of words and `k` is the length of the longest one.

<details>
<summary><b>1. Brute force: compare each word with every group</b> · O(n²·k) time · O(1) extra space</summary>

Keep a list of groups. For each word, check it against the first word of every group with the Valid Anagram check (count up for one, down for the other); join the first group it matches, or start a new group.

```java
class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        List<List<String>> groups = new ArrayList<>();
        for (String str : strs) {
            boolean placed = false;
            for (List<String> group : groups) {
                if (isAnagram(group.get(0), str)) {
                    group.add(str);
                    placed = true;
                    break;
                }
            }
            if (!placed) {
                List<String> newGroup = new ArrayList<>();
                newGroup.add(str);
                groups.add(newGroup);
            }
        }
        return groups;
    }

    private boolean isAnagram(String first, String second) {
        if (first.length() != second.length()) {
            return false;
        }
        int[] freq = new int[26];
        for (int i = 0; i < first.length(); i++) {
            freq[first.charAt(i) - 'a']++;
            freq[second.charAt(i) - 'a']--;
        }
        for (int count : freq) {
            if (count != 0) {
                return false;
            }
        }
        return true;
    }
}
```

</details>

<details>
<summary><b>2. Better: the sorted letters as the key</b> · O(n·k log k) time · O(n·k) space</summary>

Brute force compares a word with every group, one by one. Instead, give each word a key that all its anagrams share: its letters sorted (`eat`, `tea` and `ate` all sort to `aet`). A hash map from key to group finds the right group in one lookup.

```java
class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> groups = new HashMap<>();
        for (String str : strs) {
            char[] letters = str.toCharArray();
            Arrays.sort(letters);
            String key = new String(letters);
            if (!groups.containsKey(key)) {
                groups.put(key, new ArrayList<>());
            }
            groups.get(key).add(str);
        }
        return new ArrayList<>(groups.values());
    }
}
```

</details>

**3. Optimal: the letter counts as the key** · O(n·k) time · O(n·k) space, explained below.

## Optimal approach: a key made of letter counts

Sorting a word just to build its key costs O(k log k). But two words are anagrams exactly when every letter appears the same number of times, the same 26 counts as in [Valid Anagram](../01-valid-anagram/). Counting takes one pass, O(k).

1. **Count:** for each word, `freq[letter - 'a']++` for every letter.
2. **Build the key:** `Arrays.toString(freq)` turns the 26 counts into a string. `"eat"`, `"tea"` and `"ate"` all give `"[1, 0, 0, 0, 1, 0, …, 1, …]"` (one `a`, one `e`, one `t`).
3. **Group:** if `groups` has no list for this key yet, put an empty one; then add the word to the key's list.
4. **Return** `new ArrayList<>(groups.values())`.

On `["eat", "tea", "tan", "ate", "nat", "bat"]`: `"eat"` starts the group for one `a`, one `e`, one `t`. `"tea"` has the same counts and joins it. `"tan"` (one `a`, one `n`, one `t`) starts a new group. `"ate"` joins the first, `"nat"` joins `"tan"`, and `"bat"` starts a third. The answer is `[["eat", "tea", "ate"], ["tan", "nat"], ["bat"]]`, in any order.

## Why it works

Two words are anagrams exactly when their 26 letter counts are equal, so equal keys mean the same group and different keys mean different groups. The hash map finds a word's group in one lookup instead of comparing it with every group.

## Watch out

- **The key must be a `String` (or another value type), not the `int[]` itself.** Arrays use identity for `equals` and `hashCode`, so two `int[]` with the same counts are different keys, and every word would get its own group.
- **Separate the counts in the key.** `Arrays.toString` writes `"[1, 11, …]"`. Gluing the digits together with no separator could make different counts look alike (`1` then `11` vs `11` then `1` both read `111`).
- **The empty string is a word too.** `[""]` returns `[[""]]`: all 26 counts are `0`, and that's a valid key.
- **Don't start a group with `List.of(str)`.** It's immutable, so the next `add` throws `UnsupportedOperationException`; start with `new ArrayList<>()`.

## Complexity

| Approach | Time | Space |
|---|---|---|
| Brute force: compare each word with every group | O(n²·k) | O(1) extra, besides the answer |
| Better: sorted letters as the key | O(n·k log k) | O(n·k): the keys and the groups |
| **Optimal: letter counts as the key** (my solution) | **O(n·k)**: one pass over each word, plus 26 to build its key | **O(n·k)**: the keys and the groups |

## Recognize it next time

- "Group" or "bucket" things that are equal in some sense (anagrams, same letters, same shape): give each one a **key** that all equal things share, and let a hash map collect them.
- For anagrams, the key is the letter counts (or the sorted letters). [Valid Anagram](../01-valid-anagram/) compares two words with the same counts.

## My solution (optimal) · [`GroupAnagrams.java`](GroupAnagrams.java)

```java
class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> groups = new HashMap<>();

        for (String str : strs) {
            int[] freq = new int[26];
            for (char letter : str.toCharArray()) {
                freq[letter - 'a']++;
            }
            String key = Arrays.toString(freq);
            if (!groups.containsKey(key)) {
                groups.put(key, new ArrayList<>());
            }
            groups.get(key).add(str);
        }
        return new ArrayList<>(groups.values());
    }
}
```
