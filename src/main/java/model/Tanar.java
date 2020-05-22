package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
}
