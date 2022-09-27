package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import org.springframework.cache.annotation.Cacheable;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.etenums.ETExerciseLevel;

import java.util.Map;

public interface LimitsCustomRepo {

    @Cacheable(value = "limit-by-level")
    Map<ETExerciseLevel,Short> findMaximumLimitByLevels();

}
