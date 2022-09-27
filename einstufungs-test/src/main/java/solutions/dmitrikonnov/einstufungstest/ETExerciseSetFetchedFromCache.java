package solutions.dmitrikonnov.einstufungstest;

import org.springframework.context.ApplicationEvent;

public class ETExerciseSetFetchedFromCache extends ApplicationEvent {
    public ETExerciseSetFetchedFromCache(Object source) {
        super(source);
    }
}
