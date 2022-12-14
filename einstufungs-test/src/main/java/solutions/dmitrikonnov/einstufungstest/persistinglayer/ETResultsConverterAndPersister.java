package solutions.dmitrikonnov.einstufungstest.persistinglayer;

import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.dto.ETResultsDto;
import solutions.dmitrikonnov.etentities.ETResults;
import solutions.dmitrikonnov.etresultsrepo.ETResultsRepo;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ETResultsConverterAndPersister {

    private final ETResultsRepo repo;

    @Async
    @Timed (value = "et.service.results-converter-persister")
    public Future<UUID> convertAndPersist(ETResultsDto resultsDto) {

        var uuid = UUID.randomUUID();

        System.out.println("convert and persist THREAD: "+ Thread.currentThread().getName());
        ETResults results = ETResults.builder()
                .id(uuid)
                .taskSheetHash(resultsDto.getTaskSheetHash())
                .numberCorrectAnswers(resultsDto.getNumberCorrectAnswers())
                .maxReachedLevel(resultsDto.getMaxReachedLevel())
                .levelToNumberCorrect(resultsDto.getLevelToNumberOfCorrect().entrySet()
                        .stream().collect(Collectors.toMap(entry->entry.getKey().toString(), Map.Entry::getValue)))
                .idToCorrectnessMap(resultsDto.getIdToCorrectnessMap())
                .build();
        log.info("ErgebnisseDto {} converted to Entity {}", resultsDto,results);

        log.info("ErgebnisseEntity {} persisted", results);
        return new AsyncResult<>(uuid);
    }

}
