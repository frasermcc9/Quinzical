package quinzical.impl.strategies.boardloader;

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

public class DefaultBoardLoaderStrategy implements BoardLoaderStrategy {

    private final BoardComponentFactory boardComponentFactory;

    private final GameModel gameModel;

    private Pane header;
    private Pane content;

    public DefaultBoardLoaderStrategy(BoardComponentFactory boardComponentFactory, GameModel gameModel) {
        this.boardComponentFactory = boardComponentFactory;
        this.gameModel = gameModel;
    }

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
                    gameModel.activateQuestion(ques);
                });
            }
        });
    }

    @Override
    public BoardLoaderStrategy injectComponents(Pane header, Pane content) {
        this.content = content;
        this.header = header;

        return this;
    }

    @Override
    public void loadBoard() {

        Map<String, List<GameQuestion>> questionMap = new LinkedHashMap<>(gameModel.getBoardQuestions());

        loadHeader(questionMap);
        loadContent(questionMap);
    }
}
