/**
 * 2466. Count Ways To Build Good Strings
 * https://leetcode.com/problems/count-ways-to-build-good-strings/
 * Pattern: dynamic-programming -> Choose-next
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int countGoodStrings(int low, int high, int zero, int one) {
        int[] dp = new int[high + 1];
        Arrays.fill(dp, -1);

        return helper(low, high, zero, one, 0, dp);
    }

    public static int helper(int low, int high, int zero, int one, int str, int[] dp){
        if(str > high){
            return 0;
        }

        if(dp[str] != -1){
            return dp[str];
        }
        int ans = 0;
        if(str >= low && str <= high){
            ans = 1;
            ans += helper(low, high, zero, one, str + one, dp);
            ans += helper(low, high, zero, one, str + zero, dp);
        }
        else{
            ans += helper(low, high, zero, one, str + one, dp);
            ans += helper(low, high, zero, one, str + zero, dp);
        }

        dp[str] = ans % 1000000007;
        return dp[str];
    }
}
