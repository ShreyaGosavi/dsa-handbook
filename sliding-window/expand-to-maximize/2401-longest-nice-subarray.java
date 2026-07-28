/**
 * 2401. Longest Nice Subarray
 * https://leetcode.com/problems/longest-nice-subarray/
 * Pattern: sliding-window -> expand-to-maximize
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int longestNiceSubarray(int[] nums) {

        int left = 0;
        int mask = 0;
        int maxLen = 0;

        for (int right = 0; right < nums.length; right++) {

            // Shrink until there is no bit conflict
            while ((mask & nums[right]) != 0) {
                mask ^= nums[left];
                left++;
            }

            // Add the current number to the window
            mask |= nums[right];

            maxLen = Math.max(maxLen, right - left + 1);
        }

        return maxLen;
    }
}
