/**
 * 2266. Count Number of Texts
 * https://leetcode.com/problems/count-number-of-texts/
 * Pattern: dynamic-programming -> Choose-next
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int countTexts(String pressedKeys) {
        int[] dp = new int[pressedKeys.length()];
        Arrays.fill(dp, -1);

        return helper(pressedKeys, dp, 0);
    }

    public static int helper(String pk, int[] dp, int i){
        if(i == pk.length()){
            return 1;
        }

        if(dp[i] != -1){
            return dp[i];
        }

        char ch = pk.charAt(i);
        int limit = 0;

        if(ch == '7' || ch == '9'){
            limit = 4;
        }
        else{
            limit = 3;
        }

        int ans = 0;
        for (int len = 1; len <= limit; len++) {

            // Enough characters?
            if (i + len - 1 >= pk.length()) {
                break;
            }

            // Are all digits equal?
            if (pk.charAt(i + len - 1) != ch) {
                break;
            }

            ans = (ans + helper(pk,dp, i + len)) % 1000000007;
        }

        dp[i] = (int) ans;
        return dp[i];
    }
}
