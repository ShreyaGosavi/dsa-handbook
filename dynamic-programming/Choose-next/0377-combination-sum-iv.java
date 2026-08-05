/**
 * 377. Combination Sum IV
 * https://leetcode.com/problems/combination-sum-iv/
 * Pattern: dynamic-programming -> Choose-next
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int combinationSum4(int[] nums, int target) {
        int[] dp = new int[target + 1]; //if i nos left to achieve the target -> how many ways to achieve this i
        Arrays.fill(dp, -1);

        return helper(nums, dp, target);
    }

    public static int helper(int[] nums, int[] dp, int target){
        if(target == 0){
            return 1;
        }
        if(target < 0){
            return 0;
        }

        if(dp[target] != -1){
            return dp[target];
        }
        int ans = 0;
        for(int i = 0; i < nums.length; i++){
            ans += helper(nums, dp, target - nums[i]);
        }

        dp[target] = ans;

        return dp[target];
    }
}
