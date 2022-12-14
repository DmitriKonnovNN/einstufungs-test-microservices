package solutions.dmitrikonnov.etlimitsrepo;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import solutions.dmitrikonnov.etentities.ETLimit;
import solutions.dmitrikonnov.etenums.ETTaskLevel;

import java.util.List;
import java.util.Optional;

public interface EtLimitsRepo extends JpaRepository<ETLimit,Integer>, EtLimitsCustomRepo {


    @Cacheable (value = "limit")
    List<ETLimit> findAllByOrderByLevel();

    @Cacheable (value = "limit",key="#level", unless = "#a0=='zero-arg-not-cached'")
    Optional<ETLimit> findByLevel(ETTaskLevel level);

    @CachePut (value = "limit",key="#level")
    @Modifying
    @Query("update ETLimit e set e.maxLimit = :max, e.minLimit = :min where e.level = :level ")
    void updateByLevel(@Param("level") ETTaskLevel level,
                       @Param("min") short min,
                       @Param("max")short max);

    @Cacheable (value = "limit", key="#level", unless = "#a0=='zero-arg-not-cached'")
    boolean existsByLevel(ETTaskLevel level);

}

