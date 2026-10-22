# Arrays

Each problem folder has a concept GIF, the approach, edge cases and my Java solution.

| # | Problem | Difficulty | Pattern | One-liner |
|---|---|---|---|---|
| 01 | [Two Sum](01-two-sum/) | Easy | Hash map of seen values | For each `x`, have I already seen `target − x`? |
| 02 | [Best Time to Buy and Sell Stock](02-best-time-to-buy-and-sell-stock/) | Easy | Running max from the right | Walk right → left with `maxSellPrice`; each day is a new sell day or a buy worth `maxSellPrice − price` |
