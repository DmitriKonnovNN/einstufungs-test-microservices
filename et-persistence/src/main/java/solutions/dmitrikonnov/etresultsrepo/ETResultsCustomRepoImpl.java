package solutions.dmitrikonnov.etresultsrepo;

import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
public class ETResultsCustomRepoImpl implements ETResultsCustomRepo {


    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Map<Integer, Boolean> findAllMapsItemCorrectness(@Param("createdOn") Date createdOn) {


        return  entityManager.createQuery("select entry(maps) " +
                        " from ETResults e left join fetch e.idToCorrectnessMap maps WHERE e.createdOn<:createdOn", Map.Entry.class)
                .getResultStream()
                .collect(Collectors.toMap(
                        entry -> (Integer)entry.getKey(),
                        entry-> (Boolean)entry.getValue()));
    }

}
