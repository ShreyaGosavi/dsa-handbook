/**
 * 2707. Extra Characters in a String
 * https://leetcode.com/problems/extra-characters-in-a-string/
 * Pattern: dynamic-programming -> Choose-next
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int minExtraChar(String s, String[] dictionary) {
        int[] dp = new int[s.length()];
        Arrays.fill(dp, -1);

        HashSet<String> set = new HashSet<>();

        for(String str : dictionary){
            set.add(str);
        }

        return helper(s, set, dp, 0);
    }

    public static int helper(String s, HashSet<String> set, int[] dp, int i){
        if(i == s.length()){
            return 0;
        }

        if(dp[i] != -1){
            return dp[i];
        }

        int find = Integer.MAX_VALUE;

        for(int end = i; end < s.length(); end++){
            String str = s.substring(i, end + 1);

            if(set.contains(str)){
                find = Math.min(find, helper(s, set, dp, end + 1));
            }
        }

        int skip = 1 + helper(s, set, dp, i + 1);

        dp[i] = Math.min(find, skip);

        return dp[i];
    }
}
