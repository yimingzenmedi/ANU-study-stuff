//package utils;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * TODO: write a minimum number of JUnit test cases (assertEquals) for
 * {@code MyUtil.parseDouble} that is code complete.
 * 
 * 
 *
 */
public class MyUtilTest {

	@Test
	public void test() {
		//start your code
		System.out.println("\n==============");
		assertTrue(0 == MyUtil.parseDouble(null));

		System.out.println("\n==============");
		assertTrue(111.123 == MyUtil.parseDouble("null111.123"));

		System.out.println("\n==============");
		assertTrue(0 == MyUtil.parseDouble("nullsfasf"));

		//end your code
	}

}
