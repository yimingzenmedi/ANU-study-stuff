/**
 * ExpoExp: it is extended from the abstract class Exp, This class is used to
 * represent the expression of exponentiation
 *
 * You are not required to implement any function inside this class. Please do
 * not change anything inside this class as well.
 */
public class ExpoExp extends Exp {

	private final Exp base;
	private final Exp power;

	/**	 
	 * @param input base
	 * @param input expo
	 */
	public ExpoExp(Exp base, Exp power) {
		this.base = base;
		this.power = power;
	}

	@Override
	public String show() {
		return "(" + this.base.show() + " ^ " + this.power.show() + ")";
	}

	@Override
	public int evaluate() {
		return (int) Math.pow(this.base.evaluate(), this.power.evaluate());
	}

	public Exp getBase() {
		return base;
	}

	public Exp getPower() {
		return power;
	}
}