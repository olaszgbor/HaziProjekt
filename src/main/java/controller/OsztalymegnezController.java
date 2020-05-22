package controller;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dao.OsztalyDao;
import dao.TanarDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Osztaly;
import model.Tanar;
import util.jpa.PersistenceModule;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OsztalymegnezController {
    /**
     * Egy osztály táblázatban tárolt adattagjai
     */
    @FXML
    private TableView<Osztaly> tableOsztaly;
    @FXML
    private TableColumn<Osztaly, String> columnAzon;
    @FXML
    private TableColumn<Osztaly, Integer> columnLetszam;
    @FXML
    private TableColumn<Osztaly, Tanar> columnOfo;
    @FXML
    private TableColumn<Osztaly, Integer> columnAktLetszam;
    @FXML
    private TextField szerkesztLetszamTextField;
    @FXML
    private ChoiceBox szerkesztOfoChoiceBox;

    private OsztalyDao osztalyDao;
    private TanarDao tanarDao;

    /**
     * A táblázat feltöltése az adatbázis adattagjaival
     */
    @FXML
    public void initialize() {
        Injector injector = Guice.createInjector(new PersistenceModule("jpa-persistence-unit-1"));
        osztalyDao = injector.getInstance(OsztalyDao.class);
        tanarDao = injector.getInstance(TanarDao.class);
        columnAzon.setCellValueFactory(new PropertyValueFactory<>("azon"));
        columnAktLetszam.setCellValueFactory(new PropertyValueFactory<>("aktualisLetszam"));
        columnLetszam.setCellValueFactory(new PropertyValueFactory<>("letszam"));
        columnOfo.setCellValueFactory(new PropertyValueFactory<>("ofo"));
        ObservableList<Osztaly> osztalyok = FXCollections.observableList(osztalyDao.findAll());
        tableOsztaly.setItems(osztalyok);
        columnOfo.setCellFactory(column -> {
            TableCell<Osztaly, Tanar> cell = new TableCell<Osztaly, Tanar>() {

                @Override
                protected void updateItem(Tanar item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        if (item != null) {
                            setText(item.getAzon() + " " + item.getNev());
                        }
                    }
                }
            };

            return cell;
        });

    }

    public void osztalyTorles(ActionEvent actionEvent) {
        if (tableOsztaly.getSelectionModel().getSelectedItem() != null) {
            Osztaly osztaly = tableOsztaly.getSelectionModel().getSelectedItem();
            osztaly.getOfo().setOsztaly(null);
            osztaly.setOfo(null);
            osztaly.getTanulok().stream().forEach(tanulo -> tanulo.setOsztaly(null));
            osztaly.setTanulok(null);
            osztalyDao.update(osztaly);
            osztalyDao.remove(osztaly);
            ObservableList<Osztaly> osztalyok = FXCollections.observableList(osztalyDao.findAll());
            tableOsztaly.setItems(osztalyok);
        }
    }

    public void osztalySzerkeszt(ActionEvent actionEvent) {
        if (tableOsztaly.getSelectionModel().getSelectedItem() != null) {
            Osztaly osztaly = tableOsztaly.getSelectionModel().getSelectedItem();
            szerkesztLetszamTextField.setText(String.valueOf(osztaly.getLetszam()));
            List<String> ofoazonok = osztalyDao.nemOfo().stream().map(Tanar::getAzon).collect(Collectors.toList());
            ofoazonok.add(osztaly.getOfo().getAzon());
            ObservableList<String> ofok = FXCollections.observableList(ofoazonok);
            szerkesztOfoChoiceBox.setItems(ofok);
            szerkesztOfoChoiceBox.setValue(osztaly.getOfo().getAzon());
        }
    }


    public void osztalyMent(ActionEvent actionEvent) {
        if (tableOsztaly.getSelectionModel().getSelectedItem() != null && szerkesztLetszamTextField.getText() != null && szerkesztOfoChoiceBox.getSelectionModel().getSelectedItem() != null) {
            Osztaly osztaly = tableOsztaly.getSelectionModel().getSelectedItem();
            osztaly.setLetszam(Integer.valueOf(szerkesztLetszamTextField.getText()));
            osztaly.setOfo(tanarDao.find(szerkesztOfoChoiceBox.getValue()).get());
            osztaly.getOfo().setOsztaly(osztaly);
            if(osztaly.letszamValid()){
                osztalyDao.update(osztaly);
                tableOsztaly.getItems().clear();
                ObservableList<Osztaly> osztalyok = FXCollections.observableList(osztalyDao.findAll());
                tableOsztaly.setItems(osztalyok);
            }
            szerkesztLetszamTextField.setText("");
            szerkesztOfoChoiceBox.setValue(null);
        }
    }
}
