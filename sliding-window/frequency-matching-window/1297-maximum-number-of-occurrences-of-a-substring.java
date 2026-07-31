/**
 * 1297. Maximum Number of Occurrences of a Substring
 * https://leetcode.com/problems/maximum-number-of-occurrences-of-a-substring/
 * Pattern: sliding-window -> frequency-matching-window
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int maxFreq(String s, int maxLetters, int minSize, int maxSize) {

        HashMap<Character, Integer> window = new HashMap<>();
        HashMap<String, Integer> count = new HashMap<>();

        int left = 0;
        int ans = 0;

        for (int right = 0; right < s.length(); right++) {

            char ch = s.charAt(right);
            window.put(ch, window.getOrDefault(ch, 0) + 1);

            // Keep window size exactly minSize
            if (right - left + 1 > minSize) {
                char lch = s.charAt(left);

                window.put(lch, window.get(lch) - 1);
                if (window.get(lch) == 0) {
                    window.remove(lch);
                }

                left++;
            }

            // Process only when window size is exactly minSize
            if (right - left + 1 == minSize && window.size() <= maxLetters) {

                String sub = s.substring(left, right + 1);

                count.put(sub, count.getOrDefault(sub, 0) + 1);

                ans = Math.max(ans, count.get(sub));
            }
        }

        return ans;
    }
}
