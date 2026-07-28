/**
 * 1004. Max Consecutive Ones III
 * https://leetcode.com/problems/max-consecutive-ones-iii/
 * Pattern: sliding-window -> expand-to-maximize
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int longestOnes(int[] nums, int k) {
        int left = 0;
        int max = Integer.MIN_VALUE;
        int zeros = 0;

        for(int right = 0; right < nums.length; right++){
            int num = nums[right];
            if(num == 0){
                zeros++;
            }

            while(zeros > k){
                if(nums[left] == 0){
                    zeros--;
                }
                left++;
            }

            max = Math.max(max, right - left + 1);
        }

        return max;
    }
}
