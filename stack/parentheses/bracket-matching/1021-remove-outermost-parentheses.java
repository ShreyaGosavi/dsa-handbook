/**
 * 1021. Remove Outermost Parentheses
 * https://leetcode.com/problems/remove-outermost-parentheses/
 * Pattern: stack -> parentheses/bracket-matching
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public String removeOuterParentheses(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        String result = "";

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);

            if (ch == '(') {
                stack.push(ch);

                if (stack.size() > 1) {
                    result += ch;
                }
            }

            if (ch == ')') {
                stack.pop();

                if (stack.size() > 0) {
                    result += ch;
                }
            }
        }

        return result;
    }
}
