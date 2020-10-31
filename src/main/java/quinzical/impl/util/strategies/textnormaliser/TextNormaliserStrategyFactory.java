// Copyright 2020 Fraser McCallum and Braden Palmer
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//  
//     http://www.apache.org/licenses/LICENSE-2.0
//  
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package quinzical.impl.util.strategies.textnormaliser;

import quinzical.interfaces.strategies.textnormaliser.TextNormaliserFactory;
import quinzical.interfaces.strategies.textnormaliser.TextNormaliserStrategy;

import java.text.Normalizer;

/**
 * Manages Normalisation of text, converting capitals to lower case, trimming leading
 * and trailing spaces and trimming starting "the" words.
 */
public class TextNormaliserStrategyFactory implements TextNormaliserFactory {

    /**
     * Gets a TextNormalizer to be used.
     */
    @Override
    public TextNormaliserStrategy getTextNormalizer() {
        return new MacronRequiredNormaliser();
    }
}

/**
 * A Text normalizer that removes any macrons in the text
 */
class DefaultTextNormaliser implements TextNormaliserStrategy {
    @Override
    public String normaliseText(final String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll("\\p{M}", "").trim().toLowerCase().replaceFirst("^the", "").trim();
    }
}

/**
 * A Text normalizer when macrons are required, keeping all macrons in the text 
 * as opposed to removing them.
 */
class MacronRequiredNormaliser implements TextNormaliserStrategy {
    @Override
    public String normaliseText(final String input) {
        return input.toLowerCase().replaceFirst("^the", "").trim();
    }
}
