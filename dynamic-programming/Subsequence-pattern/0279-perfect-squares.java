/**
 * 279. Perfect Squares
 * https://leetcode.com/problems/perfect-squares/
 * Pattern: dynamic-programming -> Subsequence-pattern
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int numSquares(int n) {
        int sqrt = (int) Math.sqrt(n);
        int[][] dp = new int[sqrt + 1][n + 1];

        for (int[] row : dp) {
            Arrays.fill(row, -1);
        }

        return helper(dp, sqrt, 1, n);
    }

    public static int helper(int[][] dp, int sqrt, int i, int target) {
        if (target == 0) {
            return 0;
        }

        if (i > sqrt) {
            return 1000000000;
        }

        if (dp[i][target] != -1) {
            return dp[i][target];
        }

        int take = 1000000000;
        if (i * i <= target) {
            take = 1 + helper(dp, sqrt, i, target - i * i);
        }

        int skip = helper(dp, sqrt, i + 1, target);

        dp[i][target] = Math.min(take, skip);

        return dp[i][target];

    }
}
