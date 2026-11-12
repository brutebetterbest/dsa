# 09 · Set Matrix Zeroes

`Arrays` · `Medium` · [LeetCode #73](https://leetcode.com/problems/set-matrix-zeroes/) · **Pattern: First row and column as flags**

> **One-liner:** use row 0 and column 0 as the flags for which rows and columns become zero, with one extra boolean for row 0's own zero. Zero the inner cells from the flags, then column 0 (its flag is `matrix[0][0]`), and **then** row 0.

## Problem

Given an `m × n` integer matrix, if a cell is `0`, set its whole row and column to `0`, **in place**. The follow-up asks for O(1) extra space. `1 ≤ m, n ≤ 200`, and the values can be any `int`, including negatives.

## Approach: the flags live in the first row and column

1. **Remember row 0's own zero:** scan row 0 and set `firstRowHasZero`. The next pass writes flags into row 0, so this is the only chance to read it.
2. **Mark first row and column as flags:** for every zero at `(i, j)` with `i ≥ 1`, set `matrix[i][0] = 0` (row `i`) and `matrix[0][j] = 0` (column `j`). Because `j` starts at 0, a zero in column 0 sets `matrix[0][0]`, which is column 0's flag.
3. **Mark inner matrix zeroes:** every `(i, j)` with `i, j ≥ 1` becomes `0` if `matrix[i][0] == 0` or `matrix[0][j] == 0`.
4. **Column 0, then row 0:** if `matrix[0][0] == 0`, zero column 0. If `firstRowHasZero`, zero row 0.

## Why it works

Each flag is written into a cell that has to end up `0` anyway: row `i`'s flag `matrix[i][0]` is in row `i`, and column `j`'s flag `matrix[0][j]` is in column `j`. So the flags never destroy an answer. One cell can't flag both row 0 and column 0. That's why `matrix[0][0]` flags column 0 and the boolean `firstRowHasZero` flags row 0.

## Watch out

- **Don't zero while you scan.** A zero you just wrote looks like an original zero and spreads again. With `[[0, 1], [1, 1]]`, zeroing row 0 and column 0 right away writes `0` at `(0, 1)` and `(1, 0)`, and those would zero everything. The right answer is `[[0, 0], [0, 1]]`.
- **Fill column 0 before row 0.** Zeroing row 0 first sets `matrix[0][0] = 0` and wrongly wipes column 0. For `[[1, 0], [1, 1]]`, the right answer is `[[0, 0], [1, 0]]`, but doing row 0 first gives `[[0, 0], [0, 0]]`.
- **Start the inner pass at `i = 1`, `j = 1`.** Row 0 and column 0 hold flags, not data, until the last two steps.
- **The marking pass starts at `i = 1` but `j = 0`.** Row 0 is already covered by `firstRowHasZero`, and `j = 0` is what sets the column-0 flag in `matrix[0][0]`.

## Complexity

| Time | Space |
|---|---|
| **O(m·n)**: a few passes over the grid | **O(1)** extra: one boolean; the flags live in the matrix itself |

## Recognize it next time

- "In place" or "O(1) extra space" on a grid, when the easy version needs a marks array per row and column: store the marks in the first row and column (or in the cells, with an extra state).
- The O(m + n) version with `boolean[] zeroRows` and `boolean[] zeroCols` is the easy stepping stone. Game of Life uses the same in-place idea, encoding the next state in the cell itself.

## My solution · [`SetMatrixZeroes.java`](SetMatrixZeroes.java)

```java
class Solution {
    public void setZeroes(int[][] matrix) {
        int rows = matrix.length, cols = matrix[0].length;

        // the first row holds flags too, so remember its own zero first
        boolean firstRowHasZero = false;
        for (int j = 0; j < cols; j++) {
            if (matrix[0][j] == 0) {
                firstRowHasZero = true;
            }
        }

        // mark first row and column as flags
        for (int i = 1; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;
                    matrix[0][j] = 0;
                }
            }
        }

        // mark inner matrix zeroes
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }

        // fill first column zeroes using the column flag in matrix[0][0]
        if (matrix[0][0] == 0) {
            for (int i = 0; i < rows; i++) {
                matrix[i][0] = 0;
            }
        }

        // fill first row zeroes
        if (firstRowHasZero) {
            for (int j = 0; j < cols; j++) {
                matrix[0][j] = 0;
            }
        }
    }
}
```
