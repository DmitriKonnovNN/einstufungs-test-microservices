package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import lombok.extern.slf4j.Slf4j;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.etenums.ETExerciseLevel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
public class LimitsCustomRepoImpl implements LimitsCustomRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Map<ETExerciseLevel, Short> findMaximumLimitByLevels() {
        return  entityManager
                .createQuery("select e.level AS level," +
                        " e.maxLimit as maxlevel " +
                        " from ETLimit e", Tuple.class)
                .getResultStream()
                .collect(Collectors.toMap(
                        tuple -> (ETExerciseLevel) tuple.get("level"),
                        tuple -> (Short) tuple.get("maxLimit")
                ));
    }


}
