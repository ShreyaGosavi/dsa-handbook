/**
 * 1234. Replace the Substring for Balanced String
 * https://leetcode.com/problems/replace-the-substring-for-balanced-string/
 * Pattern: sliding-window -> shrink to minimize
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int balancedString(String s) {
        int n = s.length();
        int limit = n/4;

        HashMap<Character, Integer> map = new HashMap<>();

        //original frequency count
        for(int i = 0; i < s.length(); i++){
            map.put(s.charAt(i), map.getOrDefault(s.charAt(i), 0) + 1);
        }
        //already true
        if(map.getOrDefault('Q', 0) <= limit &&
            map.getOrDefault('W', 0) <= limit &&
            map.getOrDefault('E', 0) <= limit &&
            map.getOrDefault('R', 0) <= limit){
                return 0;
        }

        int left = 0;
        int minLen = Integer.MAX_VALUE;

        for(int right = 0; right < s.length(); right++){
            char ch = s.charAt(right);

            //into the window -> not in the outside
            map.put(ch, map.get(ch) - 1);


            while(left <= right &&
            map.getOrDefault('Q', 0) <= limit &&
            map.getOrDefault('W', 0) <= limit &&
            map.getOrDefault('E', 0) <= limit &&
            map.getOrDefault('R', 0) <= limit){
                minLen = Math.min(minLen, right - left + 1);
                map.put(s.charAt(left), map.getOrDefault(s.charAt(left), 0) + 1);
                left++;
            }
        }
        return minLen;
    }
}
