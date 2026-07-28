/**
 * 322. Coin Change
 * https://leetcode.com/problems/coin-change/
 * Pattern: dynamic-programming -> Subsequence-pattern
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int coinChange(int[] coins, int amount) {
        int[][] dp = new int[coins.length][amount + 1];

        for(int[] row : dp){
            Arrays.fill(row, -1);
        }

        int ans = helper(coins, dp, 0, amount);

        return ans >= 1000000000 ? -1 : ans;
    }

    public static int helper(int[] coins, int[][] dp, int i, int amount){
        if(amount == 0){
            return 0;
        }

        if(i == coins.length){
            return 1000000000;
        }

        if(dp[i][amount] != -1){
            return dp[i][amount];
        }


        int take = 1000000000;
        if(coins[i] <= amount){
            take = 1 + helper(coins, dp, i, amount - coins[i]);
        }

        int skip = helper(coins, dp, i+1, amount);


        dp[i][amount] = Math.min(take, skip);

        return dp[i][amount];
    }
}
