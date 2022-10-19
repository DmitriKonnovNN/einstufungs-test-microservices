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
                .itemToLevel(extractLevels(exercises))
                .taskList(shuffeled)
                .build();
    }

    private Map<Integer, List<String>> extractItems (List<ETTask> tasks) {
        Map<Integer, List<String>> itemIdToSolutions = new HashMap<>();

        for (ETTask task: tasks) {
            task.getItems().forEach(item -> {
                itemIdToSolutions.put(item.getItemId(), item.getSolutions());

            });
        }
        return itemIdToSolutions;
    }

    private Map<Integer, ETTaskLevel> extractLevels(List<ETTask> tasks){

        Map<Integer, ETTaskLevel> itemIdToLevel = new HashMap<>();
        for (ETTask task: tasks) {
            ETTaskLevel level = task.getTaskLevel();
            task.getItems().forEach(item -> {
                itemIdToLevel.put(item.getItemId(), level);
            });
        }
        return itemIdToLevel;
    }

    private List<ETTaskDto> shuffleItems(List<ETTaskDto> dtos) {
        return dtos.stream()
                .peek(taskdto -> Collections.shuffle(taskdto.getItems()))
                .collect(Collectors.groupingBy(ETTaskDto::getLevel, TreeMap::new, Collectors.toList()))
                .values()
                .stream()
                .peek(Collections::shuffle)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }



}
