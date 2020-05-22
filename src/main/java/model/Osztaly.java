package model;


import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
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

}
