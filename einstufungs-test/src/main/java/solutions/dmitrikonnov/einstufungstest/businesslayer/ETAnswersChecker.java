package solutions.dmitrikonnov.einstufungstest.businesslayer;


import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.dto.ETAnswerSheetDto;
import solutions.dmitrikonnov.dto.ETTaskSheet;
import solutions.dmitrikonnov.dto.ETResultsDto;

import solutions.dmitrikonnov.einstufungstest.ETAnswerSheetCheckedEvent;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.LimitsRepo;

import solutions.dmitrikonnov.etentities.ETLimit;
import solutions.dmitrikonnov.etenums.ETTaskLevel;

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

    public ETResultsDto checkSheet(ETAnswerSheetDto answersSheet, ETTaskSheet cachedExerciseSet) {


        final Map<Integer,List<String>> cachedItemToSolutions = cachedExerciseSet.getItemToSolutions();
        final ETResultsDto resultsDto = new ETResultsDto();
        final Map<Integer, ETTaskLevel> itemIdToLevel = cachedExerciseSet.getItemToLevel();
        final Integer cachedSetHash = cachedExerciseSet.getTaskSheetHash();
        final Map<Integer, List<String>> itemHashToAnswersMap = answersSheet.getItemHashToAnswerMap();
        final List<ETLimit> minLimits = limitsRepo.findAllByOrderByLevel() ;
        final List<ETTaskLevel> correctAnswersPerLevelTemp = new ArrayList<>();

        minLimits.forEach(limit -> resultsDto
                .getLevelToNumberOfCorrect()
                .put(limit.getLevel(),(short)0));

        resultsDto.setTaskSheetHash(cachedExerciseSet.getTaskSheetHash());

        itemHashToAnswersMap.forEach((hashedId, list) -> {
            var itemId = hashedId - cachedSetHash;
            var cLoesungen = cachedItemToSolutions.get(itemId);
            Boolean correct = list.equals(cLoesungen);
            resultsDto.getIdToCorrectnessMap().put(itemId, correct);
            if(correct){
                correctAnswersPerLevelTemp.add(itemIdToLevel.get(itemId));
            }
            });

        resultsDto.setNumberCorrectAnswers((short)correctAnswersPerLevelTemp.size());
        resultsDto.getCorrectAnswersPerLevel().addAll(
                correctAnswersPerLevelTemp
                        .stream()
                        .sorted()
                        .collect(Collectors.toList()));
        publisher.publishEvent(new ETAnswerSheetCheckedEvent(this, cachedSetHash,resultsDto.toString()));
        return new ETResultsDto(resultsDto);
    }

}