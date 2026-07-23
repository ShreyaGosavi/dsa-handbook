/**
 * 438. Find All Anagrams in a String
 * https://leetcode.com/problems/find-all-anagrams-in-a-string/
 * Pattern: sliding-window -> fixed-sized-window
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public List<Integer> findAnagrams(String s, String p) {
        HashMap<Character, Integer> map1 = new HashMap<>();
        HashMap<Character, Integer> map2 = new HashMap<>();
        List<Integer> result = new ArrayList<>();

        for(int i = 0; i < p.length(); i++){
            map1.put(p.charAt(i), map1.getOrDefault(p.charAt(i), 0) + 1);
        }

        int left = 0;
        for(int right = 0; right < s.length(); right++){
            char ch = s.charAt(right);
            map2.put(ch, map2.getOrDefault(ch, 0) + 1);

            if(right - left + 1 == p.length()){
                if(map1.equals(map2)){
                    result.add(left);
                }

                char leftChar = s.charAt(left);

                map2.put(leftChar, map2.get(leftChar) - 1);

                if (map2.get(leftChar) == 0) {
                    map2.remove(leftChar);
                }

                left++;
            }
        }

        return result;
    }
}
