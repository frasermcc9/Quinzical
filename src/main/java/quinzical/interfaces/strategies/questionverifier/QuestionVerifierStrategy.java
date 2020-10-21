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

package quinzical.interfaces.strategies.questionverifier;

import com.jfoenix.controls.JFXTextArea;
import quinzical.impl.util.questionparser.Solution;

import java.util.List;

/**
 * Interface for HintQuestionVerifier, DefaultQuestionVerifier and PracticeQuestionVerifier.
 * Manages the Verifying of solutions against some given text areas.
 */
public interface QuestionVerifierStrategy {
    List<Boolean> verifySolutions(List<Solution> solutions, List<JFXTextArea> textAreas);
}

