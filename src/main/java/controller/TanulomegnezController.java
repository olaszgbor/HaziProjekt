package controller;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dao.OsztalyDao;
import dao.TanuloDao;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
public class TanulomegnezController {
    /**
     * A tanulók táblázatban tárolt adattagjai
     */
    @FXML
    private TableView<Tanulo> tableTanulo;
    @FXML
    private TableColumn<Tanulo, String> columnNev;
    @FXML
    private TableColumn<Tanulo, Integer> columnKor;
    @FXML
    private TableColumn<Tanulo, String> columnAzon;
    @FXML
    private TableColumn<Tanulo, Date> columnSzul;
    @FXML
    private TableColumn<Tanulo, Osztaly> columnOsztaly;
    @FXML
    private TextField szerkesztNevTextField;
    @FXML
    private TextField szerkesztKorTextField;

    @FXML
    private DatePicker szerkesztSzulDatePicker;
    @FXML
    private ChoiceBox szerkesztOsztalyChoiceBox;

    private TanuloDao tanuloDao;
    private OsztalyDao osztalyDao;

    /**
     * A táblázat feltöltése az adatbázis adattagjaival
     */
    @FXML
    public void initialize() {
        Injector injector = Guice.createInjector(new PersistenceModule("jpa-persistence-unit-1"));
        tanuloDao = injector.getInstance(TanuloDao.class);
        osztalyDao = injector.getInstance(OsztalyDao.class);
        columnAzon.setCellValueFactory(new PropertyValueFactory<>("azon"));
        columnNev.setCellValueFactory(new PropertyValueFactory<>("nev"));
        columnKor.setCellValueFactory(new PropertyValueFactory<>("kor"));
        columnSzul.setCellValueFactory(new PropertyValueFactory<>("szuletesiIdo"));
        columnOsztaly.setCellValueFactory(new PropertyValueFactory<>("osztaly"));
        ObservableList<Tanulo> tanulok = FXCollections.observableList(tanuloDao.findAll());
        tableTanulo.setItems(tanulok);
        columnOsztaly.setCellFactory(column -> {
            TableCell<Tanulo, Osztaly> cell = new TableCell<Tanulo, Osztaly>() {

                @Override
                protected void updateItem(Osztaly item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        if (item != null) {
                            setText(item.getAzon());
                        }
                    }
                }
            };

            return cell;
        });
        columnSzul.setCellFactory(column -> {
            TableCell<Tanulo, Date> cell = new TableCell<Tanulo, Date>() {
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

    public void tanuloTorles(ActionEvent actionEvent) {
        if (tableTanulo.getSelectionModel().getSelectedItem() != null) {
            Tanulo tanulo = tableTanulo.getSelectionModel().getSelectedItem();
            if (tanulo.getOsztaly() != null) {
                tanulo.getOsztaly().getTanulok().remove(tanulo);
                tanulo.setOsztaly(null);
                log.info("{} - {} kivéve a(z) {} osztályból", tanulo.getAzon(), tanulo.getNev(), tanulo.getOsztaly());
            } else log.warn("{} - {} nem tartozik egy osztályba sem", tanulo.getAzon(), tanulo.getNev());
            tanuloDao.update(tanulo);
            tanuloDao.remove(tanulo);
            ObservableList<Tanulo> tanulok = FXCollections.observableList(tanuloDao.findAll());
            tableTanulo.setItems(tanulok);
            log.info("{} - {} törölve", tanulo.getAzon(), tanulo.getNev());
        } else log.warn("Nincs kijelölve senki");
    }

    public void tanuloSzerkeszt(ActionEvent actionEvent) {
        if (tableTanulo.getSelectionModel().getSelectedItem() != null) {
            Tanulo tanulo = tableTanulo.getSelectionModel().getSelectedItem();
            szerkesztNevTextField.setText(tanulo.getNev());
            szerkesztKorTextField.setText(String.valueOf(tanulo.getKor()));
            LocalDate date = tanulo.getSzuletesiIdo().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            szerkesztSzulDatePicker.setValue(date);
            ObservableList<String> osztalyok = FXCollections.observableList(osztalyDao.findAll().stream().map(Osztaly::getAzon).collect(Collectors.toList()));
            szerkesztOsztalyChoiceBox.setItems(osztalyok);
            if (tanulo.getOsztaly() != null) {
                szerkesztOsztalyChoiceBox.setValue(tanulo.getOsztaly().getAzon());
            }
        } else log.warn("Nincs kijelölve senki");
    }


    public void tanuloMent(ActionEvent actionEvent) {
        if (tableTanulo.getSelectionModel().getSelectedItem() != null) {
            if (szerkesztNevTextField.getText() != null && szerkesztKorTextField.getText() != null && szerkesztSzulDatePicker.getValue() != null && szerkesztOsztalyChoiceBox.getSelectionModel().getSelectedItem() != null) {
                Tanulo tanulo = tableTanulo.getSelectionModel().getSelectedItem();
                Osztaly ujOsztaly = osztalyDao.find(szerkesztOsztalyChoiceBox.getValue()).get();
                if (tanulo.getOsztaly() != ujOsztaly && ujOsztaly.letszamValid()) {
                    Osztaly regiOsztaly = osztalyDao.find(tanulo.getOsztaly().getAzon()).get();
                    regiOsztaly.getTanulok().remove(tanulo);
                    regiOsztaly.setAktualisLetszam(regiOsztaly.getTanulok().size());
                    ujOsztaly.getTanulok().add(tanulo);
                    ujOsztaly.setAktualisLetszam(ujOsztaly.getTanulok().size());
                    tanulo.setOsztaly(ujOsztaly);
                }
                tanulo.setNev(szerkesztNevTextField.getText());
                tanulo.setKor(Integer.valueOf(szerkesztKorTextField.getText()));
                LocalDate localDate = szerkesztSzulDatePicker.getValue();
                Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
                Date date = Date.from(instant);
                tanulo.setSzuletesiIdo(date);
                if (tanulo.eletkorValid()) {
                    if (tanulo.nevValid()) {
                        if (tanulo.azonValid()) {
                            if (tanulo.szulIdoValid()) {
                                osztalyDao.update(ujOsztaly);
                                tanuloDao.update(tanulo);
                                tableTanulo.getItems().clear();
                                ObservableList<Tanulo> tanulok = FXCollections.observableList(tanuloDao.findAll());
                                tableTanulo.setItems(tanulok);
                                log.info("{} - {} szerkesztve", tanulo.getAzon(), tanulo.getNev());
                            } else log.warn("{} - {} születési ideje nem érvényes", tanulo.getAzon(), tanulo.getNev());
                        } else log.warn("{} - {} azonosítója nem érvényes", tanulo.getAzon(), tanulo.getNev());
                    } else log.warn("{} - {} neve nem érvényes", tanulo.getAzon(), tanulo.getNev());
                } else log.warn("{} - {} életkora nem érvényes", tanulo.getAzon(), tanulo.getNev());
                szerkesztNevTextField.setText("");
                szerkesztKorTextField.setText("");
                szerkesztSzulDatePicker.setValue(null);
                szerkesztOsztalyChoiceBox.setValue(null);
            } else log.warn("Nem lett kitöltve az összes mező");
        } else log.warn("Nem lett kijelölve tanuló");
    }
}
