/**
 * 983. Minimum Cost For Tickets
 * https://leetcode.com/problems/minimum-cost-for-tickets/
 * Pattern: dynamic-programming -> Choose-next
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int mincostTickets(int[] days, int[] costs) {
        int[] dp = new int[days.length + 1];
        Arrays.fill(dp, -1);

        return helper(days, costs, dp, 0);
    }

    public static int helper(int[] days, int[] costs, int[] dp, int i) {
        if (i == days.length) {
            return 0;
        }

        if (dp[i] != -1) {
            return dp[i];
        }

        //one day pass
        int one = costs[0] + helper(days, costs, dp, i + 1);

        //7 days pass
        int maxdays = days[i] + 6;
        int newI = i;
        while (newI < days.length && days[newI] <= maxdays) {
            newI++;
        }
        int seven = costs[1] + helper(days, costs, dp, newI);

        maxdays = days[i] + 29;
        newI = i;
        while (newI < days.length && days[newI] <= maxdays) {
            newI++;
        }
        int thirty = costs[2] + helper(days, costs, dp, newI);

        dp[i] = Math.min(one, Math.min(seven, thirty));
        return dp[i];
    }

    //now instead of a while loop we can use binary search aswell -> reducing worst case n2 to log of n
}
