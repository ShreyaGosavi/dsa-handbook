/**
 * 1695. Maximum Erasure Value
 * https://leetcode.com/problems/maximum-erasure-value/
 * Pattern: sliding-window -> expand-to-maximize
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int maximumUniqueSubarray(int[] nums) {
        HashMap<Integer, Integer> map = new HashMap<>();

        int left = 0; 
        int maxSum = 0;
        int sum = 0;

        for(int right = 0; right < nums.length; right++){
            int num = nums[right];
            map.put(num, map.getOrDefault(num, 0) + 1);
            sum += num;

            while(map.get(num) > 1){
                map.put(nums[left], map.get(nums[left]) - 1);
                sum -= nums[left];
                left++;
            }

            maxSum = Math.max(maxSum, sum);
        }

        return maxSum;
    }
}
