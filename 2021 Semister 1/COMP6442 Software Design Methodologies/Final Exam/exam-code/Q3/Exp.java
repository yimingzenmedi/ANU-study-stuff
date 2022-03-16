import java.util.List;

/**
 * Abstract class Exp to represent expressions
 *
 * You are not required to implement any function inside this class. Please do
 * not change anything inside this class as well.
 */
public abstract class Exp {
	
	protected final ExpType type;
	
	public Exp(ExpType type) {
		this.type = type;
	}
	
	public abstract List<Exp> getSubExps();

	public ExpType getType() {
		return type;
	}
}
