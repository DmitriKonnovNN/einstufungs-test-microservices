package solutions.dmitrikonnov.einstufungstest.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Configuration
@EnableCaching()
@Slf4j

public class CaffeineCacheImpl {

    @Bean
    public CacheManager cacheManager(Ticker ticker) {
        CacheSpec tasks = new CacheSpec("tasks",1440, 10_000);
        CacheSpec limit = new CacheSpec("limit",10_080, 10_0000);
        CacheSpec limitByLevel = new CacheSpec("limit-by-level",10_080, 10_0000);
        CacheSpec toCheckCache = new CacheSpec("to-check-cache",90,600);
        Map<String, CacheSpec> specs = new HashMap<>(){{
            put(tasks.getName(),tasks);
            put(limit.getName(),limit);
            put(limitByLevel.getName(),limitByLevel);
            put(toCheckCache.getName(),toCheckCache);
        }};
        SimpleCacheManager manager = new SimpleCacheManager();
        if (!specs.isEmpty()) {
            List<CaffeineCache> caches = specs.entrySet().stream()
                    .map(entry -> buildCache(entry.getKey(), entry.getValue(), ticker)).collect(Collectors.toList());
            manager.setCaches(caches);
        }
        return manager;
    }

    private CaffeineCache buildCache(String name, CacheSpec cacheSpec, Ticker ticker) {
        log.info("Cache {} specified timeout of {} min, max of {}", name, cacheSpec.getTimeout(), cacheSpec.getMax());
        final Caffeine<Object, Object> caffeineBuilder = Caffeine.newBuilder()
                .expireAfterWrite(cacheSpec.getTimeout(), TimeUnit.MINUTES).maximumSize(cacheSpec.getMax())
                .ticker(ticker);
        if(name.equals("tasks"))caffeineBuilder.softValues();
        return new CaffeineCache(name, caffeineBuilder.build());
    }

    @Bean
    public Ticker ticker() {
        return Ticker.systemTicker();
    }
}

