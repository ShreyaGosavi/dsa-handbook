/**
 * 518. Coin Change II
 * https://leetcode.com/problems/coin-change-ii/
 * Pattern: dynamic-programming -> Subsequence-pattern
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int change(int amount, int[] coins) {
        int[][] dp = new int[coins.length][amount + 1];

        for(int[] row : dp){
            Arrays.fill(row, -1);
        }

        return helper(coins, dp, 0, amount);
    }

    public static int helper(int[] coins, int[][] dp, int i, int amount){
        if(amount == 0){
            return 1;
        }

        if(i == coins.length){
            return 0;
        }

        if(dp[i][amount] != -1){
            return dp[i][amount];
        }

        int take = 0;
        if(coins[i] <= amount){
            take = helper(coins, dp, i, amount - coins[i]);
        }

        int skip = helper(coins, dp, i + 1, amount);

        dp[i][amount] = take + skip;

        return dp[i][amount];

    }
}
