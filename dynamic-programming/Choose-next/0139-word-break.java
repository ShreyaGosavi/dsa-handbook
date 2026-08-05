/**
 * 139. Word Break
 * https://leetcode.com/problems/word-break/
 * Pattern: dynamic-programming -> Choose-next
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public boolean wordBreak(String s, List<String> wordDict) {
        int[] dp = new int[s.length()];
        Arrays.fill(dp, -1);
        HashSet<String> set = new HashSet<>(wordDict);
        return helper(s, 0, set, dp);
    }

    public static boolean helper(String s, int i, HashSet set, int[] dp){
        if(i == s.length()){
            return true;
        }

        if (dp[i] != -1) {
            return dp[i] == 1;
        }

        for(int end = i; end < s.length(); end++){
            String str = s.substring(i, end + 1);

            if(set.contains(str)){

                if(helper(s, end + 1, set, dp)){
                    dp[i] = 1;
                    return true;
                }
            }
        }

        dp[i] = 0;
        return false;
    }
}
