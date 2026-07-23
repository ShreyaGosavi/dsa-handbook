/**
 * 643. Maximum Average Subarray I
 * https://leetcode.com/problems/maximum-average-subarray-i/
 * Pattern: sliding-window -> fixed-sized-window
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public double findMaxAverage(int[] nums, int k) {
        int max = Integer.MIN_VALUE;
        int sum = 0;
        int left = 0;

        for (int right = 0; right < nums.length; right++) {
            sum += nums[right];

            if (right - left + 1 == k) {
                max = Math.max(max, sum);

                sum -= nums[left];
                left++;
            }
        }

        return (double) max / k;
    }
}
