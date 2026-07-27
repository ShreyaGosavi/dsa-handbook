/**
 * 2516. Take K of Each Character From Left and Right
 * https://leetcode.com/problems/take-k-of-each-character-from-left-and-right/
 * Pattern: sliding-window -> shrink to minimize
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int takeCharacters(String s, int k) {

        if (k == 0) {
            return 0;
        }

        int n = s.length();
        int[] outside = new int[3];

        // Total frequency (initially everything is outside the window)
        for (char ch : s.toCharArray()) {
            outside[ch - 'a']++;
        }

        // Impossible to take k of any character
        if (outside[0] < k || outside[1] < k || outside[2] < k) {
            return -1;
        }

        int left = 0;
        int maxWindow = 0;

        for (int right = 0; right < n; right++) {

            // Move character into the middle window
            outside[s.charAt(right) - 'a']--;

            // Outside became invalid -> shrink window
            while (outside[0] < k || outside[1] < k || outside[2] < k) {
                outside[s.charAt(left) - 'a']++;
                left++;
            }

            // Longest valid middle window
            maxWindow = Math.max(maxWindow, right - left + 1);
        }

        return n - maxWindow;
    }
}
