import com.kpi.epicycloid.Logic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SinCosTest {
    private int k = 20;

    @Test
    public void testSin() {
        for (double i=0; i<Math.PI*4; i+=Math.PI*4/20) {
            Assertions.assertEquals(Math.sin(i), Logic.sin(k, i), 0.01);
        }
    }

    @Test
    public void testCos() {
        for (double i=0; i<Math.PI*4; i+=Math.PI*4/20) {
            System.out.println(i);
            Assertions.assertEquals(Math.cos(i), Logic.cos(k, i), 0.01);
        }
    }

}
