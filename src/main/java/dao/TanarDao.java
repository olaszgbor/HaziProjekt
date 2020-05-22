package dao;

import model.Tanar;
import util.jpa.GenericJpaDao;

import java.util.List;

public class TanarDao extends GenericJpaDao<Tanar> {
    /**
     * Kiválasztja a még nem osztályfőnök tanárokat lekérdezéssel
     */
    public List<Tanar> nemOfo(){
        return entityManager.createQuery("select t from Tanar t where osztaly not in (select o.ofo from Osztaly o)").getResultList();
    }
}
