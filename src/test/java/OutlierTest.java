import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.runners.JUnit4;

public class OutlierTest{
    @Test
    public void testExample() {
        int[] exampleTest1 = {2,6,8,-10,3};
        int[] exampleTest2 = {206847684,1056521,7,17,1901,21104421,7,1,35521,1,7781};
        int[] exampleTest3 = {Integer.MAX_VALUE, 0, 1};
        int[] exampleTest4 = {2,6,8,-11,10};
        int[] exampleTest5 = {3,7,9,11,-44};
        int[] exampleTest6 = {-44,3,7,9,11};
        int[] exampleTest7 = {1,1,1,1,1,44,7,7,7,7,7,7,7,7};
        int[] exampleTest8 = {1,1,-1,1,1,-44,7,7,7,7,7,7,7,7};
        assertEquals(3, FindOutlier.find(exampleTest1));
        assertEquals(206847684, FindOutlier.find(exampleTest2));
        assertEquals(0, FindOutlier.find(exampleTest3));
        assertEquals(-11, FindOutlier.find(exampleTest4));
        assertEquals(-44, FindOutlier.find(exampleTest5));
        assertEquals(-44, FindOutlier.find(exampleTest6));
        assertEquals(44, FindOutlier.find(exampleTest7));
        assertEquals(-44, FindOutlier.find(exampleTest8));
    }}
     