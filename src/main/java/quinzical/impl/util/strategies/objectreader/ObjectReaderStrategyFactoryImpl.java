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

package quinzical.impl.util.strategies.objectreader;

import quinzical.interfaces.strategies.objectreader.ObjectReaderStrategy;
import quinzical.interfaces.strategies.objectreader.ObjectReaderStrategyFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Factory class for reading serialised objects.
 */
public class ObjectReaderStrategyFactoryImpl implements ObjectReaderStrategyFactory {
    /**
     * @param <T> The type of object being read. This is used as a cast.
     * @return the object reader strategy.
     */
    public <T> ObjectReaderStrategy<T> createObjectReader() {
        return new DefaultObjectReaderStrategy<>();
    }
}

/**
 * The default strategy for reading objects.
 *
 * @param <T> The object being read. The read object will be casted to this type.
 */
class DefaultObjectReaderStrategy<T> implements ObjectReaderStrategy<T> {

    /**
     * Read the object
     *
     * @param dirname the location of the object on disk.
     * @return the deserialized object, casted to type T
     * @throws IOException            If an error occurs with finding the file
     * @throws ClassNotFoundException If the class being casted to cannot be found.
     */
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
