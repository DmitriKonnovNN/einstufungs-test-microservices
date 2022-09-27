package solutions.dmitrikonnov.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ETExerciseSetDto implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;
    private final Integer exerciseSetId;
    private final List<ETExerciseDto> exerciseList;
    private final long createdAt;
}
