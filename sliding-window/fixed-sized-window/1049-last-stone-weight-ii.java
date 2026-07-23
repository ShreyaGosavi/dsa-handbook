/**
 * 1049. Last Stone Weight II
 * https://leetcode.com/problems/last-stone-weight-ii/
 * Pattern: sliding-window -> fixed-sized-window
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {

    public int lastStoneWeightII(int[] stones) {

        int total = 0;

        for (int stone : stones) {
            total += stone;
        }

        int target = total / 2;

        int[][] dp = new int[stones.length][target + 1];

        for (int[] row : dp) {
            Arrays.fill(row, -1);
        }

        int subsetSum = helper(0, target, stones, dp);

        return total - 2 * subsetSum;
    }

    private int helper(int i, int target, int[] stones, int[][] dp) {

        if (i == stones.length) {
            return 0;
        }

        if (dp[i][target] != -1) {
            return dp[i][target];
        }

        int take = 0;

        if (stones[i] <= target) {
            take = stones[i] + helper(i + 1, target - stones[i], stones, dp);
        }

        int skip = helper(i + 1, target, stones, dp);

        dp[i][target] = Math.max(take, skip);

        return dp[i][target];
    }
}
