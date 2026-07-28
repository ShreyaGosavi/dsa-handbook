/**
 * 904. Fruit Into Baskets
 * https://leetcode.com/problems/fruit-into-baskets/
 * Pattern: sliding-window -> expand-to-maximize
 * 
 * Approach:
 * Time:  O( )   Space: O( )
 */
class Solution {
    public int totalFruit(int[] fruits) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int left = 0;
        int maxLen = Integer.MIN_VALUE;

        for (int right = 0; right < fruits.length; right++) {
            int type = fruits[right];
            map.put(type, map.getOrDefault(type, 0) + 1);

            while (map.size() > 2) {
                int leftchar = fruits[left];
                map.put(leftchar, map.get(leftchar) - 1);
                if (map.get(leftchar) == 0) {
                    map.remove(leftchar);
                }
                left++;
            }

            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }
}
