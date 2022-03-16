import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ConcatenateTest {
	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {{0.7f, 0.3f, 1}, {0.4f, 0.6f, 1}});
	}

	@Parameterized.Parameter(0)
	public float a;

	@Parameterized.Parameter(1)
	public float b;

	@Parameterized.Parameter(2)
	public int expected;
	
	@Test
	public void testConcatenate() {
		String result = Concatenate.concatenate("one", "two");

        assertEquals("onetwo", result);
	}

	@Test
	public void testParameters() {
		assertEquals(expected, a + b, 0.000001);
	}

}
