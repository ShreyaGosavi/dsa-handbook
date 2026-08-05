/**
 * 1155. Number of Dice Rolls With Target Sum
 * https://leetcode.com/problems/number-of-dice-rolls-with-target-sum/
 * Pattern: dynamic-programming -> Choose-next
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int numRollsToTarget(int n, int k, int target) {
        int[][] dp = new int[n + 1][target + 1]; //if x dice left and y target is left -> what are the ways we can achieve these.
        for(int[] row : dp){
            Arrays.fill(row, -1);
        }

        return helper(k, n, target, dp);
    }

    public static int helper(int k, int diceLeft, int target, int[][] dp){
        if(diceLeft == 0 && target == 0){
            return 1;
        }
        if(diceLeft == 0 && target != 0){
            return 0;
        }
        if(target <= 0 && diceLeft > 0){
            return 0;
        }

        if(dp[diceLeft][target] != -1){
            return dp[diceLeft][target];
        }

        //at ith dice -> let us see what no are valid to choose
        int ans = 0;
        for(int i = 1; i <= k; i++){
            ans = (ans + helper(k, diceLeft - 1, target - i, dp)) % 1000000007;
        }

        dp[diceLeft][target] = ans;

        return dp[diceLeft][target];
    }
}
