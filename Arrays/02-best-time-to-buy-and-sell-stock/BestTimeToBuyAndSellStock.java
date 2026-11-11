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
