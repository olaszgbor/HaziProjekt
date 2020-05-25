package model;


import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

/**
 * Egy osztályt reprezentáló osztály
 */
@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Osztaly {
    /**
     * Egy osztály neve
     */
    @Id
    private String azon;
    /**
     * Egy osztály jelenlegi létszáma
     */
    private int aktualisLetszam;
    /**
     * Egy osztály maximális létszáma létszáma
     */
    private int letszam;
    /**
     * Egy osztály tanulóinak listája
     */
    @OneToMany(mappedBy = "osztaly")
    private List<Tanulo> tanulok;
    /**
     * Egy osztály osztályfőnökének a neve
     */
    @OneToOne
    @JoinColumn(name = "osztaly")
    private Tanar ofo;

    /**
     * Az osztály létszámának érvényességét vizsgáló metódus
     * @return Igaz, ha nagyobb, mint 0, kisebb, mint 40,
     * és az aktuális létszám a megadott létszámnál kisebb,
     * egyébként hamis
     */
    public boolean letszamValid() {
        return aktualisLetszam < letszam &&
                letszam > 0 &&
                letszam < 40;
    }

    /**
     * Az azonosító érvényességét vizsgáló metódus
     * @return Igaz, ha tartalmaz összesen 1 pontot,
     * a pont előtti egység 13-nál kevesebb szám,
     * a pont utáni egység maximum 3 karakter, és nem tartalmaz számot,
     * egyébként hamis
     */
    public boolean azonValid() {
        if (getAzon().contains(".")) {
            String[] azonreszek = getAzon().split("\\.");
            if (azonreszek.length == 2) {
                for (char c : azonreszek[0].toCharArray()) {
                    if (!Character.isDigit(c)) {
                        return false;
                    }
                }
                if (Integer.valueOf(azonreszek[0]) > 0 && Integer.valueOf(azonreszek[0]) <= 13) {
                    if (azonreszek[1].length() <= 3) {
                        char[] azonreszekreszek = azonreszek[1].toCharArray();
                        for (char c : azonreszekreszek) {
                            if (!Character.isLetter(c))
                                return false;
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
