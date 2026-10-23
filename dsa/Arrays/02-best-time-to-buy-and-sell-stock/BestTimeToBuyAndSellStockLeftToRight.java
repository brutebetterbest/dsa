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
