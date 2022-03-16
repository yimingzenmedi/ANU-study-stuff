/**
 * SubExp: it is extended from the abstract class Exp. This class is used to
 * represent the expression of subtraction
 *
 * You are not required to implement any function inside this class. Please do
 * not change anything inside this class as well.
 */
public class SubExp extends Exp {

	private final Exp term;
	private final Exp exp;

	public SubExp(Exp term, Exp exp) {
		this.term = term;
		this.exp = exp;
	}

	@Override
	public String show() {
		return "(" + this.term.show() + " - " + this.exp.show() + ")";
	}

	@Override
	public int evaluate() {
		return this.term.evaluate() - this.exp.evaluate();
	}

	public Exp getTerm() {
		return term;
	}

	public Exp getExp() {
		return exp;
	}
}
