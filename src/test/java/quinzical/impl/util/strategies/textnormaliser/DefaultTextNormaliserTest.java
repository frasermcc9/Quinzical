package quinzical.impl.util.strategies.textnormaliser;


import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DefaultTextNormaliserTest {
    private final TextNormaliserStrategyFactory textNormaliserFactory = new TextNormaliserStrategyFactory();

    @Test
    public void TestNormalizeInput1() {
        final String str = "Māori";
        final String newStr = textNormaliserFactory.getTextNormalizer().normaliseText(str);
        assertEquals("māori", newStr);
    }

    @Test
    public void TestNormalizeInput2() {
        final String str = "Katipō";
        final String newStr = textNormaliserFactory.getTextNormalizer().normaliseText(str);
        assertEquals("katipō", newStr);
    }

    @Test
    public void TestNormalizeInput3() {
        final String str = "Pūhā";
        final String newStr = textNormaliserFactory.getTextNormalizer().normaliseText(str);
        assertEquals("pūhā", newStr);
    }
}
