/**
 * 1423. Maximum Points You Can Obtain from Cards
 * https://leetcode.com/problems/maximum-points-you-can-obtain-from-cards/
 * Pattern: sliding-window -> fixed-sized-window
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int maxScore(int[] cardPoints, int k) {
        int n = cardPoints.length - k;
        int minSum = Integer.MAX_VALUE;
        int sum = 0;
        int left = 0;
        int total = 0;

        for(int card : cardPoints){
            total += card;
        }

        if(n == 0){
            return total;
        }

        for(int i = 0; i < cardPoints.length; i++){
            int num = cardPoints[i];
            sum += num;

            if(i - left + 1 == n){
                minSum = Math.min(sum, minSum);
                sum -= cardPoints[left];
                left++;
            }
        }
        return total - minSum;
    }
}
