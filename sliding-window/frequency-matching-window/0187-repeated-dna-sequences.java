/**
 * 187. Repeated DNA Sequences
 * https://leetcode.com/problems/repeated-dna-sequences/
 * Pattern: sliding-window -> frequency-matching-window
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public List<String> findRepeatedDnaSequences(String s) {
        HashMap<String, Integer> map = new HashMap<>();
        List<String> answer = new ArrayList<>();
        int left = 0;

        for(int right = 0; right < s.length(); right++){
            if(right - left + 1 == 10){
                String str = s.substring(left, right + 1);
                map.put(str, map.getOrDefault(str, 0) + 1);

                if(map.get(str) > 1){
                    if(!answer.contains(str)){
                        answer.add(str);
                    }
                }

                left++;
            }
        }
        return answer;
    }
}
