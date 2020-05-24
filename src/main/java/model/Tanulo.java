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

    public boolean eletkorValid(){
        return getKor()>=6;
    }


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

    public boolean szulIdoValid(){
        Calendar szuletesiIdo = Calendar.getInstance();
        szuletesiIdo.setTime(getSzuletesiIdo());
        return Calendar.getInstance().get(Calendar.YEAR)-szuletesiIdo.get(Calendar.YEAR)<=getKor()+1 &&
                Calendar.getInstance().get(Calendar.YEAR)-szuletesiIdo.get(Calendar.YEAR)>=getKor();
    }
}
