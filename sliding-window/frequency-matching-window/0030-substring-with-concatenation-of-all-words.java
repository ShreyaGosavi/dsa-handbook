/**
 * 30. Substring with Concatenation of All Words
 * https://leetcode.com/problems/substring-with-concatenation-of-all-words/
 * Pattern: sliding-window -> frequency-matching-window
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public List<Integer> findSubstring(String s, String[] words) {

        List<Integer> ans = new ArrayList<>();

        if (s == null || s.length() == 0 || words.length == 0)
            return ans;

        int wordLen = words[0].length();
        int wordCount = words.length;

        HashMap<String, Integer> need = new HashMap<>();

        for (String word : words) {
            need.put(word, need.getOrDefault(word, 0) + 1);
        }

        // Try every possible starting alignment
        for (int offset = 0; offset < wordLen; offset++) {

            HashMap<String, Integer> window = new HashMap<>();

            int left = offset;
            int count = 0;

            for (int right = offset; right + wordLen <= s.length(); right += wordLen) {

                String word = s.substring(right, right + wordLen);

                if (need.containsKey(word)) {

                    window.put(word, window.getOrDefault(word, 0) + 1);
                    count++;

                    // Too many copies of this word
                    while (window.get(word) > need.get(word)) {

                        String leftWord = s.substring(left, left + wordLen);

                        window.put(leftWord, window.get(leftWord) - 1);

                        if (window.get(leftWord) == 0)
                            window.remove(leftWord);

                        left += wordLen;
                        count--;
                    }

                    // Found a valid window
                    if (count == wordCount) {

                        ans.add(left);

                        String leftWord = s.substring(left, left + wordLen);

                        window.put(leftWord, window.get(leftWord) - 1);

                        if (window.get(leftWord) == 0)
                            window.remove(leftWord);

                        left += wordLen;
                        count--;
                    }

                } else {

                    window.clear();
                    count = 0;
                    left = right + wordLen;
                }
            }
        }

        return ans;
    }
}
