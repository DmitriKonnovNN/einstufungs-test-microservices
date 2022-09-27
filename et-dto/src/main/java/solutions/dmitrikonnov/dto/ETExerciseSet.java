package solutions.dmitrikonnov.dto;

import lombok.*;
import solutions.dmitrikonnov.etenums.ETExerciseLevel;

import java.util.List;
import java.util.Map;

/**
 * Der Bogen wird einmal im BackEnd nach dem Reshuffle aufgesetzt. Hiervon werden einzelne Aufgaben abgerufen.
 * Die Reihenfolge der Aufgaben soll während der Session beibehalten werden. Dazu muss ein Cache implementiert sein.
 * Die Id wird dem Nutzer zuteil und abschließend in den Antwortbogen eingetragen.
 * */

@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class ETExerciseSet {

    private final Integer exerciseSetHash;
    private final List<ETExerciseDto> exerciseList;
    private long cachedAt;
    private final Map<Integer, List<String>> itemToSolutions;
    private final Map<Integer, ETExerciseLevel> itemToLevel;

}
