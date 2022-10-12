package solutions.dmitrikonnov.etlimitsrepo;

import org.springframework.cache.annotation.Cacheable;
import solutions.dmitrikonnov.etenums.ETTaskLevel;

import java.util.Map;

public interface EtLimitsCustomRepo {

    @Cacheable(value = "limit-by-level")
    Map<ETTaskLevel,Short> findMaximumLimitByLevels();

}
