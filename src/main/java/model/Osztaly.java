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
    private int aktualisLetszam;
    /**
     * Egy osztály létszáma
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

    public boolean letszamValid(){
        return aktualisLetszam<letszam && letszam<0 && letszam > 40;
    }

    public boolean azonValid(){
        if(getAzon().contains(".")) {
            String[] azonreszek = getAzon().split(".");
            if (Integer.valueOf(azonreszek[0]) > 0 && Integer.valueOf(azonreszek[0]) <= 13) {
                if(azonreszek[1].length()<=3){
                    char[] azonreszekreszek = azonreszek[1].toCharArray();
                    for (char c : azonreszekreszek) {
                        if (!Character.isLetter(c))
                            return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
