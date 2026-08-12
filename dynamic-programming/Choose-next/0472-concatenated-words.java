/**
 * 472. Concatenated Words
 * https://leetcode.com/problems/concatenated-words/
 * Pattern: dynamic-programming -> Choose-next
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {

    public List<String> findAllConcatenatedWordsInADict(String[] words) {

        HashSet<String> set = new HashSet<>();

        for (String word : words) {
            set.add(word);
        }

        List<String> result = new ArrayList<>();

        for (String word : words) {

            // Remove current word so it cannot use itself
            set.remove(word);

            Boolean[] dp = new Boolean[word.length()];

            if (helper(word, set, 0, dp)) {
                result.add(word);
            }

            set.add(word);
        }

        return result;
    }

    public boolean helper(String word, HashSet<String> set,
                          int i, Boolean[] dp) {

        if (i == word.length()) {
            return true;
        }

        if (dp[i] != null) {
            return dp[i];
        }

        for (int end = i; end < word.length(); end++) {

            String part = word.substring(i, end + 1);

            if (set.contains(part)) {

                if (helper(word, set, end + 1, dp)) {
                    dp[i] = true;
                    return true;
                }
            }
        }

        dp[i] = false;
        return false;
    }
}
