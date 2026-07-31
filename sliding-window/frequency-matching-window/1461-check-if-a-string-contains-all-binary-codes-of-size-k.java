/**
 * 1461. Check If a String Contains All Binary Codes of Size K
 * https://leetcode.com/problems/check-if-a-string-contains-all-binary-codes-of-size-k/
 * Pattern: sliding-window -> frequency-matching-window
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public boolean hasAllCodes(String s, int k) {
        HashSet<String> set = new HashSet<>();
        int total = (int) Math.pow(2, k);
        int left = 0;

        for(int right = 0; right < s.length(); right++){
            if(right - left + 1 == k){
                String str = s.substring(left, right + 1);
                set.add(str);
                left++;
            } 
        }

        if(set.size() == total){
            return true;
        }

        return false;
    }
}
