/**
 * 1541. Minimum Insertions to Balance a Parentheses String
 * https://leetcode.com/problems/minimum-insertions-to-balance-a-parentheses-string/
 * Pattern: stack -> parentheses/bracket-matching
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int minInsertions(String s) {
       Deque<Character> stack = new ArrayDeque<>();
       int closed = 0;

       for(int i = 0; i < s.length(); i++){
        char ch = s.charAt(i);

        if(stack.isEmpty() && ch == ')'){
            if(i < s.length() - 1 && s.charAt(i + 1) == ')'){
                closed += 1;
                i = i + 1;
            }
            else{
                closed += 2;
            }
        }
        else if(!stack.isEmpty() && ch == ')'){
            if(i < s.length() - 1 && s.charAt(i + 1) == ')'){
                stack.pop();
                i = i + 1;
            }
            else{
                stack.pop();
                closed += 1;
            }
        }
        else{
            stack.push(ch);
        }
       }

       return stack.size() * 2 + closed; 
    }
}
