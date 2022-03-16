/*
 * The given code is provided to assist you to complete the required tasks. But the
 * given code is often incomplete. You have to read and understand the given code
 * carefully, before you can apply the code properly.
 */


import java.util.Arrays;

/*
* Please review Lecture 5 Algorithms Part I, slide 20 to complete this task.
* */
public class MergeSort {

    /**
     * Sorts an array [1...n] by divide-and-conquer. It must use merge() method.
     *
     * @param a input array a needs to be sorted.
     */
    public static void mergeSort(int[] a) {
        /*  */
        int[] L = null; // This array will store the left half of array
        int[] R = null; // This array will store the right half of array
        // TODO: Complete this method
        // START YOUR CODE

        if (a.length > 1) {
            int middle = a.length / 2;
            L = Arrays.copyOfRange(a, 0, middle);
            R = Arrays.copyOfRange(a, middle, a.length);
            System.out.println("\nmergeSort:\na: " + Arrays.toString(a) + "\nL: " + Arrays.toString(L) + "\nR: " + Arrays.toString(R));
            mergeSort(L);
            mergeSort(R);
        } else {
            L = a.clone();
            R = new int[0];
        }

        if (!(L.length != 0 && R.length != 0)) {
            return;
        }
        System.out.println("+1");


        // END YOUR CODE
        merge(a,L,R); //Do not modify this part of code.
    }


    /**
     * Merges sorted subarray L and subarray R into sorted array a.
     *
     * @param a merged array a,
     * @param L input sorted subarray L,
     * @param R input sorted subarray R.
     */
    public static void merge(int[] a, int[] L, int[] R) {

        tracker.calltracking(a,L,R); //Do not modify this method. Otherwise, you will be penalized for violation.
        // TODO: Complete this method
        // START YOUR CODE

        if (R != null && L.length != 0 && R.length != 0) {

//            int[] newList = new int[a.length];
            int i = 0, j = 0;
            while (i < L.length && j < R.length) {
                if (L[i] < R[j]) {
                    a[i+j] = L[i];
                    i += 1;
                } else {
                    a[i+j] = R[j];
                    j += 1;
                }
            }
            if (j >= R.length) {
                int i_ = i;
                while (i_ < L.length) {
                    a[i_+j] = L[i_++];
                }
            }
            if (i >= L.length) {
                int j_ = j;
                while (j_ < R.length) {
                    a[i+j_] = R[j_++];
                }
            }
        }



        // END YOUR CODE

    }

}
