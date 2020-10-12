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

package quinzical.interfaces.strategies.textnormaliser;

/**
 * Interface for DefaultTextNormaliser and MacronRequiredNormaliser, which manage
 * the simplification/ normalising of text by removing trailing/leading spaces, making
 * the text all one case and similar actions.
 */
public interface TextNormaliserStrategy {
    String normaliseText(String input);
}
