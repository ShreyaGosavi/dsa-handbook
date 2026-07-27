/**
 * 1658. Minimum Operations to Reduce X to Zero
 * https://leetcode.com/problems/minimum-operations-to-reduce-x-to-zero/
 * Pattern: sliding-window -> shrink to minimize
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int minOperations(int[] nums, int x) {
        int total = 0;
        for(int num : nums){
            total += num;
        }
        total = total - x;

        if(total < 0){
            return -1;
        }

        int left = 0;
        int maxLen = Integer.MIN_VALUE;
        int sum = 0;

        for(int right = 0; right < nums.length; right++){
            sum += nums[right];

            while(sum > total){
                sum -= nums[left];
                left++;
            }

            if(sum == total){
                maxLen = Math.max(maxLen, right - left + 1);
            }
        }
        return maxLen == Integer.MIN_VALUE ? -1 : nums.length - maxLen;
    }
}
