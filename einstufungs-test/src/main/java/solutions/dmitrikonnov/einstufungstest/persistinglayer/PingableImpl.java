package solutions.dmitrikonnov.einstufungstest.persistinglayer;


import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Transactional(readOnly = true)
public class PingableImpl implements Pingable{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public <T> int ping(Class<T> clazz) {
        var name = clazz.getName();
        String queryString = String.format("SELECT 1 FROM %s",name );
        TypedQuery<Integer> query = entityManager.createQuery(queryString,Integer.class);
        return query.getSingleResult();
    }
}
