/**
 * 567. Permutation in String
 * https://leetcode.com/problems/permutation-in-string/
 * Pattern: sliding-window -> frequency-matching-window
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public boolean checkInclusion(String s1, String s2) {
        HashMap<Character, Integer> map = new HashMap<>();
        HashMap<Character, Integer> window = new HashMap<>();

        for(int i = 0; i < s1.length(); i++){
            map.put(s1.charAt(i), map.getOrDefault(s1.charAt(i), 0) + 1);
        }

        int left = 0;
        for(int right = 0; right < s2.length(); right++){
            char ch = s2.charAt(right);
            window.put(ch, window.getOrDefault(ch, 0) + 1);

            while(right - left + 1 > s1.length()){
                char lch = s2.charAt(left);
                window.put(lch, window.get(lch) - 1);

                if(window.get(lch) == 0){
                    window.remove(lch);
                } 
                left++;
            }

            if(map.equals(window)){
                return true;
            }
        }
        return false;
    }
}
