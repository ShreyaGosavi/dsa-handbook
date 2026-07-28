/**
 * 1493. Longest Subarray of 1's After Deleting One Element
 * https://leetcode.com/problems/longest-subarray-of-1s-after-deleting-one-element/
 * Pattern: sliding-window -> expand-to-maximize
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int longestSubarray(int[] nums) {
        int left = 0;
        int maxLen = 0;
        int random = 0;

        for(int right = 0; right < nums.length; right++){
            int num = nums[right];
            if(num != 1){
                random++;
            }

            while(random > 1){
                if(nums[left] != 1){
                    random--;
                }
                left++;
            }

            maxLen = Math.max(maxLen, right - left);
        }

        return maxLen;
    }
}
