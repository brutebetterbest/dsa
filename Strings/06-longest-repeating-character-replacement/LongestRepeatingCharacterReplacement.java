class Solution {
    public int characterReplacement(String s, int k) {
        int[] freq = new int[26];
        int left = 0;
        int maxFreq = 0;    // most copies of one letter in any window so far (never lowered)
        int maxLength = 0;

        for (int right = 0; right < s.length(); right++) {
            char currentChar = s.charAt(right);
            freq[currentChar - 'A']++;
            maxFreq = Math.max(maxFreq, freq[currentChar - 'A']);

            int windowLength = right - left + 1;
            if (windowLength - maxFreq > k) {
                freq[s.charAt(left) - 'A']--;
                left++;
            } else {
                maxLength = Math.max(maxLength, windowLength);
            }
        }
        return maxLength;
    }
}
