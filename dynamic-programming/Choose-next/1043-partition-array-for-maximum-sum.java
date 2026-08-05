/**
 * 1043. Partition Array for Maximum Sum
 * https://leetcode.com/problems/partition-array-for-maximum-sum/
 * Pattern: dynamic-programming -> Choose-next
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int maxSumAfterPartitioning(int[] arr, int k) {

        int[] dp = new int[arr.length];
        Arrays.fill(dp, -1);

        return helper(arr, k, dp, 0);
    }

    public static int helper(int[] arr, int k, int[] dp, int i) {

        if (i == arr.length) {
            return 0;
        }

        if (dp[i] != -1) {
            return dp[i];
        }

        int best = 0;

        for (int len = 1; len <= k && i + len - 1 < arr.length; len++) {

            int max = maximum(arr, i, i + len - 1);

            int add = max * len;

            int ans = add + helper(arr, k, dp, i + len);

            best = Math.max(best, ans);
        }

        dp[i] = best;
        return dp[i];
    }

    public static int maximum(int[] arr, int s, int e) {

        int max = Integer.MIN_VALUE;

        for (int i = s; i <= e; i++) {
            max = Math.max(max, arr[i]);
        }

        return max;
    }
}
