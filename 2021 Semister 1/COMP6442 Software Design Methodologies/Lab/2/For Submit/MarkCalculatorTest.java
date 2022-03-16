/**
 * Sample code with JUnit 4 for the parameterized test
 *
 */

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MarkCalculatorTest {
	/**
	 * Return a list of parameters which are different implementation of
	 * interface {@linkplain MarkCalculator}.
	 * Do NOT Modify this part
	*/
	@Parameters
    public static Iterable<? extends Object> getImplementations() {
        return Arrays.asList(
                new MarkCalculator0(),
                new MarkCalculator1(),
                new MarkCalculator2(),
                new MarkCalculator3(),
                new MarkCalculator4(),
                new MarkCalculator5(),
                new MarkCalculator6(),
                new MarkCalculator7(),
                new MarkCalculator8(),
                new MarkCalculator9()
        );
    }

	@Parameter
	public MarkCalculator calculator;


	// ########## YOUR CODE STARTS HERE ##########

	/* EXAMPLE Test case 1 */
	@Test(expected = ComponentOutOfRangeException.class)
	public void testException() throws ComponentOutOfRangeException {
		this.calculator.calculateMark(-1, 0, 0, 0, true);
	}

	/* EXAMPLE Test case 2 */
	@Test
	public void testGradeN() throws ComponentOutOfRangeException {
		assertEquals(new MarkGrade(0, Grade.N), this.calculator.calculateMark(0, 0, 0, 0, true));
	}

	//TODO: write other test cases

    @Test(expected = ComponentOutOfRangeException.class)
    public void testExceptionLabNegativeMark() throws ComponentOutOfRangeException {
        this.calculator.calculateMark(-1, 0, 0, 0, true);
    }
    @Test(expected =    ComponentOutOfRangeException.class)
    public void testExceptionLabMoreThanTotal() throws ComponentOutOfRangeException {
        this.calculator.calculateMark(11, 0, 0, 0, true);
    }

    @Test(expected = ComponentOutOfRangeException.class)
    public void testExceptionAssignment1NegativeMark() throws ComponentOutOfRangeException {
        this.calculator.calculateMark(0, -1, 0, 0, true);
    }
    @Test(expected = ComponentOutOfRangeException.class)
    public void testExceptionAssignment1MoreThanTotal() throws ComponentOutOfRangeException {
        this.calculator.calculateMark(0, 11, 0, 0, true);
    }

    @Test(expected = ComponentOutOfRangeException.class)
    public void testExceptionAssignment2NegativeMark() throws ComponentOutOfRangeException {
        this.calculator.calculateMark(0, 0, -1, 0, true);
    }
    @Test(expected = ComponentOutOfRangeException.class)
    public void testExceptionAssignment2MoreThanTotal() throws ComponentOutOfRangeException {
        this.calculator.calculateMark(0, 0, 11, 0, true);
    }

    @Test(expected = ComponentOutOfRangeException.class)
    public void testExceptionFinalExamNegativeMark() throws ComponentOutOfRangeException {
        this.calculator.calculateMark(0, 0, 0, -1, true);
    }
    @Test(expected = ComponentOutOfRangeException.class)
    public void testExceptionFinalExamMoreThanTotal() throws ComponentOutOfRangeException {
        this.calculator.calculateMark(0, 0, 0, 101, true);
    }

    @Test
    public void testNotAttendFinalExam() throws ComponentOutOfRangeException {
        assertEquals(new MarkGrade(null, Grade.NCN), this.calculator.calculateMark(10, 10, 10, 0, false));
    }

    @Test
    public void testRoundingUp() throws ComponentOutOfRangeException {
        assertEquals(new MarkGrade(2, Grade.N), this.calculator.calculateMark(0, 1, 0, 0, true));
    }
    @Test
    public void testRoundingDown() throws ComponentOutOfRangeException {
        assertEquals(new MarkGrade(66, Grade.C), this.calculator.calculateMark(10, 10, 10, 44, true));
    }

    @Test
    public void testGet0FinalMark() throws ComponentOutOfRangeException {
        assertEquals(new MarkGrade(0, Grade.N), this.calculator.calculateMark(0, 0, 0, 0, true));
    }
    @Test
    public void testGetGradeN() throws ComponentOutOfRangeException {
        assertEquals(new MarkGrade(25, Grade.N), this.calculator.calculateMark(10, 0, 10, 0, true));
    }
    @Test
    public void testGet44FinalMarkGradeN() throws ComponentOutOfRangeException {
        assertEquals(new MarkGrade(44, Grade.N), this.calculator.calculateMark(2, 0, 0, 70, true));
    }

    @Test
    public void testGet45FinalMarkGradePX() throws ComponentOutOfRangeException {
        assertEquals(new MarkGrade(45, Grade.PX), this.calculator.calculateMark(3, 0, 0, 70, true));
    }
    @Test
    public void testGetGradePX() throws ComponentOutOfRangeException {
        assertEquals(new MarkGrade(47, Grade.PX), this.calculator.calculateMark(5, 0, 0, 70, true));
    }
    @Test
    public void testGet49FinalMarkGradePX() throws ComponentOutOfRangeException {
        assertEquals(new MarkGrade(49, Grade.PX), this.calculator.calculateMark(7, 0, 0, 70, true));
    }

    @Test
    public void testGet50FinalMarkGradeP() throws ComponentOutOfRangeException {
        assertEquals(new MarkGrade(50, Grade.P), this.calculator.calculateMark(2, 7, 8, 43, true));
    }
    @Test
    public void testGetGradeP() throws ComponentOutOfRangeException {
        assertEquals(new MarkGrade(52, Grade.P), this.calculator.calculateMark(10, 0, 0, 70, true));
    }
    @Test
    public void testGet59FinalMarkGradeP() throws ComponentOutOfRangeException {
        assertEquals(new MarkGrade(59, Grade.P), this.calculator.calculateMark(2, 10, 0, 70, true));
    }

    @Test
    public void testGet60FinalMarkGradeC() throws ComponentOutOfRangeException {
        assertEquals(new MarkGrade(60, Grade.C), this.calculator.calculateMark(3, 10, 0, 70, true));
    }
    @Test
    public void testGetGradeC() throws ComponentOutOfRangeException {
        assertEquals(new MarkGrade(61, Grade.C), this.calculator.calculateMark(4, 10, 0, 70, true));
    }
    @Test
    public void testGet69FinalMarkGradeC() throws ComponentOutOfRangeException {
        assertEquals(new MarkGrade(69, Grade.C), this.calculator.calculateMark(0, 10, 0, 90, true));
    }

    @Test
    public void testGet70FinalMarkGradeD() throws ComponentOutOfRangeException {
        assertEquals(new MarkGrade(70, Grade.D), this.calculator.calculateMark(1, 10, 0, 90, true));
    }
    @Test
    public void testGetGradeD() throws ComponentOutOfRangeException {
        assertEquals(new MarkGrade(72, Grade.D), this.calculator.calculateMark(3, 10, 0, 90, true));
    }
    @Test
    public void testGet79FinalMarkGradeD() throws ComponentOutOfRangeException {
        assertEquals(new MarkGrade(79, Grade.D), this.calculator.calculateMark(10, 10, 0, 90, true));
    }

    @Test
    public void testGet80FinalMarkGradeHD() throws ComponentOutOfRangeException {
        assertEquals(new MarkGrade(80, Grade.HD), this.calculator.calculateMark(5, 10, 0, 100, true));
    }
    @Test
    public void testGetGradeHD() throws ComponentOutOfRangeException {
        assertEquals(new MarkGrade(90, Grade.HD), this.calculator.calculateMark(0, 10, 10, 100, true));
    }
    @Test
    public void testGet100FinalMarkGradeHD() throws ComponentOutOfRangeException {
        assertEquals(new MarkGrade(100, Grade.HD), this.calculator.calculateMark(10, 10, 10, 100, true));
    }


    @Test
    public void testSpecialValue01() throws ComponentOutOfRangeException {
        assertEquals(new MarkGrade(59, Grade.P), this.calculator.calculateMark(0, 0, 1, 95, true));
    }

	// ########## YOUR CODE ENDS HERE ##########
}
