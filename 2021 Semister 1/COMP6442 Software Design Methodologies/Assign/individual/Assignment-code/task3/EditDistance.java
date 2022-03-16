/*
 * The given code is provided to assist you to complete the required tasks. But the
 * given code is often incomplete. You have to read and understand the given code
 * carefully, before you can apply the code properly.
 */


/*
 * Please review Lecture 7 Algorithms Part III, slides 10-14 to complete this task.
 * */

//import java.util.Arrays;

/*
* EditCost defines three character edit costs. INSERT and DELETE will cost 1, and REPLACE will cost 2.
* Do not modify the character edit costs. Otherwise, your answers will not be marked correctly.
* */
enum EditCost
{
    INSERT (1),
    DELETE (1),
    REPLACE (2);

    private final int costValue;

    EditCost(int value) {
        this.costValue = value;
    }

    public int getEditCost(){
        return this.costValue;
    }
}

public class EditDistance {


/* This method computes the minimal total cost of a sequence of character edits between two strings.
 * The costs of character edits are defined in EditCost enum
 * @param seq1 the original string.
 * @param seq2 the target string.
 * @return the minimal cost of the character edits.
 * */
    public static int minDistance(String seq1, String seq2){
        // TODO: Complete this method
        // START YOUR CODE

//        System.out.println("\nSeq1: " + seq1 + ", Seq2: " + seq2);
//        System.out.println("INSERT: " + EditCost.INSERT.getEditCost() + ", DELETE: " + EditCost.DELETE.getEditCost() + ", REPLACE: " + EditCost.REPLACE.getEditCost());

        if (seq1 == null && seq2 == null) {
            return 0;
        }
        if (seq1 == null || seq1.isEmpty()) {
            return seq2.length() * EditCost.INSERT.getEditCost();
        }
        if (seq2 == null || seq2.isEmpty()) {
            return seq1.length() * EditCost.INSERT.getEditCost();
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

        //  print array
//        for (int[] ints : cost) {
//            System.out.println(Arrays.toString(ints));
//        }

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (seq1.charAt(i - 1) == seq2.charAt(j - 1)) {
                    cost[i][j] = cost[i - 1][j - 1];
//                    System.out.println("[" + i + "][" + j + "]=[" + seq1.charAt(i-1) + "][" + seq2.charAt(j-1) + "] rep cost: " + 0 + ", del cost: " + 0 + ", ins cost: " + 0 + ", MIN: " + 0);
                } else {
                    int replaceCost = cost[i - 1][j - 1] + EditCost.REPLACE.getEditCost();
                    int deleteCost = cost[i - 1][j] + EditCost.DELETE.getEditCost();
                    int insertCost = cost[i][j - 1] + EditCost.INSERT.getEditCost();
                    cost[i][j] = Math.min(replaceCost, Math.min(deleteCost, insertCost));
//                    System.out.println("[" + i + "][" + j + "]=[" + seq1.charAt(i-1) + "][" + seq2.charAt(j-1) + "] rep cost: " + replaceCost + ", del cost: " + deleteCost + ", ins cost: " + insertCost + ", MIN: " + cost[i][j]);
                }
            }
        }

        //  print array
//        System.out.println("Result: ");
//        for (int[] ints : cost) {
//            System.out.println(Arrays.toString(ints));
//        }

        return cost[len1][len2];
        // END YOUR CODE

    }
}
