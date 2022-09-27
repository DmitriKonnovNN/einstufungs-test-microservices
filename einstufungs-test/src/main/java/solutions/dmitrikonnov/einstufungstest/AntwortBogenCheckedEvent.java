package solutions.dmitrikonnov.einstufungstest;


import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AntwortBogenCheckedEvent extends ApplicationEvent {

    private final String CHECKED_MSG = "Antwortbogen has been checked. Cache is ready to get evicted";
    private final String tempResult;
    private final Integer bogenId;

    public AntwortBogenCheckedEvent(Object source, Integer bogenId ,String msg) {
        super(source);
        this.bogenId = bogenId;
        this.tempResult = msg;
    }
}
