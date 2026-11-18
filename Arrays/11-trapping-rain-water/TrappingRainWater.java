class Solution {
    public int trap(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int water = 0;
        int waterLevel = Math.min(height[left], height[right]);

        while (left < right) {
            // move the pointer on the lower wall
            if (height[left] < height[right]) {
                left++;
                if (height[left] < waterLevel) {
                    water += waterLevel - height[left];
                } else {
                    waterLevel = Math.min(height[left], height[right]);
                }
            } else {
                right--;
                if (height[right] < waterLevel) {
                    water += waterLevel - height[right];
                } else {
                    waterLevel = Math.min(height[left], height[right]);
                }
            }
        }
        return water;
    }
}
