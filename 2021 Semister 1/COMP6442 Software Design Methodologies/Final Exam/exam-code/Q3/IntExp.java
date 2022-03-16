import java.util.Collections;
import java.util.List;

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
		super(ExpType.IntExp);
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public List<Exp> getSubExps() {
		return Collections.emptyList();
	}
}
