/**
 * 1614. Maximum Nesting Depth of the Parentheses
 * https://leetcode.com/problems/maximum-nesting-depth-of-the-parentheses/
 * Pattern: stack -> parentheses/bracket-matching
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int maxDepth(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        int max = Integer.MIN_VALUE;

        for(int i = 0; i < s.length(); i++){
            char ch = s.charAt(i);

            if(ch == '('){
                stack.push(ch);
            }

            if(ch == ')'){
                stack.pop();
            }

            max = Math.max(max, stack.size());
        }

        return max;
    }
}
