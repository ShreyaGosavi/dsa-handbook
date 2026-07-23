/**
 * 567. Permutation in String
 * https://leetcode.com/problems/permutation-in-string/
 * Pattern: sliding-window -> fixed-sized-window
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public boolean checkInclusion(String s1, String s2) {

        if (s1.length() > s2.length()) {
            return false;
        }

        HashMap<Character, Integer> target = new HashMap<>();
        HashMap<Character, Integer> window = new HashMap<>();

        for (char ch : s1.toCharArray()) {
            target.put(ch, target.getOrDefault(ch, 0) + 1);
        }

        int left = 0;

        for (int right = 0; right < s2.length(); right++) {

            char ch = s2.charAt(right);
            window.put(ch, window.getOrDefault(ch, 0) + 1);

            if (right - left + 1 == s1.length()) {

                if (window.equals(target)) {
                    return true;
                }

                char leftChar = s2.charAt(left);

                window.put(leftChar, window.get(leftChar) - 1);

                if (window.get(leftChar) == 0) {
                    window.remove(leftChar);
                }

                left++;
            }
        }

        return false;
    }
}
