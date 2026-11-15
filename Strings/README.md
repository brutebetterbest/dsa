# Strings

Each problem folder has the approach, edge cases and my Java solution, with its lesson on YouTube.

| # | Problem | Difficulty | Pattern | One-liner |
|---|---|---|---|---|
| 01 | [Valid Anagram](01-valid-anagram/) | Easy | Frequency count array | `freq[letter - 'a']++` for `s`, `--` for `t`; every count back at `0` → anagram |
| 02 | [Valid Palindrome](02-valid-palindrome/) | Easy | Two pointers from both ends | Skip non-alphanumerics with `left`/`right`, compare lowercased; mismatch → `false`, pointers meet → `true` |
