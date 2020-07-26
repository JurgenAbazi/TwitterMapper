package filters.test;

import filters.Scanner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestScanner {
    @Test
    public void testScannerOnBasicInput() {
        Scanner x = new Scanner("trump");
        assertEquals("trump", x.peek());
        assertNull(x.advance());
    }

    @Test
    public void testScannerOnNotInput() {
        Scanner x = new Scanner("not good");
        assertEquals("not", x.peek());
        assertEquals("good", x.advance());
        assertEquals("good", x.peek());
        assertNull(x.advance());
    }

    @Test
    public void testScannerOnAndInput() {
        Scanner x = new Scanner("trump and evil");
        assertEquals("trump", x.peek());
        assertEquals("and", x.advance());
        assertEquals("and", x.peek());
        assertEquals("evil", x.advance());
        assertEquals("evil", x.peek());
        assertNull(x.advance());
    }

    @Test
    public void testScannerOnOrInput() {
        Scanner x = new Scanner("dusk or dawn");
        assertEquals("dusk", x.peek());
        assertEquals("or", x.advance());
        assertEquals("or", x.peek());
        assertEquals("dawn", x.advance());
        assertEquals("dawn", x.peek());
        assertNull(x.advance());
    }

    @Test
    public void runScannerTestOnOrInput() {
        String[] expected = { "trump", "or", "evil" };
        compareListToScannedInput("trump or evil", expected);
    }

    @Test
    public void runScannerTestOnCompositeInput() {
        String[] expected = { "trump", "and", "(", "evil", "or", "not", "(", "good", ")", ")" };
        compareListToScannedInput("trump and (evil or not (good))", expected);
    }

    private void compareListToScannedInput(String input, String[] expected) {
        Scanner x = new Scanner(input);
        boolean first = true;
        for (String token : expected) {
            if (first) {
                first = false;
            } else {
                assertEquals(x.advance(), token);
            }
            assertEquals(x.peek(), token);
        }
        assertNull(x.advance());
    }
}
