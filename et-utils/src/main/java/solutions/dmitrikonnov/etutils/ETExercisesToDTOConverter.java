package solutions.dmitrikonnov.etutils;

import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.dto.ETExerciseDto;
import solutions.dmitrikonnov.dto.ETItemDto;
import solutions.dmitrikonnov.etentities.ETExercise;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AufgabenToDTOConverter converts an ETAufgabe which is Entity to a ETAufgabeDTO
 * */

@Service
public class ETExercisesToDTOConverter {

    public List<ETExerciseDto> convert(List<ETExercise> exercises, Integer setHash) {
        return exercises.stream()
               .map(exercise -> convert1(exercise,setHash))
               .collect(Collectors.toList());

    }
    private  ETExerciseDto convert1 (ETExercise entity,
                                    Integer setHash) {

        return ETExerciseDto.builder()
                .exerciseDefinition(entity.getExerciseDefinition())
                .exerciseContent(entity.getExerciseContent())
                .execriseHash(entity.getExerciseId()+setHash)
                .frontEndType(entity.getFrontEndType())
                .level(entity.getExerciseLevel())
                .items(entity.getItems().stream().map(item-> ETItemDto.builder()
                        .itemId(item.getItemId()+setHash)
                        .itemExerciseContent(item.getItemExerciseContent())
                        .possibleAnswers(item.getPossibleAnswers())
                        .build()).collect(Collectors.toList()))
                .build();
    }
}

