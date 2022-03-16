/**
 * These test cases are provided to assist your understanding.
 * But these test cases are not used in the actual marking.
 * Please DO NOT only rely on the given test cases for debugging.
 * You should write your own test cases to debug your code.
 */

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ExponentiatorTest {

	private Exponentiator exponentiator;

	@Before
	public void init() {
		this.exponentiator = new Exponentiator(new EmptyTracker());
	}

	@Test(timeout = 1000)
	public void TestWithExponentEqualToOne() throws Exception {
		long base = 5;
		long power = 1;
		assertEquals((long) Math.pow(base, power), this.exponentiator.exponentiate(base, power));
	}

	// Computing 0^0 is undefined. So it should throw
	// IllegalExponentiationException.
	@Test(timeout = 1000, expected = IllegalExponentiationException.class)
	public void TestWithBaseExponentEqualToZero() throws Exception {
		this.exponentiator.exponentiate(0, 0);
	}

	@Test(timeout = 1000)
	public void TestWithExponentEqualToZero() throws Exception {
		long base = 18;
		long power = 0;
		assertEquals((long) Math.pow(base, power), this.exponentiator.exponentiate(base, power));
	}

	@Test(timeout = 1000)
	public void TestWithEvenExponent() throws Exception {
		long base = 3;
		long power = 8;
		assertEquals((long) Math.pow(base, power), this.exponentiator.exponentiate(base, power));
	}

	@Test(timeout = 1000)
	public void TestWithOddExponent() throws Exception {
		long base = 2;
		long power = 7;
		assertEquals((long) Math.pow(base, power), this.exponentiator.exponentiate(base, power));
	}

	@Test(timeout = 1000)
	public void TestWithKMultiplicator() throws Exception {
		long base = 7;
		long power = 8;
		this.exponentiator.setMultiplicator(new KMultiplicator(new EmptyTracker()));
		assertEquals((long) Math.pow(base, power), this.exponentiator.exponentiate(base, power));
	}
}
