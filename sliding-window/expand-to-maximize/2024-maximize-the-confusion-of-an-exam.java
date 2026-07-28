/**
 * 2024. Maximize the Confusion of an Exam
 * https://leetcode.com/problems/maximize-the-confusion-of-an-exam/
 * Pattern: sliding-window -> expand-to-maximize
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int maxConsecutiveAnswers(String answerKey, int k) {
        //pass 1 -> wants majority T
        int left = 0;
        int maxT = 0;
        int f = 0;

        for(int right = 0; right < answerKey.length(); right++){
            char ch = answerKey.charAt(right);

            if(ch != 'T'){
                f++;
            }

            while(f > k){
                if(answerKey.charAt(left) != 'T'){
                    f--;
                }
                left++;
            }

            maxT = Math.max(maxT, right - left + 1);
        }


        left = 0;
        int maxF = 0;
        int t = 0;

        for(int right = 0; right < answerKey.length(); right++){
            char ch = answerKey.charAt(right);

            if(ch != 'F'){
                t++;
            }

            while(t > k){
                if(answerKey.charAt(left) != 'F'){
                    t--;
                }
                left++;
            }

            maxF = Math.max(maxF, right - left + 1);
        }

        return maxT > maxF ? maxT : maxF;
    }
}
