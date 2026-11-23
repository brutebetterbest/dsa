# Strings

Each problem folder has the approach, edge cases and my Java solution, with its lesson on YouTube.

| # | Problem | Difficulty | Pattern | One-liner |
|---|---|---|---|---|
| 01 | [Valid Anagram](01-valid-anagram/) | Easy | Frequency count array | `freq[letter - 'a']++` for `s`, `--` for `t`; every count back at `0` → anagram |
| 02 | [Valid Palindrome](02-valid-palindrome/) | Easy | Two pointers from both ends | Skip non-alphanumerics with `left`/`right`, compare lowercased; mismatch → `false`, pointers meet → `true` |
| 03 | [Longest Common Prefix](03-longest-common-prefix/) | Easy | Shrinking prefix length | `prefixLength` starts at `strs[0].length()`; for each string count `matched` leading characters (up to `prefixLength`), set `prefixLength = matched`; `0` → `""` |
| 04 | [Longest Substring Without Repeating Characters](04-longest-substring-without-repeating-characters/) | Medium | Sliding window + last-seen position | Grow with `right`; `left = max(left, lastSeen[c])` jumps past a repeat inside the window (never back); `maxLength = max(…, right − left + 1)`; then `lastSeen[c] = right + 1` |
| 05 | [Group Anagrams](05-group-anagrams/) | Medium | Hash map with a letter-count key | Count each word's letters into `freq[26]`, `key = Arrays.toString(freq)`; add the word to `groups`' list for that key; return the lists |
