package solutions.dmitrikonnov.einstufungstest.weblayer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.dto.ETTaskSheet;
import solutions.dmitrikonnov.einstufungstest.cache.buffer.ETBuffer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service ("eTinRamSimpleCache")
@Slf4j
@AllArgsConstructor
public class ETInRamSimpleCache implements ExerciseSetCache {

    private final Map<Integer, ETTaskSheet> toCheckCache = new ConcurrentHashMap<>();
    private final ETBuffer buffer;

    public void saveToCheck(Integer id, ETTaskSheet set){
        set.setCachedAt(System.currentTimeMillis());
        toCheckCache.put(id, set);
    }
    public ETTaskSheet fetch(Integer id){
        return toCheckCache.get(id);
    }

    // TODO: no explicit eviction any longer needed: replace by redis with TTL!
    public void evict(Integer id) {
        toCheckCache.remove(id);
    }

    @Override
    public ETTaskSheet getPreparedExerciseSet() {
        var b = buffer.getPreparedTaskSheet();
        saveToCheck(b.getTaskSheetHash(),b);
        return b;
    }
}
