/**
 * 856. Score of Parentheses
 * https://leetcode.com/problems/score-of-parentheses/
 * Pattern: stack -> parentheses/bracket-matching
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int scoreOfParentheses(String s) {
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0);

        for (char ch : s.toCharArray()) {

            if (ch == '(') {
                stack.push(0);
            } 
            else {
                int inner = stack.pop();

                int score;
                if (inner == 0) {
                    score = 1;          // ()
                } else {
                    score = 2 * inner;  // (A)
                }

                stack.push(stack.pop() + score); // AB
            }
        }

        return stack.peek();
    }
}
