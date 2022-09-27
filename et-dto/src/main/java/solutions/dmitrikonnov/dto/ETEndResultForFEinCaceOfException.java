package solutions.dmitrikonnov.dto;

import lombok.Getter;
import solutions.dmitrikonnov.etenums.ETTaskLevel;

@Getter
public class ETEndResultForFEinCaceOfException extends ETEndResultForFE {
    private final ETResultsDto resultsDto;
    private final String EXCEPTION_MSG =
            "Please, save the whole result report!" +
            "\n Bitte, behalten Sie den ganzen Ergebnisbericht";

    public ETEndResultForFEinCaceOfException(String id,
                                      ETTaskLevel reachedLevel,
                                      String numberCorrectAnswers,
                                      ETResultsDto resultsDto) {
        super(id, reachedLevel, numberCorrectAnswers);
        this.resultsDto = resultsDto;
    }
}
