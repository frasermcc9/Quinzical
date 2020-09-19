package quinzical.interfaces.strategies.boardloader;

import javafx.scene.layout.Pane;

public interface BoardLoaderStrategy {

    BoardLoaderStrategy injectComponents(Pane header, Pane content);

    void loadBoard();

}
