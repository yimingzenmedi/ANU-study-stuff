//package utils;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * TODO: write a minimum number of JUnit test cases (assertEquals) for
 * {@code MyStringUtil.isMixedCase} that is branch complete.
 */
public class MyStringUtilTest {

	@Test
	public void test() {
		//start your code
		
		System.out.println("\n1 ===============");
		assertTrue(!MyStringUtil.isMixedCase(null));

		System.out.println("\n2 ===============");
		assertTrue(!MyStringUtil.isMixedCase(""));

		System.out.println("\n3 ===============");
		assertTrue(MyStringUtil.isMixedCase("abcDEF"));

		System.out.println("\n4 ===============");
		assertTrue(!MyStringUtil.isMixedCase("ABCD"));

		//end your code
	}

}
