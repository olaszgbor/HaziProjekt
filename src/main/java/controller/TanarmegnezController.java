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
import lombok.extern.slf4j.Slf4j;
import model.Osztaly;
import model.Tanar;
import model.Tanulo;
import util.jpa.PersistenceModule;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
public class TanarmegnezController {
    /**
     * A tanárok táblázatban tárolt adattagjai
     */
    @FXML
    private TableView<Tanar> tableTanar;
    @FXML
    private TableColumn<Tanar, String> columnNev;
    @FXML
    private TableColumn<Tanar, Integer> columnKor;
    @FXML
    private TableColumn<Tanar, String> columnAzon;
    @FXML
    private TableColumn<Tanar, Date> columnSzul;
    @FXML
    private TableColumn<Tanar, Osztaly> columnOsztaly;
    @FXML
    private TextField szerkesztNevTextField;
    @FXML
    private TextField szerkesztKorTextField;

    @FXML
    private DatePicker szerkesztSzulDatePicker;
    @FXML
    private ChoiceBox szerkesztOsztalyChoiceBox;

    private TanarDao tanarDao;
    private OsztalyDao osztalyDao;

    /**
     * A táblázat feltöltése az adatbázis adattagjaival
     */
    @FXML
    public void initialize() {
        Injector injector = Guice.createInjector(new PersistenceModule("jpa-persistence-unit-1"));
        tanarDao = injector.getInstance(TanarDao.class);
        osztalyDao = injector.getInstance(OsztalyDao.class);
        columnAzon.setCellValueFactory(new PropertyValueFactory<>("azon"));
        columnNev.setCellValueFactory(new PropertyValueFactory<>("nev"));
        columnKor.setCellValueFactory(new PropertyValueFactory<>("kor"));
        columnSzul.setCellValueFactory(new PropertyValueFactory<>("szuletesiIdo"));
        columnOsztaly.setCellValueFactory(new PropertyValueFactory<>("osztaly"));
        ObservableList<Tanar> tanarok = FXCollections.observableList(tanarDao.findAll());
        tableTanar.setItems(tanarok);
        columnOsztaly.setCellFactory(column -> {
            TableCell<Tanar, Osztaly> cell = new TableCell<Tanar, Osztaly>() {

                @Override
                protected void updateItem(Osztaly item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        if (item != null) {
                            setText(item.getAzon());
                        } else {
                            setText(null);
                        }
                    }
                }
            };

            return cell;
        });
        columnSzul.setCellFactory(column -> {
            TableCell<Tanar, Date> cell = new TableCell<Tanar, Date>() {
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        if (item != null) {
                            setText(new SimpleDateFormat("yyyy/MM/dd").format(item));
                        }
                    }
                }
            };

            return cell;
        });
        log.info("A táblázat adatokkal fel lett töltve");
    }

    public void tanarTorles(ActionEvent actionEvent) {
        if (tableTanar.getSelectionModel().getSelectedItem() != null) {
            Tanar tanar = tableTanar.getSelectionModel().getSelectedItem();
            tanar.getOsztaly().setOfo(null);
            tanar.setOsztaly(null);
            tanarDao.update(tanar);
            tanarDao.remove(tanar);
            ObservableList<Tanar> tanarok = FXCollections.observableList(tanarDao.findAll());
            tableTanar.setItems(tanarok);
            log.info("{} - {} törölve az adatbázisból", tanar.getAzon(), tanar.getNev());
        } else log.warn("Nincs kijelölve senki");
    }

    public void tanarSzerkeszt(ActionEvent actionEvent) {
        if (tableTanar.getSelectionModel().getSelectedItem() != null) {
            Tanar tanar = tableTanar.getSelectionModel().getSelectedItem();
            szerkesztNevTextField.setText(tanar.getNev());
            szerkesztKorTextField.setText(String.valueOf(tanar.getKor()));
            LocalDate date = tanar.getSzuletesiIdo().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            szerkesztSzulDatePicker.setValue(date);
            ObservableList<String> osztalyok = FXCollections.observableList(osztalyDao.nincsOfo().stream().map(Osztaly::getAzon).collect(Collectors.toList()));
            szerkesztOsztalyChoiceBox.setItems(osztalyok);
            if (tanar.getOsztaly() != null) {
                szerkesztOsztalyChoiceBox.setValue(tanar.getOsztaly().getAzon());
                log.info("{} - {} {} osztályfőnöke lesz", tanar.getAzon(), tanar.getNev(), tanar.getOsztaly());
            }
        } else log.warn("Nincs kijelölve senki");
    }


    public void tanarMent(ActionEvent actionEvent) {
        if (tableTanar.getSelectionModel().getSelectedItem() != null) {
            if (szerkesztNevTextField.getText() != null && szerkesztKorTextField.getText() != null && szerkesztSzulDatePicker.getValue() != null) {
                Tanar tanar = tableTanar.getSelectionModel().getSelectedItem();
                tanar.setNev(szerkesztNevTextField.getText());
                tanar.setKor(Integer.valueOf(szerkesztKorTextField.getText()));
                LocalDate localDate = szerkesztSzulDatePicker.getValue();
                Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
                Date date = Date.from(instant);
                tanar.setSzuletesiIdo(date);
                if (szerkesztOsztalyChoiceBox.getValue() != null) {
                    tanar.setOsztaly(osztalyDao.find(szerkesztOsztalyChoiceBox.getValue()).get());
                    tanar.getOsztaly().setOfo(tanar);
                    log.info("{} - {} {} osztályfőnöke lett", tanar.getAzon(), tanar.getNev(), tanar.getOsztaly());
                }
                log.info("{} - {} nem osztályfőnök", tanar.getAzon(), tanar.getNev());
                if (tanar.eletkorValid()) {
                    if (tanar.nevValid()) {
                        if (tanar.azonValid()) {
                            if (tanar.szulIdoValid()) {
                                tanarDao.update(tanar);
                                tableTanar.getItems().clear();
                                ObservableList<Tanar> tanarok = FXCollections.observableList(tanarDao.findAll());
                                tableTanar.setItems(tanarok);
                                log.info("{} - {} elmentve", tanar.getAzon(), tanar.getNev());
                            } else log.warn("{} - {} születési ideje nem érvényes", tanar.getAzon(), tanar.getNev());
                        } else log.warn("{} - {} azonosítója nem érvényes", tanar.getAzon(), tanar.getNev());
                    } else log.warn("{} - {} neve nem érvényes", tanar.getAzon(), tanar.getNev());
                } else log.warn("{} - {} életkora nem érvényes", tanar.getAzon(), tanar.getNev());
                szerkesztNevTextField.setText("");
                szerkesztKorTextField.setText("");
                szerkesztSzulDatePicker.setValue(null);
                szerkesztOsztalyChoiceBox.setValue(null);
            } else log.warn("Nem lett kitöltve az összes mező");
        } else log.warn("Nem lett kijelölve tanár");
    }
}
