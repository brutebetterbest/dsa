import java.util.HashMap;
import java.util.Map;

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
