package quinzical.impl.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GameQuestionControllerTest {
    @Test
    public void TestNormalizeInput1() {
        String str = "Māori";
        String newStr = new GameQuestionController().normaliseText(str);
        Assertions.assertEquals("maori", newStr);
    }    
    
    @Test
    public void TestNormalizeInput2() {
        String str = "Katipō";
        String newStr = new GameQuestionController().normaliseText(str);
        Assertions.assertEquals("katipo", newStr);
    }

    @Test
    public void TestNormalizeInput3() {
        String str = "Pūhā";
        String newStr = new GameQuestionController().normaliseText(str);
        Assertions.assertEquals("puha", newStr);
    }
}
