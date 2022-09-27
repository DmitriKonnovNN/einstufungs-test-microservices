package solutions.dmitrikonnov.einstufungstest.businesslayer;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import solutions.dmitrikonnov.etentities.ETExercise;
import solutions.dmitrikonnov.etenums.ETExerciseLevel;

import java.util.List;
import java.util.Set;


public interface ETExerciseRepo extends JpaRepository<ETExercise, Integer> {

    @Cacheable (value = "exercises",  unless = "#a0=='zero-arg-not-cached'")
    Set<ETExercise> findAllByOrderByExerciseLevelAsc();
    List<ETExercise> findAllByExerciseLevel(ETExerciseLevel level);
    List<ETExercise> findAll();
    @Modifying
    @Transactional ()
    @Query ("update ETExercise e set e.exerciseContent = :data where e.exerciseId = :id")
    void updateImageDataById(@Param("id")int id,
                             @Param("data") String imageData );
}
