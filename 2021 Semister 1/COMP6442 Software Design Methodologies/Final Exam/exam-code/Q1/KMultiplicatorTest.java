/**
 * These test cases are provided to assist your understanding.
 * But these test cases are not used in the actual marking.
 * Please DO NOT only rely on the given test cases for debugging.
 * You should write your own test cases to debug your code.
 */
 
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class KMultiplicatorTest {

	private KMultiplicator kmultiplicator;
	private final Multiplicator multiplicator = new Multiplicator();

	@Before
	public void init() {
		this.kmultiplicator = new KMultiplicator(new EmptyTracker());
	}

	@Test(timeout = 1000)
	public void Test1() {
		long x = 1;
		long y = 6;
		assertEquals(this.multiplicator.multiply(x, y), this.kmultiplicator.multiply(x, y));
	}

	@Test(timeout = 1000)
	public void Test2() {
		long x = 10;
		long y = 39;
		assertEquals(this.multiplicator.multiply(x, y), this.kmultiplicator.multiply(x, y));
	}

	@Test(timeout = 1000)
	public void Test3() {
		long x = 235;
		long y = 449;
		assertEquals(this.multiplicator.multiply(x, y), this.kmultiplicator.multiply(x, y));
	}


	@Test(timeout = 1000)
	public void Test4() {
		long x = 3530;
		long y = 4912;
		assertEquals(this.multiplicator.multiply(x, y), this.kmultiplicator.multiply(x, y));
	}
	
	@Test(timeout = 1000)
	public void Test5() {
		long x = 2345;
		long y = 52;
		assertEquals(this.multiplicator.multiply(x, y), this.kmultiplicator.multiply(x, y));
	}

	@Test(timeout = 1000)
	public void Test6() {
		long x = 212;
		long y = 20296;
		assertEquals(this.multiplicator.multiply(x, y), this.kmultiplicator.multiply(x, y));
	}
}
