package solutions.dmitrikonnov.einstufungstest;

import org.springframework.context.ApplicationEvent;

public class AufgabenBogenFetchedFromCache extends ApplicationEvent {
    public AufgabenBogenFetchedFromCache(Object source) {
        super(source);
    }
}
