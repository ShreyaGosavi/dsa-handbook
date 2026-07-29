/**
 * 2062. Count Vowel Substrings of a String
 * https://leetcode.com/problems/count-vowel-substrings-of-a-string/
 * Pattern: sliding-window -> atleast-k
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {

    public int countVowelSubstrings(String word) {
        int ans = 0;
        int n = word.length();

        int start = 0;

        while (start < n) {

            // Skip consonants
            while (start < n && !isVowel(word.charAt(start))) {
                start++;
            }

            if (start == n) break;

            int end = start;

            // Find one vowel-only segment
            while (end < n && isVowel(word.charAt(end))) {
                end++;
            }

            ans += countSegment(word, start, end);

            start = end;
        }

        return ans;
    }

    private int countSegment(String word, int start, int end) {

        HashMap<Character, Integer> freq = new HashMap<>();

        int left = start;
        int ans = 0;

        for (int right = start; right < end; right++) {

            char ch = word.charAt(right);
            freq.put(ch, freq.getOrDefault(ch, 0) + 1);

            while (freq.size() == 5) {

                // Every extension inside this vowel segment is valid
                ans += end - right;

                char remove = word.charAt(left);

                freq.put(remove, freq.get(remove) - 1);

                if (freq.get(remove) == 0) {
                    freq.remove(remove);
                }

                left++;
            }
        }

        return ans;
    }

    private boolean isVowel(char ch) {
        return ch == 'a'
            || ch == 'e'
            || ch == 'i'
            || ch == 'o'
            || ch == 'u';
    }
}
