import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 * You are given the java class called PathComplete, which has a method
 * called findSomething. Implement the minimum number of JUnit test cases to achieve 
 * path complete to the findSomething method. Write your test case(s) in the test() method
 * in the PathCompleteTest.java file. All test cases should pass the JUnit test. You are not
 * allowed to alter the signatures of the given methods and the package
 * structures of the given classes. Please upload ONLY the PathCompleteTest.java file to
 * Wattle for marking.
 *
 */
public class PathCompleteTest {

	@Test
	public void test() {
		// Implement your test cases
		// START YOUR CODE

		int a1 = 1, b1 = 2, c1 = 3;
		int res1 = PathComplete.findSomething(a1, b1, c1);
		int a2 = 1, b2 = 3, c2 = 2;
		int res2 = PathComplete.findSomething(a2, b2, c2);
		int a3 = 2, b3 = 1, c3 = 3;
		int res3 = PathComplete.findSomething(a3, b3, c3);
		int a4 = 3, b4 = 1, c4 = 2;
		int res4 = PathComplete.findSomething(a4, b4, c4);

		assertEquals(res1, c1);
		assertEquals(res2, b2);
		assertEquals(res3, c3);
		assertEquals(res4, a4);

		// END YOUR CODE
	}
}
