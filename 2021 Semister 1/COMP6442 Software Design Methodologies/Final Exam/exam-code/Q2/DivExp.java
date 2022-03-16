/**
 * DivExp: it is extended from the abstract class Exp. This class is used to
 * represent the expression of division
 *
 * You are not required to implement any function inside this class. Please do
 * not change anything inside this class as well.
 */
public class DivExp extends Exp {

	private final Exp factor;
	private final Exp term;

	public DivExp(Exp factor, Exp term) {
		this.factor = factor;
		this.term = term;
	}

	@Override
	public String show() {
		return "(" + this.factor.show() + " / " + this.term.show() + ")";
	}

	@Override
	public int evaluate() {
		return this.factor.evaluate() / this.term.evaluate();
	}

	public Exp getFactor() {
		return factor;
	}

	public Exp getTerm() {
		return term;
	}
}