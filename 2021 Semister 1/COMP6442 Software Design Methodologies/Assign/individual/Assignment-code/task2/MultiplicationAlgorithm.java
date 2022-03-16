/*
 * The given code is provided to assist you to complete the required tasks. But the
 * given code is often incomplete. You have to read and understand the given code
 * carefully, before you can apply the code properly.
 */


/*
 * Please review Lecture 5 Algorithms Part I, slide 49 to complete this task.
 * */

public class MultiplicationAlgorithm {

    /**
     * Using divide-and-conquer to implement the Karatsuba multiplication to compute the product x times y.
	 * x and y are two n-digit input numbers.
     *
     * @param x input x
     * @param y input y
     */
    public static long KMultiply(long x, long y){
        // TODO: Complete this method
        tracker.calltracking(x,y); //Do not modify this method. Otherwise, you will be penalized for violation.
        // START YOUR CODE

        String strX = String.valueOf(x);
        String strY = String.valueOf(y);
        int lenX = strX.length();
        int lenY = strY.length();

        int n = Math.max(lenX, lenY);

        if (n == 1) {
//            System.out.println("\nx: " + x + ", y: " + y);
//            System.out.println("x x y: " + (x * y));
            return x * y;
        }

        if (n % 2 != 0) {
            n += 1;
        }

        int n_2 = n / 2;

        long a = (long) (x / Math.pow(10, n_2));
        long b = (long) (x - a * Math.pow(10, n_2));
        long c = (long) (y / Math.pow(10, n_2));
        long d = (long) (y - c * Math.pow(10, n_2));



        long ac = KMultiply(a, c);
        long bd = KMultiply(b, d);
        long abcd = KMultiply(a+b, c+d);

//        System.out.println("\nx: " + x + ", y: " + y + ", should be: " + (x * y));
//        System.out.println("n: " + n + ", n/2: " + n_2);
//        System.out.println("a: " + a + ", b: " + b + ", c: " + c + ", d: " + d);
//        System.out.println("a x c: " + ac + ", b x d: " + bd + ", (a+b): " + (a+b) + ", (c+d): " + (c+d) + ", (a+b) x (c+d): " + abcd);
//        System.out.println("(ac * (10 ^ n)) = (" + ac + " * 10 ^ " + n + ") = " + (ac * Math.pow(10, n)));
//        System.out.println("(a x d + c x b) * (10 ^ n/2) = (" + a + " x " + d + " + " + c + " x " + b + ") * (10 ^ " + n_2 + ") = " + ((abcd - ac - bd) * Math.pow(10, n_2)));
//        System.out.println((ac * Math.pow(10, n)) + " + " + ((abcd - ac - bd) * Math.pow(10, n_2)) + " + " + bd + " = " + (ac * Math.pow(10, n) + (abcd - ac - bd) * Math.pow(10, n_2) + bd));

        return (long) (ac * Math.pow(10, n) + (abcd - ac - bd) * Math.pow(10, n_2) + bd);

//        return 0;
        // END YOUR CODE
    }
}
