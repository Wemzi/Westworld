import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class firstTest {
    @Test
    public void testOne(){
        int i=1;
        int j=i;
        assertEquals(1, j);
    }
}
