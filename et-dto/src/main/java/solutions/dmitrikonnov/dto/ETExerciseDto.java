package solutions.dmitrikonnov.dto;

import lombok.Builder;
import lombok.Data;
import solutions.dmitrikonnov.etenums.ETExerciseFrontEndType;
import solutions.dmitrikonnov.etenums.ETExerciseLevel;

import java.io.Serializable;
import java.util.List;

/**
 * DTO only to be sent to FrontEnd
 * */
@Data
@Builder
public class ETExerciseDto implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;
    // private final Integer exerciseSetHash;
    private Integer execriseHash; // AufgabeId AufgabenBogenId
    private String exerciseDefinition;
    private String exerciseContent;
    private ETExerciseLevel level;
    private List<ETItemDto> items;
    private ETExerciseFrontEndType frontEndType;

}
