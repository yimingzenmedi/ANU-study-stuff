public class IllegalExponentiationException extends Exception {

	private static final long serialVersionUID = 1L;

	public IllegalExponentiationException() {
		super("The base and the power cannot be zero at the same time!");
	}
}
