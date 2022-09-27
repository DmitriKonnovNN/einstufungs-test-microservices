package solutions.dmitrikonnov.dto;

import lombok.Builder;
import lombok.Getter;
import solutions.dmitrikonnov.etenums.ETTaskLevel;

@Builder
@Getter
public class ETEndResultForFE {
    private final String id;

    private final ETTaskLevel reachedLevel;

    private final String numberCorrectAnswers;
}
