/**
 * Skeleton code for Edit Distance Computation of DNA sequences.
 * The DNA sequence consists of possible characters from the set {'A', 'C', 'G', 'T'}, 
 * and a special character '?'. Character '?' can be replaced by any character in 
  * {'A', 'C', 'G', 'T'} at zero cost. But character '?' cannot be deleted or inserted.
 * You are required to implement the minDistance method by dynamic programming.
 * The character-dependent edit costs are defined in EditCost class.
 *
 * The given code is provided to assist you to complete the required tasks. But the 
 * given code is often incomplete. You have to read and understand the given code 
 * carefully, before you can apply the code properly. You might need to implement 
 * additional procedures, such as error checking and handling, in order to apply the 
 */

public class EditDistance {

    /* Compute the minimal total cost of character edits between two DNA sequences by dynamic programming.
     * 
     * @param seq1 the original sequence.
     * @param seq2 the target sequence.
     * @return the minimal cost of the character edits.
     */
    public static int minDistance(String seq1, String seq2) {
        // TODO: Complete this method
        // START YOUR CODE
        System.out.println("seq1: " +seq1 + ", seq2: " + seq2);
        if (seq1 == null && seq2 == null) {
            return 0;
        }
        if (seq1 == null || seq1.isEmpty()) {
            int distance = 0;
            int len = seq2.length();
            for (int i = 0; i < len; i++) {
                char c = seq2.charAt(i);
                int index = EditCost.convertToIndex(c);
                distance += EditCost.getInsertCost(index);
            }
            return distance;
        }
        if (seq2 == null || seq2.isEmpty()) {
            int distance = 0;
            int len = seq1.length();
            for (int i = 0; i < len; i++) {
                char c = seq1.charAt(i);
                int index = EditCost.convertToIndex(c);
                distance += EditCost.getInsertCost(index);
            }
            return distance;
        }

        int len1 = seq1.length();
        int len2 = seq2.length();

        int[][] cost = new int[len1 + 1][len2 + 1];

        //  Initialize border:
        for (int i = 0; i <= len1; i++) {
            cost[i][0] = i;
        }
        for (int j = 0; j <= len2; j++) {
            cost[0][j] = j;
        }

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (seq1.charAt(i - 1) == seq2.charAt(j - 1)) {
                    cost[i][j] = cost[i - 1][j - 1];
                } else {
                    char c1 = seq1.charAt(i - 1);
                    char c2 = seq2.charAt(j - 1);
                    int index1 = EditCost.convertToIndex(c1);
                    int index2 = EditCost.convertToIndex(c2);

                    int replaceCost = cost[i - 1][j - 1] + EditCost.getReplaceCost(index1, index2);
                    int deleteCost = cost[i - 1][j] + EditCost.getDeleteCost(index1);
                    int insertCost = cost[i][j - 1] + EditCost.getInsertCost(index2);
                    cost[i][j] = Math.min(replaceCost, Math.min(deleteCost, insertCost));
                }
            }
        }

        return cost[len1][len2];
//        return 0;
        // END YOUR CODE
    }



}