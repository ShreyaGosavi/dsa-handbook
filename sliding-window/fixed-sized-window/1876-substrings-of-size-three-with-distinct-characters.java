/**
 * 1876. Substrings of Size Three with Distinct Characters
 * https://leetcode.com/problems/substrings-of-size-three-with-distinct-characters/
 * Pattern: sliding-window -> fixed-sized-window
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int countGoodSubstrings(String s) {
        HashMap<Character, Integer> map = new HashMap<>();
        int count = 0;
        int left = 0;

        for(int right = 0; right < s.length(); right++){
            char ch = s.charAt(right);

            if(map.containsKey(ch)){
                if(left <= map.get(ch)){
                    left = map.get(ch) + 1;
                }
            }
            map.put(ch, right);

            if(right - left + 1 == 3){
                count += 1;
                left++;
            }
        }

        return count;
    }
}
