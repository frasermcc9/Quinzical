package quinzical.impl.util.strategies.objectreader;

import quinzical.interfaces.strategies.objectreader.ObjectReaderStrategy;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ObjectReaderStrategyFactory {
    public <T> void createStrategy() {
        new DefaultObjectReaderStrategy<T>();
    }
}

class DefaultObjectReaderStrategy<T> implements ObjectReaderStrategy<T> {

    @Override
    public T readObject() throws IOException, ClassNotFoundException {

        FileInputStream fileIn = new FileInputStream(System.getProperty("user.dir") + "/data/save.qdb");
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
