/**
 * Knapsack with Duplicate Items
 * https://www.geeksforgeeks.org/problems/knapsack-with-duplicate-items4201
 * Source: GeeksforGeeks (Medium)
 * Pattern: dynamic-programming -> Subsequence-pattern
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int knapSack(int val[], int wt[], int capacity) {
        int[][] dp = new int[val.length][capacity + 1];
        
        for(int[] row : dp){
            Arrays.fill(row, -1);
        }
        
        return helper(val, wt, dp, capacity, 0);
    }
    
    public static int helper(int[] val, int[] wt, int[][] dp, int capacity, int i){
        if(capacity == 0){
            return 0; //values earned are 0
        }
        
        if(i == val.length){
            return 0; //values earned are 0
        }
        
        if(dp[i][capacity] != -1){
            return dp[i][capacity];
        }
        
        int take = 0;
        if(wt[i] <= capacity){
            take = val[i] + helper(val, wt, dp, capacity - wt[i], i);
        }
        
        int skip = helper(val, wt, dp, capacity, i + 1);
        
        dp[i][capacity] = Math.max(take, skip);
        
        return dp[i][capacity];
    }
}
