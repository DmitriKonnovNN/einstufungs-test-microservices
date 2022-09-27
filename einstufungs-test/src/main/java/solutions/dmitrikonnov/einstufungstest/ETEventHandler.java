package solutions.dmitrikonnov.einstufungstest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.cache.buffer.ETBuffer;
import solutions.dmitrikonnov.exceptions.NoTaskSetToServeException;

@Service
@Slf4j
@AllArgsConstructor
public class ETEventHandler {

    private final ETBuffer buffer;

    @Async
    @EventListener (ETAnswerSheetCheckedEvent.class)
    public void onApplicationEvent(ETAnswerSheetCheckedEvent event){
        log.info("Event {} --- time {} .", event.getCHECKED_MSG(),event.getTimestamp());
        log.debug("Temporary result of check {}", event.getTempResult());
       // cache.evict(event.getSheetId()); // TODO: no explicit eviction any longer needed: replace by redis with TTL!

    }
    @Async
    @EventListener(ETExerciseSetFetchedFromCache.class)
    public void onApplicationEvent(ETExerciseSetFetchedFromCache event){
        buffer.fillUpIfAlmostEmpty();
    }

    @EventListener (ApplicationReadyEvent.class)
    public void onApplicationEvent (ApplicationReadyEvent event) {
        try { buffer.warmUp();}
        catch (NoTaskSetToServeException ignored) {

        }

    }
    //TODO: As well, try out: ContextRefreshedEvent, ApplicationStartedEvent
}
