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
        return getKor() <=14;
    }


    public boolean nevValid() {
        if (getNev().contains(" ")) {
            String[] nevreszek = getAzon().split(" ");
            if(nevreszek[0].equals("ifj.") || nevreszek[0].equals("id.")) {
                for (String n:nevreszek) {
                    if(!Character.isUpperCase(n.charAt(0))){
                        return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean szulIdoValid(){
        return Calendar.getInstance().get(Calendar.YEAR)-getSzuletesiIdo().getYear()<=getKor()-1;
    }
}
