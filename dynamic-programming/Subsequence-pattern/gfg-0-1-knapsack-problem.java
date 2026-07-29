/**
 * 0 - 1 Knapsack Problem
 * https://www.geeksforgeeks.org/problems/0-1-knapsack-problem0945
 * Source: GeeksforGeeks (Medium)
 * Pattern: dynamic-programming -> Subsequence-pattern
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int knapsack(int W, int val[], int wt[]) {
        int[][] dp = new int[val.length][W + 1];
        for(int[] row : dp){
            Arrays.fill(row, -1);
        }
        
        return helper(dp, val, wt, W, 0);
    }
    
    public static int helper(int[][] dp, int[] val, int[] wt, int w, int i){
        if(w == 0){
            return 0;
        }
        
        if(i == val.length){
            return 0;
        }
        
        if(dp[i][w] != -1){
            return dp[i][w];
        }
        
        
        int take = 0;
        if(wt[i] <= w){
            take = val[i] + helper(dp, val, wt, w - wt[i], i + 1);
        }
        
        int skip = helper(dp, val, wt, w, i + 1);
        
        dp[i][w] = Math.max(take, skip);
        
        return dp[i][w];
    }
}

