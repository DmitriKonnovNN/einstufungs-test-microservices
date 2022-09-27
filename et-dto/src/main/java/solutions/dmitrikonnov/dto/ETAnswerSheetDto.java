package solutions.dmitrikonnov.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * This class should be used as DTO only
 * */
@Data
public class ETAnswerSheetDto implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;
    @JsonProperty("answerSheetId")
    private final Integer answerSheetId;
    @JsonProperty("itemHashToAnswerMap")
    private final Map<Integer, List<String>> itemHashToAnswerMap;
    @JsonProperty ("createdAt")
    private final long createdAt;

}
