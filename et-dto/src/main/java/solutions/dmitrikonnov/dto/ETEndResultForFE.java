package solutions.dmitrikonnov.dto;

import lombok.Builder;
import lombok.Getter;
import solutions.dmitrikonnov.etenums.ETExerciseLevel;

@Builder
@Getter
public class ETEndResultForFE {
    private final String id;

    private final ETExerciseLevel reachedLevel;

    private final String numberCorrectAnswers;
}
