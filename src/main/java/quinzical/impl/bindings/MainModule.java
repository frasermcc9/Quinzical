package quinzical.impl.bindings;

import com.google.inject.AbstractModule;
import quinzical.Test;
import quinzical.impl.models.QuestionCollectionImpl;
import quinzical.interfaces.models.QuestionCollection;

public class MainModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(QuestionCollection.class).to(QuestionCollectionImpl.class);
    }
}
