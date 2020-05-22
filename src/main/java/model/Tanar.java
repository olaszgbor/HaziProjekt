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

    public boolean eletkorValid() {
        return getKor() >= 21 && getKor() < 60;
    }

    public boolean nevValid() {
        if (getNev().contains(" ")) {
            String[] nevreszek = getAzon().split(" ");
            if(nevreszek[0].equals("dr.") || nevreszek[0].equals("prof.") || nevreszek[0].equals("ifj.") || nevreszek[0].equals("id.")) {
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
