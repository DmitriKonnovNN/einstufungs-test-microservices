package solutions.dmitrikonnov.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import solutions.dmitrikonnov.etenums.ETExerciseLevel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ETResultsDto implements Serializable {
    private static final long serialVersionUID = -1798070786993154676L;

    public ETResultsDto(ETResultsDto resultsDto) {
        this.id = resultsDto.getId();
        this.exerciseSetHash = resultsDto.getExerciseSetHash();
        this.maxReachedLevel = resultsDto.getMaxReachedLevel();
        this.numberCorrectAnswers = resultsDto.getNumberCorrectAnswers();
        this.correctAnswersPerLevel = resultsDto.getCorrectAnswersPerLevel();
        this.idToCorrectnessMap = resultsDto.getIdToCorrectnessMap();
        this.levelToNumberOfCorrect = resultsDto.getLevelToNumberOfCorrect();

    }

    private  String id;
    private  Integer exerciseSetHash;
    private  ETExerciseLevel maxReachedLevel;
    private  Short numberCorrectAnswers;
    private  List<ETExerciseLevel> correctAnswersPerLevel = new ArrayList<>();
    private  Map<Integer, Boolean> idToCorrectnessMap = new HashMap<>();
    private  Map<ETExerciseLevel, Short> levelToNumberOfCorrect = new HashMap<>();

}
