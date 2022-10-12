package solutions.dmitrikonnov.ettaskrepo;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import solutions.dmitrikonnov.etentities.ETTask;
import solutions.dmitrikonnov.etenums.ETTaskLevel;

import java.util.List;
import java.util.Set;

@Repository
public interface ETTaskRepo extends JpaRepository<ETTask, Integer> {

    @Cacheable (value = "tasks",  unless = "#a0=='zero-arg-not-cached'")
    Set<ETTask> findAllByOrderByTaskLevelAsc();
    List<ETTask> findAllByTaskLevel(ETTaskLevel level);
    List<ETTask> findAll();
    @Modifying
    @Transactional ()
    @Query ("update ETTask e set e.taskContent = :data where e.taskId = :id")
    void updateImageDataById(@Param("id")int id,
                             @Param("data") String imageData );
}