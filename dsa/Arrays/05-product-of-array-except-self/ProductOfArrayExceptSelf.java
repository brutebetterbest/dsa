class Solution {
    public int[] productExceptSelf(int[] nums) {
        int[] answer = new int[nums.length];

        // answer[i] = product of all numbers left of i
        answer[0] = 1;
        for (int i = 1; i < nums.length; i++) {
            answer[i] = answer[i - 1] * nums[i - 1];
        }

        int suffixProduct = 1;                          // product of all numbers right of i
        for (int i = nums.length - 1; i >= 0; i--) {
            answer[i] = answer[i] * suffixProduct;      // left product * right product
            suffixProduct *= nums[i];                   // updating right product
        }
        return answer;
    }
}
