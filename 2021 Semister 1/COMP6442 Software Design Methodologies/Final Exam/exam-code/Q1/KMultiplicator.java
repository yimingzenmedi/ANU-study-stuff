/**
 * The given code is provided to assist you to complete the required tasks. But
 * the given code is often incomplete. You have to read and understand the given
 * code carefully, before you can apply the code properly. You might need to
 * implement additional procedures, such as error checking and handling, in
 * order to apply the code properly.
 */

public class KMultiplicator extends Multiplicator {

	private final Tracker tracker;

	public KMultiplicator(Tracker tracker) {
		this.tracker = tracker;
	}

	@Override
	public long multiply(long x, long y) {
		this.tracker.trace(x, y); // Do not modify this method. Otherwise, you will be penalized for violation.
		long result = 0;

		// ########## YOUR CODE STARTS HERE ##########
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



		long ac = multiply(a, c);
		long bd = multiply(b, d);
		long abcd = multiply(a+b, c+d);

//        System.out.println("\nx: " + x + ", y: " + y + ", should be: " + (x * y));
//        System.out.println("n: " + n + ", n/2: " + n_2);
//        System.out.println("a: " + a + ", b: " + b + ", c: " + c + ", d: " + d);
//        System.out.println("a x c: " + ac + ", b x d: " + bd + ", (a+b): " + (a+b) + ", (c+d): " + (c+d) + ", (a+b) x (c+d): " + abcd);
//        System.out.println("(ac * (10 ^ n)) = (" + ac + " * 10 ^ " + n + ") = " + (ac * Math.pow(10, n)));
//        System.out.println("(a x d + c x b) * (10 ^ n/2) = (" + a + " x " + d + " + " + c + " x " + b + ") * (10 ^ " + n_2 + ") = " + ((abcd - ac - bd) * Math.pow(10, n_2)));
//        System.out.println((ac * Math.pow(10, n)) + " + " + ((abcd - ac - bd) * Math.pow(10, n_2)) + " + " + bd + " = " + (ac * Math.pow(10, n) + (abcd - ac - bd) * Math.pow(10, n_2) + bd));

		result = (long) (ac * Math.pow(10, n) + (abcd - ac - bd) * Math.pow(10, n_2) + bd);

		// ########## YOUR CODE ENDS HERE ##########

		return result;
	}
}
