package quinzical.impl.util.strategies.textnormaliser;

import quinzical.interfaces.strategies.textnormaliser.TextNormaliserFactory;
import quinzical.interfaces.strategies.textnormaliser.TextNormaliserStrategy;

import java.text.Normalizer;

public class TextNormaliserStrategyFactory implements TextNormaliserFactory {
    @Override
    public TextNormaliserStrategy getTextNormalizer() {
        return new DefaultTextNormaliser();
    }
}

class DefaultTextNormaliser implements TextNormaliserStrategy {
    @Override
    public String normaliseText(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll("\\p{M}", "").trim().toLowerCase().replaceFirst("^the", "").trim();
    }
}