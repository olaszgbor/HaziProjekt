package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Calendar;
import java.util.Date;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * Egy tanulót reprezentáló osztály
 */
public class Tanulo {
    /**
     * Egy tanuló neve
     */
    private String nev;
    /**
     * Egy tanuló kora
     */
    private int kor;
    /**
     * Egy tanuló iskolai azonosítója // igazolványszáma
     */
    @Id
    private String azon;
    /**
     * Egy tanuló születésének ideje
     */
    private Date szuletesiIdo;
    /**
     * Egy tanuló osztálya
     */
    @ManyToOne
    private Osztaly osztaly;

    /**
     * Az életkor érvényességét vizsgáló metódus
     * @return Igaz, ha 6-nál nagyobb vagy vele egyenlő, egyébként hamis
     */
    public boolean eletkorValid(){
        return getKor()>=6;
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
     * @return Igaz, ha szóközzel elválasztva két nagybetűvel kezdődő Stringsorozatot adunk,
     * vagy ha ezelőtt jelöljük, hogy ifjabb vagy idősebb családtagról van szó azonos névvel,
     * egyébként hamis.
     */
    public boolean nevValid() {
        if (getNev().contains(" ")) {
            String[] nevreszek = getNev().split(" ");
            if(nevreszek[0].equals("ifj.") || nevreszek[0].equals("id.")) {
                if (nevreszek.length > 2) {
                    for (int i = 1; i < nevreszek.length - 1; i++) {
                        if (!Character.isUpperCase(nevreszek[i].charAt(0))) {
                            return false;
                        }
                        return true;
                    }
                }
            }
            for (String m:nevreszek) {
                if(!Character.isUpperCase(m.charAt(0))){
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
