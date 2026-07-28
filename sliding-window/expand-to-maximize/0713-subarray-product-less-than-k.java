/**
 * 713. Subarray Product Less Than K
 * https://leetcode.com/problems/subarray-product-less-than-k/
 * Pattern: sliding-window -> expand-to-maximize
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int numSubarrayProductLessThanK(int[] nums, int k) {
        int left = 0;
        int ways = 0;
        int product = 1;

        if (k <= 1) {
            return 0;
        }

        for(int right = 0; right < nums.length; right++){
            int num = nums[right];

            product *= num;

            while(product >= k){
                int leftnum = nums[left];
                product /= leftnum;
                left++;
            }

            ways += right - left + 1;  
        }
        return ways;
    }
}
