package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solutions.dmitrikonnov.dto.ETAnswerSheetDto;
import solutions.dmitrikonnov.dto.ETEndResultForFE;
import solutions.dmitrikonnov.dto.ETEndResultForFEinCaceOfException;
import solutions.dmitrikonnov.dto.ETTaskSheet;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.ETResultsConverterAndPersister;
import solutions.dmitrikonnov.etentities.ETTask;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@AllArgsConstructor
@Slf4j
public class ETTaskService {

    private final ETTaskSetter exSetter;
    private final ETAnswersChecker checker;
    private final ETResultsEvaluator evaluator;
    private final ETResultsConverterAndPersister converterAndPersister;
    private final ETExerciseSheetSetter setSetter;


    @Transactional(readOnly = true)
    public ETTaskSheet getTaskSet(){
        List<ETTask> aufgesetzteListe = exSetter.setList();
        if(aufgesetzteListe.isEmpty()) return null;
        ETTaskSheet set = setSetter.set(aufgesetzteListe);
        return set;
    }


    @Transactional
    public ETEndResultForFE checkAnswerSheetAndGetTestResults(ETAnswerSheetDto answerSheet, ETTaskSheet chachedAufgabenBogen) {
        var resultsDto = checker.checkSheet(answerSheet, chachedAufgabenBogen);
        var resultsDto1 = evaluator.evaluate(resultsDto);
        Future<String> resultsUUID = converterAndPersister.convertAndPersist(resultsDto1);

        try {
            return ETEndResultForFE.builder()
                    .reachedLevel(resultsDto1.getMaxReachedLevel())
                    .numberCorrectAnswers(resultsDto1.getNumberCorrectAnswers().toString())
                    .id(resultsUUID.get(5, TimeUnit.SECONDS))
                    .build();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error(Arrays.toString(e.getStackTrace()));//TODO: we need here some logic to notify person in charge about the fact of data base's gone.
            resultsUUID.cancel(true);
            return new ETEndResultForFEinCaceOfException(
                    resultsDto1.getId(),
                    resultsDto1.getMaxReachedLevel(),
                    resultsDto1.getNumberCorrectAnswers().toString(),
                    resultsDto1);
        }
    }

}

