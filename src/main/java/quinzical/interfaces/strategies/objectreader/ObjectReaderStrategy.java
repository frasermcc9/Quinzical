package quinzical.interfaces.strategies.objectreader;

import java.io.IOException;

public interface ObjectReaderStrategy<T> {
    T readObject(String dirname) throws IOException, ClassNotFoundException;
}
