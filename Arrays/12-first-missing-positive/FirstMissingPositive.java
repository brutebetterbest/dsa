class Solution {
    public int firstMissingPositive(int[] nums) {
        int n = nums.length;

        // put every value 1..n at its home index (value - 1)
        for (int i = 0; i < n; i++) {
            while (nums[i] > 0 && nums[i] <= n && nums[nums[i] - 1] != nums[i]) {
                int correctIndex = nums[i] - 1;
                int temp = nums[correctIndex];
                nums[correctIndex] = nums[i];
                nums[i] = temp;
            }
        }

        // the first index not holding index + 1 is the missing number
        for (int i = 0; i < n; i++) {
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }
        return n + 1;
    }
}
