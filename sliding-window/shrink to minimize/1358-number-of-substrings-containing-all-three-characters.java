/**
 * 1358. Number of Substrings Containing All Three Characters
 * https://leetcode.com/problems/number-of-substrings-containing-all-three-characters/
 * Pattern: sliding-window -> shrink to minimize
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int numberOfSubstrings(String s) {

        int n = s.length();
        int[] freq = new int[3];

        int left = 0;
        int ans = 0;

        for (int right = 0; right < n; right++) {

            freq[s.charAt(right) - 'a']++;

            while (freq[0] > 0 && freq[1] > 0 && freq[2] > 0) {

                ans += (n - right);

                freq[s.charAt(left) - 'a']--;
                left++;
            }
        }

        return ans;
    }
}
