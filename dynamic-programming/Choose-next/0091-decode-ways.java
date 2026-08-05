/**
 * 91. Decode Ways
 * https://leetcode.com/problems/decode-ways/
 * Pattern: dynamic-programming -> Choose-next
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int numDecodings(String s) {

        int[] dp = new int[s.length()];
        Arrays.fill(dp, -1);

        return helper(s, 0, dp);
    }

    public int helper(String s, int i, int[] dp) {

        if (i == s.length()) {
            return 1;
        }

        if (s.charAt(i) == '0') {
            return 0;
        }

        if (dp[i] != -1) {
            return dp[i];
        }

        // Take one digit
        int one = helper(s, i + 1, dp);

        // Take two digits
        int two = 0;

        if (i + 1 < s.length()) {

            int num = (s.charAt(i) - '0') * 10 + (s.charAt(i + 1) - '0');

            if (num >= 10 && num <= 26) {
                two = helper(s, i + 2, dp);
            }
        }

        dp[i] = one + two;

        return dp[i];
    }
}
