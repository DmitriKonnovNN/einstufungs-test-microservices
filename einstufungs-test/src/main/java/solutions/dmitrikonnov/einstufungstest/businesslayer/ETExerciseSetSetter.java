package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.dto.ETExerciseDto;
import solutions.dmitrikonnov.dto.ETExerciseSet;
import solutions.dmitrikonnov.etentities.ETExercise;
import solutions.dmitrikonnov.etenums.ETExerciseLevel;
import solutions.dmitrikonnov.etutils.ETExercisesToDTOConverter;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ETExerciseSetSetter {
    private final ETExercisesToDTOConverter converter;

    public ETExerciseSet set(List<ETExercise> exercises) {
        final Integer exerciseSetHash = exercises.hashCode();
        final var dtos = converter.convert(exercises,exerciseSetHash);
        final var shuffeled = shuffleItems(dtos);

        return ETExerciseSet.builder()
                .exerciseSetHash(exerciseSetHash)
                .itemToSolutions(extractItems(exercises))
                .itemToLevel(extractNiveaus(exercises))
                .exerciseList(shuffeled)
                .build();
    }

    private Map<Integer, List<String>> extractItems (List<ETExercise> aufgaben) {
        Map<Integer, List<String>> itemIdZuLoesungen = new HashMap<>();

        for (ETExercise aufgabe: aufgaben) {
            aufgabe.getItems().forEach(item -> {
                itemIdZuLoesungen.put(item.getItemId(), item.getSolutions());

            });
        }
        return itemIdZuLoesungen;
    }

    private Map<Integer, ETExerciseLevel> extractNiveaus (List<ETExercise> aufgaben){

        Map<Integer,ETExerciseLevel> itemIdZuNiveau = new HashMap<>();
        for (ETExercise aufgabe: aufgaben) {
            ETExerciseLevel niveau = aufgabe.getExerciseLevel();
            aufgabe.getItems().forEach(item -> {
                itemIdZuNiveau.put(item.getItemId(), niveau);
            });
        }
        return itemIdZuNiveau;
    }

    private List<ETExerciseDto> shuffleItems(List<ETExerciseDto> dtos) {
        return dtos.stream()
                .peek(aufgdto -> Collections.shuffle(aufgdto.getItems()))
                .collect(Collectors.groupingBy(ETExerciseDto::getLevel, TreeMap::new, Collectors.toList()))
                .values()
                .stream()
                .peek(Collections::shuffle)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }



}
