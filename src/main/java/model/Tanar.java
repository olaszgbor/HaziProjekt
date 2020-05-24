package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * Egy tanárt reprezentáló osztály
 */
public class Tanar {
    /**
     * Egy tanár neve
     */
    private String nev;
    /**
     * Egy tanár kora
     */
    private int kor;
    /**
     * Egy tanár iskolai azonosítója
     */
    @Id
    private String azon;
    /**
     * Egy tanár születésének ideje
     */
    private Date szuletesiIdo;
    /**
     * Egy tanár aktuális osztálya, ha van épp neki
     */
    @OneToOne(mappedBy = "ofo")
    private Osztaly osztaly;

    /**
     * Az életkor érvényességét vizsgáló metódus
     * @return Igaz, ha 21-nél nagyobb vagy vele egyenlő, és 60-nál kevesebb, egyébként hamis
     */
    public boolean eletkorValid() {
        return getKor() >= 21 && getKor() < 60;
    }
    /**
     * Az azonosító érvényességét vizsgáló metódus
     * @return Igaz, ha bármilyen (nem-whitespace) karaktert tartalmaz, egyébként hamis
     */
    public boolean azonValid() {
        return !getAzon().isBlank();
    }

    /**
     * A név érvényességét vizsgáló metódus
     * @return Igaz, ha szóközzel elválasztva két nagybetűvel kezdődő Sztringsorozatot adunk,
     * vagy ha ezelőtt kisbetűvel jelöljük, hogy ifjabb vagy idősebb családtagról van szó azonos névvel,
     * vagy ha kisbetűs titulusa (professzor vagy doktor) van a tanárnak,
     * egyébként hamis.
     */
    public boolean nevValid() {
        if (getNev().contains(" ")) {
            String[] nevreszek = getNev().split(" ");
            if(nevreszek[0].equals("dr.") || nevreszek[0].equals("prof.") || nevreszek[0].equals("ifj.") || nevreszek[0].equals("id.")) {
                if (nevreszek.length > 2) {
                    for (int i = 1; i < nevreszek.length - 1; i++) {
                        if (!Character.isUpperCase(nevreszek[i].charAt(0))) {
                            return false;
                        }
                        return true;
                    }
                }
            }
            for (String m : nevreszek) {
                if (!Character.isUpperCase(m.charAt(0))) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * A születési idő érvényességét vizsgáló metódus
     * @return Igaz, ha a mai dátum évének és a születési idő évének különbsége
     * megegyezik a korral, vagy annál eggyel több, egyébként hamis.
     */
    public boolean szulIdoValid(){
        Calendar szuletesiIdo = Calendar.getInstance();
        szuletesiIdo.setTime(getSzuletesiIdo());
        return Calendar.getInstance().get(Calendar.YEAR)-szuletesiIdo.get(Calendar.YEAR)==getKor()+1 ||
                Calendar.getInstance().get(Calendar.YEAR)-szuletesiIdo.get(Calendar.YEAR)==getKor();
    }

}
