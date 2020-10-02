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

package quinzical.impl.util.strategies.boardloader;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.interfaces.factories.BoardComponentFactory;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.strategies.boardloader.BoardLoaderStrategy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Deprecated
public class DefaultBoardLoaderStrategy implements BoardLoaderStrategy {

    private final BoardComponentFactory boardComponentFactory;
    private final GameModel gameModel;

    private Pane header;
    private Pane content;

    /**
     * Construct the strategy with the component factory to create components, and the game model to get the questions
     * from.
     *
     * @param boardComponentFactory
     * @param gameModel
     */
    public DefaultBoardLoaderStrategy(BoardComponentFactory boardComponentFactory, GameModel gameModel) {
        this.boardComponentFactory = boardComponentFactory;
        this.gameModel = gameModel;
    }

    /**
     * Loads the category headers into the header pane.
     *
     * @param questionMap
     */
    private void loadHeader(Map<String, List<GameQuestion>> questionMap) {
        header.getChildren().clear();
        int categoryCount = questionMap.size();
        double paneWidth = header.getPrefWidth();
        double widthPerBox = paneWidth / categoryCount;

        List<String> categories = new ArrayList<>(questionMap.keySet());

        for (int i = 0; i < categoryCount; i++) {
            Label lbl = boardComponentFactory.createLabel();
            lbl.setPrefWidth(widthPerBox);
            lbl.setLayoutX(i * widthPerBox);
            lbl.setText(categories.get(i).toUpperCase());
            header.getChildren().add(lbl);
        }

    }

    /**
     * Loads the buttons into the content pane.
     *
     * @param questionMap
     */
    private void loadContent(Map<String, List<GameQuestion>> questionMap) {
        content.getChildren().clear();

        int categoryCount = questionMap.size();
        double paneWidth = content.getPrefWidth();
        double paneHeight = content.getPrefHeight();
        double widthPerBox = paneWidth / categoryCount;

        AtomicInteger i = new AtomicInteger();

        questionMap.forEach((cat, questionList) -> {
            VBox vbox = boardComponentFactory.createVbox();
            vbox.setPrefWidth(paneWidth);
            vbox.setLayoutX(i.get() * widthPerBox);
            content.getChildren().add(vbox);
            i.getAndIncrement();

            double heightPerBox = paneHeight / questionList.size();
            for (int j = 0; j < questionList.size(); j++) {
                GameQuestion ques = questionList.get(j);
                Button btn = boardComponentFactory.createButton();
                vbox.getChildren().add(btn);
                btn.setMinWidth(widthPerBox);
                btn.setMinHeight(heightPerBox);
                btn.setLayoutY(j * heightPerBox);
                btn.setText(ques.getValue() + "");

                if (!ques.isAnswerable()) {
                    btn.setDisable(true);
                    continue;
                }

                final int qIndex = j;
                btn.setOnAction(e -> {
                    //not implemented
                });
            }
        });
    }

    /**
     * Inject the components that the new components should be added to.
     */
    @Override
    public BoardLoaderStrategy injectComponents(Pane header, Pane content) {
        this.content = content;
        this.header = header;

        return this;
    }

    /**
     * Load the components.
     */
    @Override
    public void loadBoard() {

        Map<String, List<GameQuestion>> questionMap = new LinkedHashMap<>(gameModel.getBoardQuestions());

        loadHeader(questionMap);
        loadContent(questionMap);
    }
}
