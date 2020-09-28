package quinzical.interfaces.strategies.objectreader;

public interface ObjectReaderStrategyFactory {
    <T> ObjectReaderStrategy<T> createObjectReader();
}
