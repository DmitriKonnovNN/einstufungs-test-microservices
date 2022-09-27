package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.dto.ETTaskDto;
import solutions.dmitrikonnov.dto.ETTaskSheet;
import solutions.dmitrikonnov.etentities.ETTask;
import solutions.dmitrikonnov.etenums.ETTaskLevel;
import solutions.dmitrikonnov.etutils.ETTasksToDTOConverter;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ETExerciseSheetSetter {
    private final ETTasksToDTOConverter converter;

    public ETTaskSheet set(List<ETTask> exercises) {
        final Integer exerciseSetHash = exercises.hashCode();
        final var dtos = converter.convert(exercises,exerciseSetHash);
        final var shuffeled = shuffleItems(dtos);

        return ETTaskSheet.builder()
                .taskSheetHash(exerciseSetHash)
                .itemToSolutions(extractItems(exercises))
                .itemToLevel(extractNiveaus(exercises))
                .taskList(shuffeled)
                .build();
    }

    private Map<Integer, List<String>> extractItems (List<ETTask> aufgaben) {
        Map<Integer, List<String>> itemIdZuLoesungen = new HashMap<>();

        for (ETTask aufgabe: aufgaben) {
            aufgabe.getItems().forEach(item -> {
                itemIdZuLoesungen.put(item.getItemId(), item.getSolutions());

            });
        }
        return itemIdZuLoesungen;
    }

    private Map<Integer, ETTaskLevel> extractNiveaus (List<ETTask> aufgaben){

        Map<Integer, ETTaskLevel> itemIdZuNiveau = new HashMap<>();
        for (ETTask aufgabe: aufgaben) {
            ETTaskLevel niveau = aufgabe.getTaskLevel();
            aufgabe.getItems().forEach(item -> {
                itemIdZuNiveau.put(item.getItemId(), niveau);
            });
        }
        return itemIdZuNiveau;
    }

    private List<ETTaskDto> shuffleItems(List<ETTaskDto> dtos) {
        return dtos.stream()
                .peek(aufgdto -> Collections.shuffle(aufgdto.getItems()))
                .collect(Collectors.groupingBy(ETTaskDto::getLevel, TreeMap::new, Collectors.toList()))
                .values()
                .stream()
                .peek(Collections::shuffle)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }



}
