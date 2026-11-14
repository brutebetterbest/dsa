# 02 · Best Time to Buy and Sell Stock

`Arrays` · `Easy` · [LeetCode #121](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/) · **Pattern: Running max from the right**

[![Best Time to Buy and Sell Stock: the lesson, from brute force to optimal](https://img.youtube.com/vi/V3Ic6tEwLSQ/maxresdefault.jpg)](https://youtu.be/V3Ic6tEwLSQ)

▶ **[Watch the lesson](https://youtu.be/V3Ic6tEwLSQ)** · Quick version: **[Short](https://youtube.com/shorts/ElhEPZ39-2g)**

> **One-liner:** walk right → left carrying `maxSellPrice`, the highest price on a later day. Each day is either a new sell day or a buy day worth `maxSellPrice − prices[i]`.

## Problem

`prices[i]` is the stock price on day `i`. Buy on one day and sell on a **later** day. Return the maximum profit, or `0` if no trade makes money.

## From brute force to optimal

The lesson builds three solutions in order. The earlier ones are folded below; the optimal one, my solution, is the rest of this page.

<details>
<summary><b>1. Brute force</b> · O(n²) time · O(1) space</summary>

Try every trade: pick a buy day, then every later sell day.

```java
class Solution {
    public int maxProfit(int[] prices) {
        int maxProfit = 0;
        for (int buy = 0; buy < prices.length; buy++) {
            for (int sell = buy + 1; sell < prices.length; sell++) {
                maxProfit = Math.max(maxProfit, prices[sell] - prices[buy]);
            }
        }
        return maxProfit;
    }
}
```

</details>

<details>
<summary><b>2. Better: best later price array</b> · O(n) time · O(n) space</summary>

Every buy day asks the same question, *what is the highest price after me?* Answer it once for every day in `maxSellPrice`, filled right to left. Then each day needs one subtraction.

```java
class Solution {
    public int maxProfit(int[] prices) {
        int n = prices.length;
        int[] maxSellPrice = new int[n];          // the highest price after day i
        for (int i = n - 2; i >= 0; i--) {
            maxSellPrice[i] = Math.max(maxSellPrice[i + 1], prices[i + 1]);
        }

        int maxProfit = 0;
        for (int i = 0; i < n - 1; i++) {
            maxProfit = Math.max(maxProfit, maxSellPrice[i] - prices[i]);
        }
        return maxProfit;
    }
}
```

</details>

**3. Optimal: one `maxSellPrice`** · O(n) time · O(1) space, explained below.

## Optimal approach: the move, for each day from right to left

Start with `maxSellPrice = prices[n − 1]` (the last day is the only sell option) and `maxProfit = 0`.

1. **Price ≥ `maxSellPrice`? New sell day:** `maxSellPrice = prices[i]`. Every earlier day would rather sell here.
2. **Lower → profit if I buy today:** `maxSellPrice − prices[i]`.
3. **Keep the best profit:** `maxProfit = Math.max(maxProfit, maxSellPrice − prices[i])`.

## Why one pass is enough

To buy on day `i`, the best sell is the highest price **after** `i`. Walking right → left, `maxSellPrice` is exactly that, so each day is answered in O(1) instead of scanning every later day.

## Watch out

- **Sell after you buy.** It isn't `max − min` of the array: in `[7, 1, 5, 3, 6, 4]`, `7 − 1` is illegal because 7 comes first. Starting from the last day keeps every sell to the right of its buy.
- **Prices only fall** (`[7, 6, 4, 3, 1]`): every day is a new high, the else branch never runs, and the answer stays `0`.
- **One day only:** the loop never runs → `0`. (An empty array would throw at `prices[prices.length − 1]`, but LeetCode guarantees at least one day.)

## Complexity

| Approach | Time | Space |
|---|---|---|
| Brute force | O(n²) | O(1) |
| Better: best later price array | O(n) | O(n) |
| **Optimal: one `maxSellPrice`** (my solution) | **O(n)**: one pass, O(1) work per day | **O(1)**: just `maxSellPrice` and `maxProfit` |

## Recognize it next time

- "Best pair `(i, j)` with `i < j`", where the value depends on one element and the **best element after it** (or before it) → carry a running max/min in one pass instead of trying every pair.
- The mirror version is the one most editorials show: scan left → right, keep `minBuyPrice` (the cheapest buy so far), and ask "what if I sell today?". Same idea, same cost. See **Another approach** below.

## My solution (optimal) · [`BestTimeToBuyAndSellStock.java`](BestTimeToBuyAndSellStock.java)

```java
class Solution {
    public int maxProfit(int[] prices) {
        int maxProfit = 0;
        int maxSellPrice = prices[prices.length - 1];

        for (int i = prices.length - 2; i >= 0; i--) {
            if (prices[i] >= maxSellPrice) {
                maxSellPrice = prices[i];
            } 
            else {
                maxProfit = Math.max (maxProfit, maxSellPrice - prices[i]);
            }
        }
        return maxProfit;
    }
}
```

<details>
<summary><b>The mirror: left → right with <code>minBuyPrice</code></b> · O(n) time · O(1) space</summary>

My other version, [`BestTimeToBuyAndSellStockLeftToRight.java`](BestTimeToBuyAndSellStockLeftToRight.java).

The mirror image of the solution above. Walk **left → right** and carry `minBuyPrice`, the cheapest price on an **earlier** day. Each day is either a sell day worth `prices[i] − minBuyPrice`, or the new cheapest day to buy. Same O(n) time and O(1) space.

```java
class Solution {
    public int maxProfit(int[] prices) {
        int maxProfit = 0;
        int minBuyPrice = prices[0];

        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > minBuyPrice) {
                maxProfit = Math.max(maxProfit, prices[i] - minBuyPrice);
            } else {
                minBuyPrice = prices[i];
            }
        }
        return maxProfit;
    }
}
```

</details>
