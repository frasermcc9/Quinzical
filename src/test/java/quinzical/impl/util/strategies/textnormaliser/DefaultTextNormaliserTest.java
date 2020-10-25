package quinzical.impl.util.strategies.textnormaliser;


import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DefaultTextNormaliserTest {
    private final TextNormaliserStrategyFactory textNormaliserFactory = new TextNormaliserStrategyFactory();

    @Test
    public void TestNormalizeInput1() {
        String str = "Māori";
        String newStr = textNormaliserFactory.getTextNormalizer().normaliseText(str);
        assertEquals("māori", newStr);
    }

    @Test
    public void TestNormalizeInput2() {
        String str = "Katipō";
        String newStr = textNormaliserFactory.getTextNormalizer().normaliseText(str);
        assertEquals("katipō", newStr);
    }

    @Test
    public void TestNormalizeInput3() {
        String str = "Pūhā";
        String newStr = textNormaliserFactory.getTextNormalizer().normaliseText(str);
        assertEquals("pūhā", newStr);
    }
}
