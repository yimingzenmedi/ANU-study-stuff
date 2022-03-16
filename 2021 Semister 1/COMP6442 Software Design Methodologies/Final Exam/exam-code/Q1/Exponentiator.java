/**
 * The given code is provided to assist you to complete the required tasks. But
 * the given code is often incomplete. You have to read and understand the given
 * code carefully, before you can apply the code properly. You might need to
 * implement additional procedures, such as error checking and handling, in
 * order to apply the code properly.
 */

public class Exponentiator {

	private final Tracker tracker;
	private Multiplicator multiplicator;

	public Exponentiator(Tracker tracker) {
		this.tracker = tracker;
		this.multiplicator = new Multiplicator();
	}

	public void setMultiplicator(Multiplicator multiplicator) {
		this.multiplicator = multiplicator;
	}

	public long exponentiate(long base, long power) throws Exception {

		this.tracker.trace(base, power); // Do not modify this method. Otherwise, you will be penalized for violation.
		long result = 0;

		// ########## YOUR CODE STARTS HERE ##########
//		System.out.println(base + " ^ " + power + ", should be: " + Math.pow(base, power));
		if (power == 0) {
			if (base == 0) {
				throw new IllegalExponentiationException();
			}
			return 1;
		}
		if (power == 1) {
			return base;
		}
		if (power == 2) {
			return base * base;
		}

		long power1 = power / 2;
		long power2 = power - power1;
		result = exponentiate(base, power1) * exponentiate(base, power2);

		System.out.println(result);
		// ########## YOUR CODE ENDS HERE ##########

		return result;
	}
}
