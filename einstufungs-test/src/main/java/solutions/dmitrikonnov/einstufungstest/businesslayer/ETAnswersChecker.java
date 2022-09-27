package solutions.dmitrikonnov.einstufungstest.businesslayer;


import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.dto.ETAnswerSheetDto;
import solutions.dmitrikonnov.dto.ETExerciseSet;
import solutions.dmitrikonnov.dto.ETResultsDto;

import solutions.dmitrikonnov.einstufungstest.AntwortBogenCheckedEvent;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.LimitsRepo;

import solutions.dmitrikonnov.etentities.ETLimit;
import solutions.dmitrikonnov.etenums.ETExerciseLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The Service checks the correctness of answers.
 * */

@Service
@AllArgsConstructor
public class ETAnswersChecker {

    private final ApplicationEventPublisher publisher;
    private final LimitsRepo limitsRepo;

    public ETResultsDto checkSheet(ETAnswerSheetDto antwortBogen, ETExerciseSet cachedAufgabenBogen) {


        final Map<Integer,List<String>> cachedItemZuloesungen = cachedAufgabenBogen.getItemToSolutions();
        final ETResultsDto ergebnisseDto = new ETResultsDto();
        final Map<Integer, ETExerciseLevel> itemIdZuNiveau = cachedAufgabenBogen.getItemToLevel();
        final Integer cachedBogenHash = cachedAufgabenBogen.getExerciseSetHash();
        final Map<Integer, List<String>> itemHashZuAntwortMap = antwortBogen.getItemHashToAnswerMap();
        final List<ETLimit> mindestSchwellen = limitsRepo.findAllByOrderByLevel() ;
        final List<ETExerciseLevel> richtigeLoesungenNachNiveauTemp = new ArrayList<>();

        mindestSchwellen.forEach(schwelle -> ergebnisseDto
                .getLevelToNumberOfCorrect()
                .put(schwelle.getLevel(),(short)0));

        ergebnisseDto.setExerciseSetHash(cachedAufgabenBogen.getExerciseSetHash());

        itemHashZuAntwortMap.forEach((hashedId, list) -> {
            var itemId = hashedId - cachedBogenHash;
            var cLoesungen = cachedItemZuloesungen.get(itemId);
            Boolean correct = list.equals(cLoesungen);
            ergebnisseDto.getIdToCorrectnessMap().put(itemId, correct);
            if(correct){
                richtigeLoesungenNachNiveauTemp.add(itemIdZuNiveau.get(itemId));
            }
            });

        ergebnisseDto.setNumberCorrectAnswers((short)richtigeLoesungenNachNiveauTemp.size());
        ergebnisseDto.getCorrectAnswersPerLevel().addAll(
                richtigeLoesungenNachNiveauTemp
                        .stream()
                        .sorted()
                        .collect(Collectors.toList()));
        publisher.publishEvent(new AntwortBogenCheckedEvent(this, cachedBogenHash,ergebnisseDto.toString()));
        return new ETResultsDto(ergebnisseDto);
    }

}