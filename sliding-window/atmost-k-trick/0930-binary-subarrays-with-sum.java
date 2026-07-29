/**
 * 930. Binary Subarrays With Sum
 * https://leetcode.com/problems/binary-subarrays-with-sum/
 * Pattern: sliding-window -> atmost-k-trick
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int numSubarraysWithSum(int[] nums, int goal) {
        int countk = atMost(nums, goal);
        int countk1 = atMost(nums, goal - 1);
        return countk - countk1;
    }

    public static int atMost(int[] nums, int goal){
        int sum = 0;
        int left = 0;
        int subarrays = 0;

        for(int right = 0; right < nums.length; right++){
            int num = nums[right];
            sum += num;

            while(sum > goal && left <= right){
                int lnum = nums[left];
                sum -= lnum;
                left++;
            }

            subarrays += right - left + 1;
        }

        return subarrays;
    }
}
