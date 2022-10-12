package solutions.dmitrikonnov.etlimitsrepo;

import lombok.extern.slf4j.Slf4j;
import solutions.dmitrikonnov.etenums.ETTaskLevel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
public class EtLimitsCustomRepoImpl implements EtLimitsCustomRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Map<ETTaskLevel, Short> findMaximumLimitByLevels() {
        return  entityManager
                .createQuery("select e.level AS level," +
                        " e.maxLimit as maxlevel " +
                        " from ETLimit e", Tuple.class)
                .getResultStream()
                .collect(Collectors.toMap(
                        tuple -> (ETTaskLevel) tuple.get("level"),
                        tuple -> (Short) tuple.get("maxLimit")
                ));
    }


}
