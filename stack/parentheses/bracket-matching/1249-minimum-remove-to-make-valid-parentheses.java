/**
 * 1249. Minimum Remove to Make Valid Parentheses
 * https://leetcode.com/problems/minimum-remove-to-make-valid-parentheses/
 * Pattern: stack -> parentheses/bracket-matching
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public String minRemoveToMakeValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        String ans = "";
        for(int i = 0; i < s.length(); i++){
            char ch = s.charAt(i);

            if(ch == '('){
                stack.push(ch);
                ans = ans + ch;
            }
            else if(ch == ')'){
                if(!stack.isEmpty()){
                    stack.pop();
                    ans = ans + ch;
                }
            }
            else{
                ans = ans + ch;
            }
        }

        stack.clear();
        String result = "";

        for(int i = ans.length() - 1; i >= 0; i--){
            char ch = ans.charAt(i);

            if(ch == ')'){
                stack.push(ch);
                result = ch + result;
            }
            else if(ch == '('){
                if(!stack.isEmpty()){
                    stack.pop();
                    result = ch + result;
                }
            }
            else{
                result = ch + result;
            }
        }

        return result;
    }
}
