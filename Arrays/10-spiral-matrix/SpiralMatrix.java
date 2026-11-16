import java.util.ArrayList;
import java.util.List;

class Solution {
    public List<Integer> spiralOrder(int[][] matrix) {
        int top = 0;
        int bottom = matrix.length - 1;
        int left = 0;
        int right = matrix[0].length - 1;

        List<Integer> result = new ArrayList<>();

        while (top <= bottom && left <= right) {
            // top row, left to right
            for (int col = left; col <= right; col++) {
                result.add(matrix[top][col]);
            }
            top++;

            // right column, top to bottom
            for (int row = top; row <= bottom; row++) {
                result.add(matrix[row][right]);
            }
            right--;

            // only one row or column was left, and it's walked already
            if (top > bottom || left > right) break;

            // bottom row, right to left
            for (int col = right; col >= left; col--) {
                result.add(matrix[bottom][col]);
            }
            bottom--;

            // left column, bottom to top
            for (int row = bottom; row >= top; row--) {
                result.add(matrix[row][left]);
            }
            left++;
        }
        return result;
    }
}
