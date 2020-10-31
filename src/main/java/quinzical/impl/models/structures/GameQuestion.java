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

package quinzical.impl.models.structures;

import quinzical.impl.util.questionparser.Question;

/**
 * Extension of question that adds functionality to make them useful for the game. Adds support for checking if the
 * question is answered, answerable, and its value.
 */
public class GameQuestion extends Question {
    private static final long serialVersionUID = 1L;

    private boolean answered = false;
    private boolean answerable = false;
    private int value = 0;
    private boolean correct = false;

    /**
     * Game questions are constructed by passing the normal question into the constructor.
     *
     * @param question the question to transform into a game question.
     */
    public GameQuestion(final Question question) {
        super(question);
    }

    public void answer(final boolean correct) {
        this.answerable = false;
        this.answered = true;
        this.correct = correct;
    }

    public boolean isCorrect() {
        return this.correct;
    }

    public int getValue() {
        return value;
    }

    public void setValue(final int value) {
        this.value = value;
    }

    public boolean isAnswerable() {
        return answerable;
    }

    public void setAnswerable(final boolean answerable) {
        this.answerable = answerable;
    }

    public boolean isAnswered() {
        return answered;
    }
    
    public void setAnswered(final boolean answered) {
        this.answered = answered;
    }

}
