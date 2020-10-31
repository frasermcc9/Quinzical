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

package quinzical.impl.util.questionparser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a solution to a question, contains all the variants to the solution 
 */
public class Solution implements Serializable {

    private static final long serialVersionUID = 1L;
    private final List<String> variants = new ArrayList<>();

    /**
     * Creates a new Solution with the given solution array
     * 
     * @param solutions - an array containing the variants to the solution 
     */
    public Solution(final String[] solutions) {
        for (String s : solutions) {
            s = s.trim();
            variants.add(s);
        }
    }

    /**
     * Creates a new Solution with the given solution array
     *
     * @param solutions - A list containing the variants to the solution 
     */
    public Solution(final List<String> solutions) {
        for (String s : solutions) {
            s = s.trim();
            variants.add(s);
        }
    }

    /**
     * Gets the a copy of the variants of this solution object.
     */
    public List<String> getVariants() {
        return new ArrayList<>(variants);
    }
}
