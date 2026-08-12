/**
 * 140. Word Break II
 * https://leetcode.com/problems/word-break-ii/
 * Pattern: dynamic-programming -> Choose-next
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {

    public List<String> wordBreak(String s, List<String> wordDict) {

        HashSet<String> set = new HashSet<>(wordDict);

        List<String>[] dp = new List[s.length()];

        return helper(s, set, dp, 0);
    }

    public static List<String> helper(
            String s,
            HashSet<String> set,
            List<String>[] dp,
            int i) {

        // Successfully reached the end
        if (i == s.length()) {
            List<String> base = new ArrayList<>();
            base.add("");
            return base;
        }

        // Already solved
        if (dp[i] != null) {
            return dp[i];
        }

        List<String> result = new ArrayList<>();

        // Try every possible next word
        for (int end = i; end < s.length(); end++) {

            String word = s.substring(i, end + 1);

            if (set.contains(word)) {

                List<String> remaining =
                        helper(s, set, dp, end + 1);

                // Put current word in front of
                // every valid sentence from the remaining part
                for (String sentence : remaining) {

                    if (sentence.equals("")) {
                        result.add(word);
                    } else {
                        result.add(word + " " + sentence);
                    }
                }
            }
        }

        dp[i] = result;

        return result;
    }
}
