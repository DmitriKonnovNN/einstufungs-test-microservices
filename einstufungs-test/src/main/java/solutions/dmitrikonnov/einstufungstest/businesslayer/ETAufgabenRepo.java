package solutions.dmitrikonnov.einstufungstest.businesslayer;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETAufgabe;

import java.util.List;
import java.util.Set;


public interface ETAufgabenRepo extends JpaRepository<ETAufgabe, Integer> {

    @Cacheable (value = "aufgaben",  unless = "#a0=='zero-arg-not-cached'")
    Set<ETAufgabe> findAllByOrderByAufgabenNiveauAsc();
    List<ETAufgabe> findAllByAufgabenNiveau(ETAufgabenNiveau niveau);
    List<ETAufgabe> findAll();
    @Modifying
    @Transactional ()
    @Query ("update ETExercise a set a.exerciseContent = :data where a.exerciseId = :id")
    void updateImageDataById(@Param("id")int id,
                             @Param("data") String imageData );
}
