package solutions.dmitrikonnov.einstufungstest.weblayer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.dto.ETTaskSheet;
import solutions.dmitrikonnov.einstufungstest.cache.buffer.ETBuffer;
import solutions.dmitrikonnov.exceptions.TimeForTestExpiredException;

@Service ("eTBufferAndCaffeineCache")
@Slf4j
@AllArgsConstructor
public class ETBufferAndCaffeineCache implements ExerciseSetCache {
    private final ETBuffer buffer;
    private final CacheManager cacheManager;

    @Override
    public void saveToCheck(Integer id, ETTaskSheet bogen) {
        var cache = cacheManager.getCache("to-check-cache");
        cache.put(id, bogen);
    }

    @Override
    @Cacheable (cacheNames = "to-check-cache", key = "#id")
    public ETTaskSheet fetch(Integer id) {
        return serviceMock(id);
    }

    @Override
    public void evict(Integer id) {
    }

    @Override
    public ETTaskSheet getPreparedExerciseSet() {
        var b = buffer.getPreparedTaskSheet();
        saveToCheck(b.getTaskSheetHash(),b);
        return b;
    }
    /**
     * mocks the actual AufgabenBogenService to make the cache either return the cached value or throw
     * a related exception.
     * */
    private ETTaskSheet serviceMock (Integer id) {
        throw new TimeForTestExpiredException("Zeit für den Test ist um.");
    }
}
