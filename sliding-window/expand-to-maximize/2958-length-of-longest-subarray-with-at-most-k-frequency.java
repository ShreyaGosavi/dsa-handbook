/**
 * 2958. Length of Longest Subarray With at Most K Frequency
 * https://leetcode.com/problems/length-of-longest-subarray-with-at-most-k-frequency/
 * Pattern: sliding-window -> expand-to-maximize
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int maxSubarrayLength(int[] nums, int k) {
        HashMap<Integer, Integer> map = new HashMap<>();

        int left = 0;
        int longest = Integer.MIN_VALUE;

        for(int right = 0; right < nums.length; right++){
            int num = nums[right];
            map.put(num, map.getOrDefault(num, 0) + 1);

            while(map.get(num) > k){
                map.put(nums[left], map.getOrDefault(nums[left], 0) - 1);

                if(map.get(nums[left]) == 0){
                    map.remove(nums[left]);
                }
                left++;
            }

            longest = Math.max(longest, right - left + 1);
        }

        return longest;
    }
}
