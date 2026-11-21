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
