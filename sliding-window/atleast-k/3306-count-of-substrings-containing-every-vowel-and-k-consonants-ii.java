/**
 * 3306. Count of Substrings Containing Every Vowel and K Consonants II
 * https://leetcode.com/problems/count-of-substrings-containing-every-vowel-and-k-consonants-ii/
 * Pattern: sliding-window -> atleast-k
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {

    public long countOfSubstrings(String word, int k) {
        return atLeast(word, k) - atLeast(word, k + 1);
    }

    private long atLeast(String word, int k) {
        int left = 0;
        int consonants = 0;
        long ans = 0;

        HashMap<Character, Integer> vowelFreq = new HashMap<>();

        for (int right = 0; right < word.length(); right++) {

            char ch = word.charAt(right);

            if (isVowel(ch)) {
                vowelFreq.put(ch, vowelFreq.getOrDefault(ch, 0) + 1);
            } else {
                consonants++;
            }

            while (vowelFreq.size() == 5 && consonants >= k) {

                // Every extension of this window to the right
                // will also satisfy the conditions.
                ans += word.length() - right;

                char remove = word.charAt(left);

                if (isVowel(remove)) {
                    vowelFreq.put(remove, vowelFreq.get(remove) - 1);

                    if (vowelFreq.get(remove) == 0) {
                        vowelFreq.remove(remove);
                    }
                } else {
                    consonants--;
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
