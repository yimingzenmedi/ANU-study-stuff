/**
 * IntExp: it is extended from the abstract class Exp. This class is used to
 * represented the expression of 32-bit unsigned integer
 *
 * You are not required to implement any function inside this class. Please do
 * not change any thing inside this class as well.
 */
public class IntExp extends Exp {

	private final int value;

	public IntExp(int value) {
		this.value = value;
	}

	@Override
	public String show() {
		return String.valueOf(this.value);
	}

	@Override
	public int evaluate() {
		return value;
	}

	public int getValue() {
		return value;
	}
}
