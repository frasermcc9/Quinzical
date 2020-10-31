package quinzical.impl.controllers.components;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import quinzical.impl.constants.Theme;
import quinzical.interfaces.models.GameModel;

public class StoreItem<ItemType> {

    @Inject
    GameModel gameModel;

    @FXML
    private VBox container;
    @FXML
    private Label lblType;
    @FXML
    private Label lblDescription;
    @FXML
    private JFXButton btnBuy;

    private int cost;
    private ItemType purchase;

    public int getCost() {
        return cost;
    }

    public ItemType getPurchase() {
        return purchase;
    }

    public void setData(final int cost, final ItemType purchase, final String typeName, final String itemDescription) {
        this.cost = cost;
        this.purchase = purchase;

        lblType.setText(typeName);
        lblDescription.setText(itemDescription);
        btnBuy.setText(cost + " Coins");

        if (cost > gameModel.getUserData().getCoins()) {
            btnBuy.setDisable(true);
        }

        if (purchase instanceof Theme) {
            container.getStyleClass().add(((Theme) purchase).name());
        }
    }

    public void disableBuy() {
        this.btnBuy.setDisable(true);
    }

    public void setBuyAction(final Runnable runnable) {
        btnBuy.setOnAction(a -> {
            runnable.run();
            gameModel.getUserData().incrementCoins(-cost);
        });
    }

    public void overrideButtonText(final String text) {
        btnBuy.setText(text);
    }


}
