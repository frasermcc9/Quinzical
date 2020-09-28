package quinzical.impl.util.strategies.textnormaliser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TextNormaliserFactoryImplTest {
    private final TextNormaliserStrategyFactory textNormaliserFactory = new TextNormaliserStrategyFactory();

    @Test
    public void TestNormalizeInput1() {
        String str = "Māori";
        String newStr = textNormaliserFactory.getTextNormalizer().normaliseText(str);
        Assertions.assertEquals("maori", newStr);
    }

    @Test
    public void TestNormalizeInput2() {
        String str = "Katipō";
        String newStr = textNormaliserFactory.getTextNormalizer().normaliseText(str);
        Assertions.assertEquals("katipo", newStr);
    }

    @Test
    public void TestNormalizeInput3() {
        String str = "Pūhā";
        String newStr = textNormaliserFactory.getTextNormalizer().normaliseText(str);
        Assertions.assertEquals("puha", newStr);
    }
}
