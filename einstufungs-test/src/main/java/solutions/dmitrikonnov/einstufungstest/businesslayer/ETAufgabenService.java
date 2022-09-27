package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solutions.dmitrikonnov.dto.ETAnswerSheetDto;
import solutions.dmitrikonnov.dto.ETEndResultForFE;
import solutions.dmitrikonnov.dto.ETEndResultForFEinCaceOfException;
import solutions.dmitrikonnov.dto.ETExerciseSet;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.ETResultsConverterAndPersister;
import solutions.dmitrikonnov.etentities.ETExercise;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@AllArgsConstructor
@Slf4j
public class ETAufgabenService {

    private final ETExerciseSetter exSetter;
    private final ETAnswersChecker pruefer;
    private final ETResultsEvaluator evaluator;
    private final ETResultsConverterAndPersister converterAndPersister;
    private final ETExerciseSetSetter setSetter;


    @Transactional(readOnly = true)
    public ETExerciseSet getAufgabenListe (){
        List<ETExercise> aufgesetzteListe = exSetter.setList();
        if(aufgesetzteListe.isEmpty()) return null;
        ETExerciseSet set = setSetter.set(aufgesetzteListe);
        return set;
    }


    @Transactional
    public ETEndResultForFE checkAntwortBogenAndGetTestErgebnisse (ETAnswerSheetDto answerSheet, ETExerciseSet chachedAufgabenBogen) {
        var ergebnisseDto = pruefer.checkSheet(answerSheet, chachedAufgabenBogen);
        var ergebnisseDto1 = evaluator.evaluate(ergebnisseDto);
        Future<String> ergebnisseUUID = converterAndPersister.convertAndPersist(ergebnisseDto1);

        try {
            return ETEndResultForFE.builder()
                    .reachedLevel(ergebnisseDto1.getMaxErreichtesNiveau())
                    .zahlRichtigerAntworten(ergebnisseDto1.getZahlRichtigerAntworten().toString())
                    .id(ergebnisseUUID.get(5, TimeUnit.SECONDS))
                    .build();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error(Arrays.toString(e.getStackTrace()));//TODO: we need here some logic to notify person in charge about the fact of data base's gone.
            ergebnisseUUID.cancel(true);
            return new ETEndResultForFEinCaceOfException(
                    ergebnisseDto1.getId(),
                    ergebnisseDto1.getMaxErreichtesNiveau(),
                    ergebnisseDto1.getZahlRichtigerAntworten().toString(),
                    ergebnisseDto1);
        }
    }

}

