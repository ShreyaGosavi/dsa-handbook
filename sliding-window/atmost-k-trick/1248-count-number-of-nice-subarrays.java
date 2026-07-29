/**
 * 1248. Count Number of Nice Subarrays
 * https://leetcode.com/problems/count-number-of-nice-subarrays/
 * Pattern: sliding-window -> atmost-k-trick
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int numberOfSubarrays(int[] nums, int k) {
        int countk = atMost(nums, k);
        int countk1 = atMost(nums, k - 1);
        return countk - countk1;
    }

    public static int atMost(int[] nums, int k) {
        int count = 0;

        int left = 0;
        int subarrays = 0;

        for (int right = 0; right < nums.length; right++) {
            int num = nums[right];
            if (num % 2 == 1) {
                count++;
            }

            while (count > k) {
                int lnum = nums[left];
                if (lnum % 2 == 1) {
                    count--;
                }
                left++;
            }

            subarrays += right - left + 1;
        }
        return subarrays;
    }
}
