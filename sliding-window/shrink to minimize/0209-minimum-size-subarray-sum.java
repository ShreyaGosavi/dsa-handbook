/**
 * 209. Minimum Size Subarray Sum
 * https://leetcode.com/problems/minimum-size-subarray-sum/
 * Pattern: sliding-window -> shrink to minimize
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int minSubArrayLen(int target, int[] nums) {
        int minSize = Integer.MAX_VALUE;
        int left = 0;
        int sum = 0;

        for(int right = 0; right < nums.length; right++){
            int num = nums[right];
            sum += num;

            while(sum >= target){
                minSize = Math.min(minSize, right - left + 1);
                sum -= nums[left];
                left++;
            }
        }

        return minSize == Integer.MAX_VALUE ? 0 : minSize;
    }
}
