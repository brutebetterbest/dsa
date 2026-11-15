# 02 · Valid Palindrome

`Strings` · `Easy` · [LeetCode #125](https://leetcode.com/problems/valid-palindrome/) · **Pattern: Two pointers from both ends**

> **One-liner:** walk `left` and `right` toward each other on the original string, skip anything that isn't a letter or digit, and compare the two characters lowercased. A mismatch means `false`; meeting in the middle means `true`.

## Problem

Given a string `s`, return `true` if it reads the same forward and backward after turning uppercase letters into lowercase and removing every character that isn't a letter or digit. `s` has 1 to 2·10⁵ printable ASCII characters. A string with nothing left after cleaning, like `" "`, is a palindrome.

## From brute force to optimal

The lesson builds three solutions in order. All three are O(n) time; each one needs less extra space than the last. The earlier ones are folded below; the optimal one, my solution, is the rest of this page.

<details>
<summary><b>1. Brute force: clean, reverse, compare</b> · O(n) time · O(n) space</summary>

Build a cleaned lowercase copy with only letters and digits, reverse it, and compare the two. `"Taco cat!"` cleans to `"tacocat"`, which reads the same reversed.

```java
class Solution {
    public boolean isPalindrome(String s) {
        StringBuilder cleaned = new StringBuilder();
        for (char ch : s.toCharArray()) {
            if (Character.isLetterOrDigit(ch)) {
                cleaned.append(Character.toLowerCase(ch));
            }
        }
        String forward = cleaned.toString();
        String backward = cleaned.reverse().toString();
        return forward.equals(backward);
    }
}
```

</details>

<details>
<summary><b>2. Better: clean once, two pointers</b> · O(n) time · O(n) space</summary>

The reversed copy is extra, and so is comparing every character when the first pair might already differ. Keep one cleaned copy and compare it from both ends inward, stopping at the first mismatch.

```java
class Solution {
    public boolean isPalindrome(String s) {
        StringBuilder cleaned = new StringBuilder();
        for (char ch : s.toCharArray()) {
            if (Character.isLetterOrDigit(ch)) {
                cleaned.append(Character.toLowerCase(ch));
            }
        }

        int left = 0, right = cleaned.length() - 1;
        while (left < right) {
            if (cleaned.charAt(left) != cleaned.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
}
```

</details>

**3. Optimal: two pointers that skip in place** · O(n) time · O(1) space, explained below.

## Optimal approach: skip, compare, move inward

The cleaned copy only exists so the pointers never land on a space or a symbol. The pointers can skip those themselves.

1. **Start at both ends:** `left = 0`, `right = s.length() - 1`.
2. **Skip:** move `left` forward and `right` backward past anything that isn't a letter or digit.
3. **Compare lowercased:** different → `false`. Equal → move both one step inward.
4. **Meet in the middle:** when `left` reaches `right`, every pair matched → `true`.

On `"Taco cat!"`: `right` skips the `!`, `'T'` matches `'t'`, `'a'` matches `'a'`, `'c'` matches `'c'`, then `right` skips the space and the pointers meet on the middle `'o'`. On `"race a car"`, `r`, `a` and `c` match, then `'e'` meets `'a'`: `false`.

## Why it works

A palindrome's first character equals its last, its second equals its second-to-last, and so on. Skipping the non-alphanumeric characters on both sides lines up exactly the same pairs that the cleaned copy would, so each pair is compared once, without building anything.

## Watch out

- **Keep `left < right` inside both skip loops.** On `".,"`, the left skip would walk past the end of the string and `charAt` would throw. With the guard, the pointers stop when they meet.
- **Digits count.** Use `isLetterOrDigit`, not `isLetter`: `"0P"` is `false`, but skipping the `0` would wrongly say `true`.
- **Nothing left is still a palindrome.** `" "` has no letters or digits, so nothing is compared and the answer is `true`.
- **Lowercase both sides when comparing**, so `'T'` matches `'t'`.

## Complexity

| Approach | Time | Space |
|---|---|---|
| Brute force: clean, reverse, compare | O(n) | O(n): two extra strings |
| Better: clean once, two pointers | O(n) | O(n): one extra string |
| **Optimal: two pointers that skip in place** (my solution) | **O(n)**: each character is visited at most once | **O(1)**: two indexes, no copy |

## Recognize it next time

- "Reads the same forward and backward", or comparing a string's ends: two pointers moving inward.
- When the input has characters to ignore, skip them with the pointers instead of building a cleaned copy. Valid Palindrome II (delete at most one character) reuses the same pointers.

## My solution (optimal) · [`ValidPalindrome.java`](ValidPalindrome.java)

```java
class Solution {
    public boolean isPalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;

        while (left < right) {
            // skip anything that isn't a letter or digit
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) left++;
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) right--;

            if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
}
```
