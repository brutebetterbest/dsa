# 10 · Spiral Matrix

`Arrays` · `Medium` · [LeetCode #54](https://leetcode.com/problems/spiral-matrix/) · **Pattern: Four shrinking boundaries**

> **One-liner:** keep four boundaries, `top`, `bottom`, `left` and `right`, around the part not visited yet. Walk the top row, the right column, the bottom row and the left column, and shrink each side right after walking it. If only one row or column was left, stop before walking it back.

## Problem

Given an `m × n` matrix, return all its elements in spiral order: clockwise from the top-left corner, along the top row, down the right column, back along the bottom row, up the left column, then the same on the inner part, until every element is taken once. `1 ≤ m, n ≤ 10`, and the values are between −100 and 100.

## From brute force to optimal

The lesson builds three solutions in order. All three are O(m·n) time; each one needs less extra space than the last. The earlier ones are folded below; the optimal one, my solution, is the rest of this page.

<details>
<summary><b>1. Brute force: walk and turn, with a visited grid</b> · O(m·n) time · O(m·n) space</summary>

Walk like a robot: keep going right, and turn right whenever the next cell is outside the grid or already visited. A `visited` grid the size of the matrix remembers every cell you've been to.

```java
class Solution {
    public List<Integer> spiralOrder(int[][] matrix) {
        int rows = matrix.length, cols = matrix[0].length;
        boolean[][] visited = new boolean[rows][cols];
        int[] rowStep = {0, 1, 0, -1};   // right, down, left, up
        int[] colStep = {1, 0, -1, 0};

        List<Integer> result = new ArrayList<>();
        int row = 0, col = 0, dir = 0;
        for (int count = 0; count < rows * cols; count++) {
            result.add(matrix[row][col]);
            visited[row][col] = true;

            int nextRow = row + rowStep[dir], nextCol = col + colStep[dir];
            if (nextRow < 0 || nextRow >= rows || nextCol < 0 || nextCol >= cols
                    || visited[nextRow][nextCol]) {
                dir = (dir + 1) % 4;   // turn right
                nextRow = row + rowStep[dir];
                nextCol = col + colStep[dir];
            }
            row = nextRow;
            col = nextCol;
        }
        return result;
    }
}
```

</details>

<details>
<summary><b>2. Better: mark visited cells in the matrix</b> · O(m·n) time · O(1) space</summary>

The `visited` grid is a second matrix just to hold marks. Write the mark into the matrix itself instead: the values are −100 to 100, so `101` can never be a real value. The catch: the caller's matrix is destroyed, and every step still checks the walls and the mark.

```java
class Solution {
    public List<Integer> spiralOrder(int[][] matrix) {
        final int VISITED = 101;   // values are -100 to 100, so 101 never appears
        int rows = matrix.length, cols = matrix[0].length;
        int[] rowStep = {0, 1, 0, -1};   // right, down, left, up
        int[] colStep = {1, 0, -1, 0};

        List<Integer> result = new ArrayList<>();
        int row = 0, col = 0, dir = 0;
        for (int count = 0; count < rows * cols; count++) {
            result.add(matrix[row][col]);
            matrix[row][col] = VISITED;

            int nextRow = row + rowStep[dir], nextCol = col + colStep[dir];
            if (nextRow < 0 || nextRow >= rows || nextCol < 0 || nextCol >= cols
                    || matrix[nextRow][nextCol] == VISITED) {
                dir = (dir + 1) % 4;   // turn right
                nextRow = row + rowStep[dir];
                nextCol = col + colStep[dir];
            }
            row = nextRow;
            col = nextCol;
        }
        return result;
    }
}
```

</details>

**3. Optimal: four shrinking boundaries** · O(m·n) time · O(1) space, explained below.

## Optimal approach: walk the four sides, then shrink

The visited cells are always whole rows and columns around the outside, so the part not visited yet is always a rectangle. Four numbers describe it, and no cell needs a mark.

1. **Frame the whole matrix:** `top = 0`, `bottom = m − 1`, `left = 0`, `right = n − 1`.
2. **Top row, left to right**, then `top++`: that row is done.
3. **Right column, top to bottom**, then `right--`.
4. **Only one row or column was left?** If `top > bottom` or `left > right`, the first two sides already walked it: stop.
5. **Bottom row, right to left** (`bottom--`), then **left column, bottom to top** (`left++`). Repeat while `top ≤ bottom` and `left ≤ right`.

On `[[1,2,3,4],[5,6,7,8],[9,10,11,12]]`, the first round takes `1 2 3 4`, `8 12`, `11 10 9`, `5`. What's left is the single row `6 7`: the top-row pass takes it, `top` moves past `bottom`, and the check stops the walk. Answer: `1 2 3 4 8 12 11 10 9 5 6 7`.

## Why it works

Each side's loop takes exactly the cells of one side of the current rectangle, and shrinking that boundary right away removes them from it. So the rectangle always holds exactly the cells not taken yet, and every cell is added once, in clockwise order.

## Watch out

- **Check after the first two sides.** When one row or one column is left, the first two sides take all of it, and the way back would add it again. In the example, the bottom-row pass would walk the row `6 7` back and add `6` twice. On a single column `[[1],[2],[3]]`, the left-column pass would add `2` twice. One check, `top > bottom || left > right`, covers both.
- **Shrink each boundary right after its side.** Then the next side starts past the corner: the right column starts at row 1, so `4` isn't added twice.
- **Test rectangles, not just squares.** A square never needs the check: it ends on a single cell or a 2×2 ring. Rectangles like the 3×4 example or `[[1],[2],[3]]` do.

## Complexity

| Approach | Time | Space |
|---|---|---|
| Brute force: walk and turn, with a visited grid | O(m·n) | O(m·n): a `visited` grid |
| Better: mark visited cells in the matrix | O(m·n) | O(1) extra, but it overwrites the input |
| **Optimal: four shrinking boundaries** (my solution) | **O(m·n)**: each cell is added once | **O(1)**: four boundaries, and the input is untouched |

## Recognize it next time

- "Spiral", "clockwise", "layer by layer" or "ring" on a grid: four boundaries that shrink after each side.
- Spiral Matrix II (fill `1` to `n²` in spiral order) uses the same four loops, writing instead of reading. Rotate Image also works ring by ring.

## My solution (optimal) · [`SpiralMatrix.java`](SpiralMatrix.java)

```java
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
```
