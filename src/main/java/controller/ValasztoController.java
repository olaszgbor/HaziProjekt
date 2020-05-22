package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.checkerframework.checker.units.qual.A;

import java.io.IOException;

public class ValasztoController {

    /**
     * A JavaFX-es felület részei
     */
    @FXML
    private Button valasztTanuloButton;
    @FXML
    private Button valasztTanarButton;
    @FXML
    private Button valasztOsztalyButton;
    private Stage tanuloStage;
    private Stage tanarStage;
    private Stage osztalyStage;

    /**
     * A JavaFX-es felületet betöltő függvény meghívása a külön gombok esetében másképp
     */
    public void tanuloMenuBetolt(ActionEvent actionEvent) throws IOException {
        menuBetolt(tanuloStage, "/fxml/Tanuloletreh.fxml");
    }

    public void tanarMenuBetolt(ActionEvent actionEvent) throws IOException {
        menuBetolt(tanarStage, "/fxml/Tanarletreh.fxml");
    }

    public void osztalyMenuBetolt(ActionEvent actionEvent) throws IOException {
        menuBetolt(osztalyStage, "/fxml/Osztalyletreh.fxml");
    }

    public void tanuloMenuMegnez(ActionEvent actionEvent) throws IOException {
        menuBetolt(tanuloStage, "/fxml/Tanulomegnez.fxml");
    }

    public void tanarMenuMegnez(ActionEvent actionEvent) throws IOException {
        menuBetolt(tanarStage, "/fxml/Tanarmegnez.fxml");
    }

    public void osztalyMenuMegnez(ActionEvent actionEvent) throws IOException {
        menuBetolt(osztalyStage, "/fxml/Osztalymegnez.fxml");
    }

    /**
     * A JavaFX felületet betöltő függvény
     */
    public void menuBetolt(Stage stage, String fxml) throws IOException {
        if (stage != null) {
            stage.close();
        }
        stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
        stage.toFront();
    }
}
