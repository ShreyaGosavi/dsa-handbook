/**
 * 438. Find All Anagrams in a String
 * https://leetcode.com/problems/find-all-anagrams-in-a-string/
 * Pattern: sliding-window -> frequency-matching-window
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public List<Integer> findAnagrams(String s, String p) {
        HashMap<Character, Integer> map = new HashMap<>();
        HashMap<Character, Integer> window = new HashMap<>();
        int left = 0;
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < p.length(); i++) {
            map.put(p.charAt(i), map.getOrDefault(p.charAt(i), 0) + 1);
        }

        for (int right = 0; right < s.length(); right++) {
            char ch = s.charAt(right);
            window.put(ch, window.getOrDefault(ch, 0) + 1);

            while (right - left + 1 > p.length()) {
                char lch = s.charAt(left);
                window.put(lch, window.get(lch) - 1);

                if (window.get(lch) == 0) {
                    window.remove(lch);
                }
                left++;
            }

            if(map.equals(window)){
                result.add(left);
            }
        }

        return result;
    }
}
