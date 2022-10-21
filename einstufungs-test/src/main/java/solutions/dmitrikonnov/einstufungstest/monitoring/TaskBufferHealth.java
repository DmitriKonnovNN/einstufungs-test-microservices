package solutions.dmitrikonnov.einstufungstest.monitoring;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import solutions.dmitrikonnov.einstufungstest.cache.buffer.ETBuffer;

@Component
@Slf4j
@AllArgsConstructor
public class TaskBufferHealth implements HealthIndicator {

    private final ETBuffer buffer;

    @Override
    public Health health() {
        log.debug("buffer size in health= {}", buffer.getBufferedTaskNumber());
        if(buffer.getBufferedTaskNumber()==0){
            return Health.outOfService().withDetail("Buffer size", 0).build();}
        return Health.up().withDetail("Buffer size",buffer.getBufferedTaskNumber()).build();
    }


}
