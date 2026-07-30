/**
 * 879. Profitable Schemes
 * https://leetcode.com/problems/profitable-schemes/
 * Pattern: dynamic-programming -> Subsequence-pattern
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int profitableSchemes(int n, int minProfit, int[] group, int[] profit) {
        int[][][] dp = new int[profit.length][n + 1][minProfit + 1];

        for (int[][] rows : dp) {
            for (int[] r : rows) {
                Arrays.fill(r, -1);
            }
        }

        return helper(group, profit, dp, 0, n, minProfit);
    }

    public static int helper(int[] group, int[] profit, int[][][] dp, int i, int membersLeft, int profitNeeded) {
        if (i == profit.length) {
            return profitNeeded == 0 ? 1 : 0;
        }

        if (dp[i][membersLeft][profitNeeded] != -1) {
            return dp[i][membersLeft][profitNeeded];
        }

        int take = 0;
        if (group[i] <= membersLeft) {
            take = helper(group, profit, dp, i + 1, membersLeft - group[i], Math.max(0, profitNeeded - profit[i]));
        }

        int skip = helper(group, profit, dp, i + 1, membersLeft, profitNeeded);

        long ans = (long) take + skip;

        dp[i][membersLeft][profitNeeded] = (int) (ans % 1000000007);

        return dp[i][membersLeft][profitNeeded];
    }
}
