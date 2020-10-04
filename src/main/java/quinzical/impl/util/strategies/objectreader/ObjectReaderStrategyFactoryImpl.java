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

public class ObjectReaderStrategyFactoryImpl implements ObjectReaderStrategyFactory {
    public <T> ObjectReaderStrategy<T> createObjectReader() {
        return new DefaultObjectReaderStrategy<>();
    }
}

/**
 * Manages the creation of objects by reading a file.
 */
class DefaultObjectReaderStrategy<T> implements ObjectReaderStrategy<T> {

    /**
     * Reads the file from the specified directory and creates an object from it.
     * 
     * @param dirname -  the directory to read the file from
     * @return - the object that was created from the file
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
