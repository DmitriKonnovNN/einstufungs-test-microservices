package solutions.dmitrikonnov.einstufungstest.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import solutions.dmitrikonnov.einstufungstest.cache.buffer.ETBuffer;

@Component
public class TaskBufferGauge {


    public TaskBufferGauge(@Autowired ETBuffer buffer, MeterRegistry registry) {
        registry.gauge("task.buffer.elements", Tags.empty(),buffer.getBufferedTaskNumber());
    }

}
