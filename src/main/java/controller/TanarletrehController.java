package controller;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dao.TanarDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import model.Tanar;
import org.checkerframework.checker.units.qual.A;
import util.jpa.PersistenceModule;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
public class TanarletrehController {

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

    private TanarDao tanarDao;

    /**
     * Hozzáfér az adatbázishoz
     */
    @FXML
    public void initialize() {
        Injector injector = Guice.createInjector(new PersistenceModule("jpa-persistence-unit-1"));
        tanarDao = injector.getInstance(TanarDao.class);
    }

    /**
     * A tanár létrehozása az adatbázisba
     */
    public void tanarLetrehozas(ActionEvent actionEvent) {
        if (nevTextfield.getText() != null && korTextfield.getText() != null && azonTextfield.getText() != null && szulDatePicker.getValue() != null) {
            LocalDate localDate = szulDatePicker.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date date = Date.from(instant);
            Tanar tanar = Tanar.builder()
                    .nev(nevTextfield.getText())
                    .kor(Integer.valueOf(korTextfield.getText()))
                    .azon(azonTextfield.getText())
                    .szuletesiIdo(date)
                    .build();
            if (tanar.eletkorValid()) {
                if (tanar.nevValid()) {
                    if (tanar.azonValid()) {
                        if (tanar.szulIdoValid()) {
                            tanarDao.persist(tanar);
                            log.info("{} - {} létrehozva", tanar.getAzon(), tanar.getNev());
                        } else log.warn("{} - {} születési ideje nem érvényes", tanar.getAzon(), tanar.getNev());
                    } else log.warn("{} - {} azonosítója nem érvényes", tanar.getAzon(), tanar.getNev());
                } else log.warn("{} - {} neve nem érvényes", tanar.getAzon(), tanar.getNev());
            } else log.warn("{} - {} életkora nem érvényes", tanar.getAzon(), tanar.getNev());
        } else log.warn("Nem lett kitöltve az összes mező");
    }
}
