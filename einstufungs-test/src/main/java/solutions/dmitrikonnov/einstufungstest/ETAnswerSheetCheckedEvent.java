package solutions.dmitrikonnov.einstufungstest;


import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ETAnswerSheetCheckedEvent extends ApplicationEvent {

    private final String CHECKED_MSG = "Antwortbogen has been checked. Cache is ready to get evicted";
    private final String tempResult;
    private final Integer sheetId;

    public ETAnswerSheetCheckedEvent(Object source, Integer sheetId, String msg) {
        super(source);
        this.sheetId = sheetId;
        this.tempResult = msg;
    }
}
