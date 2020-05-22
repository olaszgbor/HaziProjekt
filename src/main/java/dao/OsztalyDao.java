package dao;

import model.Osztaly;
import model.Tanar;
import util.jpa.GenericJpaDao;

import java.util.List;

public class OsztalyDao extends GenericJpaDao<Osztaly> {

    public List<Tanar> nemOfo(){
        return entityManager.createQuery("select t from Tanar t where osztaly not in (select o.ofo from Osztaly o)").getResultList();
    }
    public List<Osztaly> nincsOfo(){
        return entityManager.createQuery("select o from Osztaly o where ofo is null").getResultList();
    }
}
