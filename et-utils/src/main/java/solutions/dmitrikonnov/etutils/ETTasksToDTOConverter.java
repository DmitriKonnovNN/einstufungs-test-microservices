package solutions.dmitrikonnov.etutils;

import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.dto.ETTaskDto;
import solutions.dmitrikonnov.dto.ETItemDto;
import solutions.dmitrikonnov.etentities.ETTask;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AufgabenToDTOConverter converts an ETAufgabe which is Entity to a ETAufgabeDTO
 * */

@Service
public class ETTasksToDTOConverter {

    public List<ETTaskDto> convert(List<ETTask> tasks, Integer setHash) {
        return tasks.stream()
               .map(task -> convert1(task,setHash))
               .collect(Collectors.toList());

    }
    private ETTaskDto convert1 (ETTask entity,
                                Integer setHash) {

        return ETTaskDto.builder()
                .taskDefinition(entity.getTaskDefinition())
                .taskContent(entity.getTaskContent())
                .taskHash(entity.getTaskId()+setHash)
                .frontEndType(entity.getFrontEndType())
                .level(entity.getTaskLevel())
                .items(entity.getItems().stream().map(item-> ETItemDto.builder()
                        .itemId(item.getItemId()+setHash)
                        .itemTaskContent(item.getItemTaskContent())
                        .possibleAnswers(item.getPossibleAnswers())
                        .build()).collect(Collectors.toList()))
                .build();
    }
}

