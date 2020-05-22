package controller;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dao.OsztalyDao;
import dao.TanarDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.Osztaly;
import model.Tanar;
import util.jpa.PersistenceModule;

import java.util.stream.Collectors;


public class OsztalyletrehController {

    /**
     * A JavaFX-es felület részei
     */
    @FXML
    private TextField azonTextfield;
    @FXML
    private TextField letszamTextfield;
    @FXML
    private ChoiceBox ofoChoiceBox;

    private OsztalyDao osztalyDao;
    private TanarDao tanarDao;


    /**
     * Hozzáfér az adatbázishoz
     * Feltölti a lehetséges osztályfőnökökkel a listát a kiválasztáshoz
     */
    @FXML
    public void initialize(){
        Injector injector = Guice.createInjector(new PersistenceModule("jpa-persistence-unit-1"));
        osztalyDao = injector.getInstance(OsztalyDao.class);
        tanarDao = injector.getInstance(TanarDao.class);
        ObservableList<String> tanarok = FXCollections.observableList(tanarDao.nemOfo().stream().map(Tanar::getAzon).collect(Collectors.toList()));
        ofoChoiceBox.setItems(tanarok);
    }

    /**
     * Az osztály létrehozása az adatbázisba
     */
    public void osztalyLetrehozas(ActionEvent actionEvent) {
        if (azonTextfield.getText() != null && letszamTextfield.getText() != null && ofoChoiceBox.getSelectionModel().getSelectedItem() != null ) {

            Osztaly osztaly = Osztaly.builder()
                    .azon(azonTextfield.getText())
                    .aktualisLetszam(0)
                    .letszam(Integer.valueOf(letszamTextfield.getText()))
                    .ofo(tanarDao.find(ofoChoiceBox.getSelectionModel().getSelectedItem()).get())
                    .build();
            if(osztalyDao.find(osztaly.getAzon()).isEmpty()) {
                osztalyDao.persist(osztaly);
            }
        }
    }
}
