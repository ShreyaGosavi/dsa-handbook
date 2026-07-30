/**
 * Rod Cutting
 * https://www.geeksforgeeks.org/problems/rod-cutting0840
 * Source: GeeksforGeeks (Medium)
 * Pattern: dynamic-programming -> Subsequence-pattern
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int cutRod(int[] price) {
        //i represents the cut length i will be making
        int[][] dp = new int[price.length + 1][price.length + 1];
        
        for(int[] row : dp){
            Arrays.fill(row, -1);
        }
        
        return helper(price, dp, 1, price.length);
    }
    
    public static int helper(int[] price, int[][] dp, int i, int length){
        if(length == 0){
            return 0; //u can earn nothing if left nothing
        }
        if(i == price.length + 1){
            return 0; //u cannot make a cut here -> hence earning 0
        }
        
        if(dp[i][length] != -1){
            return dp[i][length];
        }
        
        int take = 0;
        if(i <= length){
            take = price[i - 1] + helper(price, dp, i, length - i);
        }
        
        int skip = helper(price, dp, i + 1, length);
        
        dp[i][length] = Math.max(take, skip);
        
        return dp[i][length];
    }
}
