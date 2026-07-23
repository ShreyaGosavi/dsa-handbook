/**
 * 2461. Maximum Sum of Distinct Subarrays With Length K
 * https://leetcode.com/problems/maximum-sum-of-distinct-subarrays-with-length-k/
 * Pattern: sliding-window -> fixed-sized-window
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public long maximumSubarraySum(int[] nums, int k) {
        HashMap<Integer, Integer> map = new HashMap<>();
        long max = 0;
        long sum = 0;
        int left = 0;

        for(int right = 0; right < nums.length; right++){
            int num = nums[right];
            sum += num;

            if(map.containsKey(num)){
                if(map.get(num) >= left){
                    while(left < map.get(num) + 1){
                        sum -= nums[left];
                        left++;
                    }
                }
            }
            map.put(num, right);

            if(right - left + 1 == k){
                max = Math.max(sum, max);
                sum -= nums[left];
                left++;
            }
        }

        return max;
    }
}
