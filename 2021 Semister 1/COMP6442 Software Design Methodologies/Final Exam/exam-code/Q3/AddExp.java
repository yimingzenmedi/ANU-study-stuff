import java.util.Arrays;
import java.util.List;

/**
 * AddExp: it is extended from the abstract class Exp. This class is used to
 * represent the expression of addition
 *
 * You are not required to implement any function inside this class. Please do
 * not change anything inside this class as well.
 */
public class AddExp extends Exp {

	private final Exp child1;
	private final Exp child2;

	public AddExp(Exp child1, Exp child2) {
		super(ExpType.AddExp);
		this.child1 = child1;
		this.child2 = child2;
	}
	
	@Override
	public List<Exp> getSubExps() {
		return Arrays.asList(this.child1, this.child2);
	}
}