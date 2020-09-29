package quinzical.impl.util.strategies.objectreader;

import quinzical.interfaces.strategies.objectreader.ObjectReaderStrategy;
import quinzical.interfaces.strategies.objectreader.ObjectReaderStrategyFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ObjectReaderStrategyFactoryImpl implements ObjectReaderStrategyFactory {
    public <T> ObjectReaderStrategy<T> createObjectReader() {
        return new DefaultObjectReaderStrategy<>();
    }
}

class DefaultObjectReaderStrategy<T> implements ObjectReaderStrategy<T> {

    @Override
    public T readObject(String dirname) throws IOException, ClassNotFoundException {

        FileInputStream fileIn = new FileInputStream(dirname);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        Object obj = in.readObject();
        in.close();
        fileIn.close();

        if (obj != null) {
            @SuppressWarnings("unchecked")
            T obj1 = (T) obj;
            return obj1;
        } else {
            return null;
        }
    }
}
