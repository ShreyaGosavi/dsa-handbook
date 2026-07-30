/**
 * 474. Ones and Zeroes
 * https://leetcode.com/problems/ones-and-zeroes/
 * Pattern: dynamic-programming -> Subsequence-pattern
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int findMaxForm(String[] strs, int m, int n) {
        int[][][] dp = new int[strs.length][m + 1][n + 1];

        for (int[][] row : dp) {
            for (int[] r : row) {
                Arrays.fill(r, -1);
            }
        }

        return helper(strs, dp, m, n, 0);
    }

    public static int helper(String[] strs, int[][][] dp, int zero, int one, int i) {
        if (zero == 0 && one == 0) {
            return 0; //no element added
        }

        if (i == strs.length) {
            return 0; //cannot add anything further
        }

        if (dp[i][zero][one] != -1) {
            return dp[i][zero][one];
        }

        String str = strs[i];
        int no1 = 0;
        int no0 = 0;

        for (int k = 0; k < str.length(); k++) {
            char ch = str.charAt(k);
            if (ch == '1') {
                no1++;
            } else {
                no0++;
            }
        }

        int take = 0;
        if (no1 <= one && no0 <= zero) {
            take = 1 + helper(strs, dp, zero - no0, one - no1, i + 1);
        }

        int skip = helper(strs, dp, zero, one, i + 1);

        dp[i][zero][one] = Math.max(take, skip);

        return dp[i][zero][one];
    }

}
