/**
 * 3305. Count of Substrings Containing Every Vowel and K Consonants I
 * https://leetcode.com/problems/count-of-substrings-containing-every-vowel-and-k-consonants-i/
 * Pattern: sliding-window -> atmost-k-trick
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {

    public int countOfSubstrings(String word, int k) {
        return (int)(atMost(word, k) - atMost(word, k - 1));
    }

    private long atMost(String word, int k) {
        if (k < 0) return 0;

        int left = 0;
        int consonants = 0;
        long ans = 0;

        HashMap<Character, Integer> freq = new HashMap<>();

        int lastA = -1;
        int lastE = -1;
        int lastI = -1;
        int lastO = -1;
        int lastU = -1;

        for (int right = 0; right < word.length(); right++) {

            char ch = word.charAt(right);

            if (isVowel(ch)) {
                freq.put(ch, freq.getOrDefault(ch, 0) + 1);

                if (ch == 'a') lastA = right;
                else if (ch == 'e') lastE = right;
                else if (ch == 'i') lastI = right;
                else if (ch == 'o') lastO = right;
                else lastU = right;
            } else {
                consonants++;
            }

            while (consonants > k) {
                char remove = word.charAt(left);

                if (isVowel(remove)) {
                    freq.put(remove, freq.get(remove) - 1);

                    if (freq.get(remove) == 0) {
                        freq.remove(remove);
                    }
                } else {
                    consonants--;
                }

                left++;
            }

            if (freq.size() == 5) {
                int earliest = Math.min(
                        lastA,
                        Math.min(
                                lastE,
                                Math.min(lastI, Math.min(lastO, lastU))
                        )
                );

                if (earliest >= left) {
                    ans += earliest - left + 1;
                }
            }
        }

        return ans;
    }

    private boolean isVowel(char ch) {
        return ch == 'a' || ch == 'e' || ch == 'i'
                || ch == 'o' || ch == 'u';
    }
}
