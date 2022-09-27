package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import solutions.dmitrikonnov.etentities.ETResults;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Primary
public interface ETResultsRepo extends JpaRepository<ETResults, UUID>, ETResultsCustomRepo,Pingable {


    List<ETResults> findAllByCreatedOnBefore(Date createdOn);


    @Query(value = "SELECT eem.et_aufg_id AS id FROM et_results_mapping AS eem " +
            "INNER JOIN et_results AS ee ON ee.id = eem.et_results_id " +
            "WHERE eem.et_exercise_correctness = true AND ee.created_on < : createdOn ", nativeQuery = true)
    List<Integer> findAllIdsOfCorrectAnsweredItems(Date createdOn);

}
