import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * You are given a java class called Something, which has a method called
 * someMethod. Please implement a minimum number of test cases for testing `someMethod`
 * that are branch complete within `someMethod`. Write your test case(s) in test() method in
 * `SomethingTest.java`. You are not allowed to alter the signatures of the given
 * methods and the package structures of the given classes. Please upload the
 * `SomethingTest.java` file to Wattle for marking.
 *
 */
public class SomethingTest {

	@Test
	public void test() {
		// Implement your test cases
		// START YOUR CODE

		System.out.println("=====================");
		assertEquals(47, Something.someMethod(10, 2, 31, 16));

		System.out.println("=====================");
		assertEquals(108, Something.someMethod(36, 72, 2, 10));

		System.out.println("=====================");
		assertEquals(180, Something.someMethod(36, 72, 72, 36));
		// END YOUR CODE
	}
}
