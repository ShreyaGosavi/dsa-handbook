/**
 * 992. Subarrays with K Different Integers
 * https://leetcode.com/problems/subarrays-with-k-different-integers/
 * Pattern: sliding-window -> atmost-k-trick
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int subarraysWithKDistinct(int[] nums, int k) {
        int countk = atMost(nums, k);
        int countk1 = atMost(nums, k - 1);

        return countk - countk1;
    }

    public static int atMost(int[] nums, int k){
        HashMap<Integer, Integer> map = new HashMap<>();

        int left = 0;
        int count = 0;

        for(int right = 0; right < nums.length; right++){
            int num = nums[right];
            map.put(num, map.getOrDefault(num, 0) + 1);

            while(map.size() > k){
                int lnum = nums[left];
                map.put(lnum, map.getOrDefault(lnum, 0) - 1);

                if(map.get(lnum) == 0){
                    map.remove(lnum);
                }
                left++;
            }

            count += right - left + 1;
        }

        return count;
    }
}
