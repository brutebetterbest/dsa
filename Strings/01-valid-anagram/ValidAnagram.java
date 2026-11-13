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
