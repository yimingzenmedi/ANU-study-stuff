/**
 * These test cases are provided to assist your implementation.
 * However, note that these test cases may not be used in actual marking.
 * You are encouraged to write your own test case to test the correctness
 * of your implementation.
 */
 

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EditDistanceTest {

    @Test(timeout=1000)
    public void Test1() {
        String str1 = "ACGT";
        String str2 = "ACGT";
        assertEquals(0,EditDistance.minDistance(str1,str2));
    }

    @Test(timeout=1000)
    public void Test2() {
        String str1 = "ACAGTCGGT";
        String str2 = "AGGTCGACT";
        assertEquals(5,EditDistance.minDistance(str1,str2));
    }

    @Test(timeout=1000)
    public void Test3() {
        String str1 = "ACCCCGGGT";
        String str2 = "ATTTCCA";
        assertEquals(10,EditDistance.minDistance(str1,str2));
    }

    @Test(timeout=1000)
    public void Test4() {
        String str1 = "GGGG";
        String str2 = "ACGGT";
        assertEquals(5,EditDistance.minDistance(str1,str2));
    }

    @Test(timeout=1000)
    public void Test5() {
        String str1 = "AAAACCCCTTTTGGGG";
        String str2 = "ACTGACTGACTGACTG";
        assertEquals(16,EditDistance.minDistance(str1,str2));
    }

    @Test(timeout=1000)
    public void Test6() {
        String str1 = "ACGTTTT";
        String str2 = "ACTGGTG";
        assertEquals(9, EditDistance.minDistance(str1,str2));
    }

    @Test(timeout=1000)
    public void Test7() {
        String str1 = "AAAAGTCATA";
        String str2 = "CCCCATCATTACA";
        assertEquals(12, EditDistance.minDistance(str1,str2));
    }

    @Test(timeout=1000)
    public void Test8() {
        String str1 = "ACGT";
        String str2 = "A";
        assertEquals(4, EditDistance.minDistance(str1,str2));
    }

    @Test(timeout=1000)
    public void Test9() {
        String str1 = "TTGG";
        String str2 = "CGCTGA";
        assertEquals(7, EditDistance.minDistance(str1,str2));
    }

    @Test(timeout=1000)
    public void Test10() {
        String str1 = "CGT";
        String str2 = "CGT";
        assertEquals(0, EditDistance.minDistance(str1,str2));
    }

//    @Test(timeout=1000)
//    public void Test11() {
//        String str1 = "ACGTTTT";
//        String str2 = "ACTGGTG";
//        assertEquals(6, EditDistance.minDistance(str1,str2));
//    }
//
//    @Test(timeout=1000)
//    public void Test6() {
//        String str1 = "ACGTTTT";
//        String str2 = "ACTGGTG";
//        assertEquals(9, EditDistance.minDistance(str1,str2));
//    }
//
//    @Test(timeout=1000)
//    public void Test6() {
//        String str1 = "ACGTTTT";
//        String str2 = "ACTGGTG";
//        assertEquals(9, EditDistance.minDistance(str1,str2));
//    }
}

