package solutions.dmitrikonnov.einstufungstest.cache.buffer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.dto.ETTaskDto;
import solutions.dmitrikonnov.dto.ETTaskSheet;
import solutions.dmitrikonnov.einstufungstest.businesslayer.ETTaskService;
import solutions.dmitrikonnov.exceptions.NoTaskSetToServeException;

import java.util.Objects;
import java.util.Optional;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor (onConstructor_ = {@Autowired})
public class ETBuffer {
    private final int bufferAllocSize = 12;
    private final Queue<Optional<ETTaskSheet>> toServeBuffer = new LinkedBlockingQueue<>(bufferAllocSize);
    private final ETTaskService service;

    public ETTaskSheet getPreparedTaskSheet(){

        return Objects.requireNonNull(
                toServeBuffer.poll())
                .orElseGet(this::getSetForced);
    }
    public void fillUpIfAlmostEmpty(){
        if(isAlmostEmpty()) fillUpBuffer();
    }
    @Async
    protected void fillUpBuffer(){

        for (int i = bufferAllocSize; i > 0 ; i--) {
            var exerciseSet = service.getTaskSet();
            if(exerciseSet==null) {
                log.warn("No Bogen in ServeBuffer!");
                toServeBuffer.offer(Optional.empty());
                break;
            }
            toServeBuffer.offer(Optional.of(exerciseSet));
        }
        log.warn("ServeBuffer (allocated to {} ) has been filled up with {} elements", bufferAllocSize, toServeBuffer.size());
        toServeBuffer.forEach(b-> {
            log.debug(b.orElseThrow(()->new NoTaskSetToServeException("No Bogen set up!")).getTaskSheetHash().toString());
            b.get().getTaskList().stream()
                    .map(ETTaskDto::getTaskHash)
                    .map(Object::toString)
                    .collect(Collectors.toList())
                    .forEach(log::trace);
        });
    }

    private boolean isAlmostEmpty (){
        int currentSize = toServeBuffer.size();
        log.debug("Size of Aufg-Cache : {}", currentSize);
        return currentSize < bufferAllocSize /3;
    }
    private ETTaskSheet getSetForced(){
        var bogen = service.getTaskSet();
        if(bogen == null) {
            log.error("No Bogen set up!");
            toServeBuffer.offer(Optional.empty());
            throw new NoTaskSetToServeException("No Bogen set up!");
        }
        return bogen;

    }

    public void warmUp(){
        log.debug("Buffer warm-up started");
        fillUpBuffer();
    }
}
