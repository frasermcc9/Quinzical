package quinzical.interfaces.strategies.boardloader;

import javafx.scene.layout.Pane;

public interface BoardLoaderStrategy {

    /**
     * Inject the components necessary to create the board.
     *
     * @param header  The header pane that will contain the category headings
     * @param content The content pane that will contain the buttons
     * @return this
     */
    BoardLoaderStrategy injectComponents(Pane header, Pane content);

    /**
     * Loads the components into the injected components. The
     * {@link BoardLoaderStrategy#injectComponents(Pane, Pane)} method is
     * required in order to use this method.
     */
    void loadBoard();

}
