/**
 * 3. Longest Substring Without Repeating Characters
 * https://leetcode.com/problems/longest-substring-without-repeating-characters/
 * Pattern: sliding-window -> expand-to-maximize
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int lengthOfLongestSubstring(String s) {
        HashMap<Character, Integer> map = new HashMap<>();
        int left = 0;
        int maxLen = Integer.MIN_VALUE;

        for(int right = 0; right < s.length(); right++){
            char ch = s.charAt(right);
            map.put(ch, map.getOrDefault(ch, 0) + 1);

            while(map.get(ch) > 1){
                map.put(s.charAt(left), map.get(s.charAt(left)) - 1);
                
                left++;
            }

            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen == Integer.MIN_VALUE ? 0 : maxLen;
    }
}
