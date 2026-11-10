# Arrays

Each problem folder has a concept GIF, the approach, edge cases and my Java solution.

| # | Problem | Difficulty | Pattern | One-liner |
|---|---|---|---|---|
| 01 | [Two Sum](01-two-sum/) | Easy | Hash map of seen values | For each `x`, have I already seen `target − x`? |
| 02 | [Best Time to Buy and Sell Stock](02-best-time-to-buy-and-sell-stock/) | Easy | Running max from the right | Walk right → left with `maxSellPrice`; each day is a new sell day or a buy worth `maxSellPrice − price` |
| 03 | [Contains Duplicate](03-contains-duplicate/) | Easy | Hash set of seen values | `seen.add(num)` returns false → duplicate; finish the loop → `false` |
| 04 | [Maximum Subarray](04-maximum-subarray/) | Medium | Kadane's algorithm | Extend the run (`currentSum + nums[i]`) or restart at `nums[i]`, keep the bigger; `maxSum` remembers the best |
| 05 | [Product of Array Except Self](05-product-of-array-except-self/) | Medium | Prefix × suffix product | `answer[i]` = left product (pass 1 →) × right product `suffixProduct` (pass 2 ←), no division |
| 06 | [3Sum](06-3sum/) | Medium | Sort + two pointers | Sort; for each `i` (skip repeats), `sum < 0` → `left++`, `sum > 0` → `right--`, `0` → add, then skip repeats |
| 07 | [Container With Most Water](07-container-with-most-water/) | Medium | Two pointers from both ends | Area = `width × min(height[left], height[right])`; keep the best in `maxArea`, move the shorter line inward |
| 08 | [Subarray Sum Equals K](08-subarray-sum-equals-k/) | Medium | Prefix sum + hash map | At each `prefixSum`, add how many times `prefixSum − k` was seen; seed `put(0, 1)`, count then store |
