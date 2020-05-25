package controller;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dao.OsztalyDao;
import dao.TanuloDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import model.Osztaly;
import model.Tanar;
import model.Tanulo;
import util.jpa.PersistenceModule;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
public class TanuloletrehController {

    /**
     * A JavaFX-es felület részei
     */
    @FXML
    private TextField nevTextfield;
    @FXML
    private TextField korTextfield;
    @FXML
    private TextField azonTextfield;
    @FXML
    private DatePicker szulDatePicker;
    @FXML
    private ChoiceBox osztalyChoiceBox;

    private TanuloDao tanuloDao;
    private OsztalyDao osztalyDao;

    /**
     * Hozzáfér az adatbázishoz
     * A tanuló létrehozásához szükséges osztálykiválasztáshoz az elérhető osztályokat kijelző lista feltöltése
     */
    @FXML
    public void initialize() {
        Injector injector = Guice.createInjector(new PersistenceModule("jpa-persistence-unit-1"));
        tanuloDao = injector.getInstance(TanuloDao.class);
        osztalyDao = injector.getInstance(OsztalyDao.class);
        ObservableList<String> osztalyok = FXCollections.observableList(osztalyDao.findAll().stream().map(Osztaly::getAzon).collect(Collectors.toList()));
        osztalyChoiceBox.setItems(osztalyok);
    }

    /**
     * A tanuló létrehozása az adatbázisba
     */
    public void tanuloLetrehozas(ActionEvent actionEvent) {
        if (nevTextfield.getText() != null && korTextfield.getText() != null && azonTextfield.getText() != null && szulDatePicker.getValue() != null && osztalyChoiceBox.getSelectionModel().getSelectedItem() != null) {
            Osztaly osztaly = osztalyDao.find(osztalyChoiceBox.getSelectionModel().getSelectedItem()).get();
            if (osztaly.letszamValid()) {
                LocalDate localDate = szulDatePicker.getValue();
                Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
                Date date = Date.from(instant);
                Tanulo tanulo = Tanulo.builder()
                        .nev(nevTextfield.getText())
                        .kor(Integer.valueOf(korTextfield.getText()))
                        .azon(azonTextfield.getText())
                        .szuletesiIdo(date)
                        .osztaly(osztaly)
                        .build();
                if (tanulo.eletkorValid()) {
                    if (tanulo.nevValid()) {
                        if (tanulo.azonValid()) {
                            if (tanulo.szulIdoValid()) {
                                tanuloDao.persist(tanulo);
                                log.info("{} - {} létrehozva", tanulo.getAzon(), tanulo.getNev());
                                osztaly.setAktualisLetszam(osztaly.getTanulok().size());
                                osztalyDao.update(osztaly);
                                log.info("Az osztály létszáma bővült");
                                nevTextfield.setText("");
                                korTextfield.setText("");
                                azonTextfield.setText("");
                                szulDatePicker.setValue(null);
                                osztalyChoiceBox.setValue(null);
                            } else log.warn("{} - {} születési ideje nem érvényes", tanulo.getAzon(), tanulo.getNev());
                        } else log.warn("{} - {} azonosítója nem érvényes", tanulo.getAzon(), tanulo.getNev());
                    } else log.warn("{} - {} neve nem érvényes", tanulo.getAzon(), tanulo.getNev());
                } else log.warn("{} - {} életkora nem érvényes", tanulo.getAzon(), tanulo.getNev());
            } else log.warn("{} létszáma nem érvényes", osztaly.getAzon());
        } else log.warn("Nem lett kitöltve az összes mező");
    }
}
