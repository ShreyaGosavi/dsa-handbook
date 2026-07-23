/**
 * 1456. Maximum Number of Vowels in a Substring of Given Length
 * https://leetcode.com/problems/maximum-number-of-vowels-in-a-substring-of-given-length/
 * Pattern: sliding-window -> fixed-sized-window
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int maxVowels(String s, int k) {
        int max = 0;
        int left = 0;
        int count = 0;

        for(int right = 0; right < s.length(); right++){
            char ch = s.charAt(right);
            if(isVowel(ch)){
                count++;
            }

            if(right - left + 1 == k){
                max = Math.max(count, max);
                if(isVowel(s.charAt(left))){
                    count--;
                }
                left++;
            }
        }
        return max;
    }

    public static boolean isVowel(char ch){
        if(ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u'){
            return true;
        }
        return false;
    }
}
