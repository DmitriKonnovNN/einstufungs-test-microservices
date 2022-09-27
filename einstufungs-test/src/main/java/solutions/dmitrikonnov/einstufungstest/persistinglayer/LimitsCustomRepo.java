package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import org.springframework.cache.annotation.Cacheable;
import solutions.dmitrikonnov.etenums.ETTaskLevel;

import java.util.Map;

public interface LimitsCustomRepo {

    @Cacheable(value = "limit-by-level")
    Map<ETTaskLevel,Short> findMaximumLimitByLevels();

}
