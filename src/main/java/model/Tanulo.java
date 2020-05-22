package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
}
