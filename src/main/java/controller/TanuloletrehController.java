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
import model.Osztaly;
import model.Tanulo;
import util.jpa.PersistenceModule;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

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
    public void initialize(){
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
            if(osztaly.getAktualisLetszam()<osztaly.getLetszam()) {
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
                tanuloDao.persist(tanulo);
                osztaly.setAktualisLetszam(osztaly.getTanulok().size());
                osztalyDao.update(osztaly);
            }
        }
    }
}
