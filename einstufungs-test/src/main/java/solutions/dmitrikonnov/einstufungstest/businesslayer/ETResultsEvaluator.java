package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.dto.ETResultsDto;

import solutions.dmitrikonnov.einstufungstest.persistinglayer.LimitsRepo;
import solutions.dmitrikonnov.etentities.ETLimit;
import solutions.dmitrikonnov.etenums.ETTaskLevel;
import solutions.dmitrikonnov.etenums.ETLimitResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static solutions.dmitrikonnov.etutils.ResultsEvaluator.*;
import static solutions.dmitrikonnov.etenums.ETLimitResult.*;

@Service
@AllArgsConstructor
public class ETResultsEvaluator {

    private final LimitsRepo limitsRepo;


    public ETResultsDto evaluate(ETResultsDto results) {
        List<ETLimit> minLimits = limitsRepo.findAllByOrderByLevel() ;
        if (noneCorrect(results)){
            minLimits.forEach(record -> setPerLevelAllWrong(record.getLevel(),results.getLevelToNumberOfCorrect()));
            results.setMaxReachedLevel(ETTaskLevel.A0);
            return results;
        }
        List<ETTaskLevel> sortedCorrectAnswers = results.getCorrectAnswersPerLevel();

        minLimits.forEach(record ->
                countCorrectPerLevel(record.getLevel(),sortedCorrectAnswers,results.getLevelToNumberOfCorrect()));

        var endGueltigesErgenis = evaluate2(results,minLimits);

        return endGueltigesErgenis;
    }


    protected ETResultsDto evaluate2 (ETResultsDto resultsDto, List<ETLimit> minLimits){

        var entrySetResults = resultsDto.getLevelToNumberOfCorrect().entrySet();

        // TODO: Check the performance of both implementations down below with respect to concurrent call;

        Map <ETTaskLevel, ETLimitResult> ergebnisMap = Collections.synchronizedMap(new HashMap<>());
        //Map <ETTaskLevel,ETSchwellenErgebnis> ergebnisMap = new ConcurrentHashMap<>();


        minLimits.forEach(schwelle -> {
            ETTaskLevel niveau = schwelle.getLevel();
            List <ETLimitResult> list = entrySetResults.stream().filter(naSet -> naSet.getKey().equals(niveau))
                    .map(naSet->
                            isAllCorrect()
                            .or(isReached())
                            .or(isJustReached())
                            .or(isNotReached())
                            .evaluate(schwelle, naSet.getValue())
                    )
                    .collect(Collectors.toUnmodifiableList());
            ergebnisMap.put(niveau,list.get(0));

        } );
        defineMaxLevel(resultsDto,ergebnisMap);
        return resultsDto;
    }


    private void defineMaxLevel (ETResultsDto ergebnisseDto, Map<ETTaskLevel, ETLimitResult> reachedLevelsMap) {
        List <ETTaskLevel> sortedNiveaus = reachedLevelsMap.keySet().stream().sorted().collect(Collectors.toList());
        boolean noneCorrect = false;
        boolean notEnough = false;
        ETTaskLevel previousLevel = ETTaskLevel.A0;
        ETTaskLevel currentReached = ETTaskLevel.A0;
        for (ETTaskLevel sortedNiveau : sortedNiveaus) {
            if (!noneCorrect && !notEnough) {
                if (reachedLevelsMap.get(sortedNiveau).equals(NONE_CORRECT)) {
                    noneCorrect = true;
                    notEnough = true;
                    previousLevel = sortedNiveau;
                    continue;
                }
                if (reachedLevelsMap.get(sortedNiveau).equals(NOT_ENOUGH)) {
                    notEnough = true;
                    previousLevel = sortedNiveau;
                    continue;
                }
                if (reachedLevelsMap.get(sortedNiveau).equals(JUST_ENOUGH)) {
                    currentReached = ETTaskLevel.getNiveauMitPostfix1(sortedNiveau); // STRICT MODE
                    continue;
                }
                if (reachedLevelsMap.get(sortedNiveau).equals(ENOUGH)) {
                    currentReached = ETTaskLevel.getNiveauMitPostfix2(sortedNiveau);
                    continue;
                }
                if (reachedLevelsMap.get(sortedNiveau).equals(ALL_CORRECT)) {
                    currentReached = sortedNiveau;
                    continue;
                }
            }
            if (noneCorrect) {
                if (reachedLevelsMap.get(sortedNiveau).equals(ALL_CORRECT)) {
                    currentReached = ETTaskLevel.getNiveauMitPostfix2(previousLevel);
                    noneCorrect = false;
                    continue;
                }
                if (reachedLevelsMap.get(sortedNiveau).equals(ENOUGH)) {
                    currentReached = ETTaskLevel.getNiveauMitPostfix1(previousLevel);
                    noneCorrect = false;
                    continue;
                }
                if (reachedLevelsMap.get(sortedNiveau).equals(JUST_ENOUGH)){
                    noneCorrect = false;
                    continue;
                }
                else break;
            }
            if (notEnough) {
                if (reachedLevelsMap.get(sortedNiveau).equals(ALL_CORRECT)) {
                    currentReached = ETTaskLevel.getNiveauMitPostfix1(sortedNiveau);
                    notEnough = false;
                    continue;
                }
                if (reachedLevelsMap.get(sortedNiveau).equals(JUST_ENOUGH)) {
                    currentReached = ETTaskLevel.getNiveauMitPostfix1(previousLevel);
                    notEnough = false;
                    break;
                }
                if (reachedLevelsMap.get(sortedNiveau).equals(ENOUGH)) {
                    currentReached = previousLevel;
                    notEnough = false;
                } else break;
            }
        }
        ergebnisseDto.setMaxReachedLevel(currentReached);
    }

    private boolean noneCorrect(ETResultsDto ergebnisse){
        return ergebnisse.getNumberCorrectAnswers().equals((short)0);
    }

    private void setPerLevelAllWrong(ETTaskLevel level, Map <ETTaskLevel,Short> map) {
        map.put(level,(short)0);
    }

    private void countCorrectPerLevel(ETTaskLevel level, List<ETTaskLevel> answers, Map <ETTaskLevel,Short> map) {
        Short richtige = (short)answers.stream().filter(level::equals).mapToInt(value -> 1).sum();

        map.put(level,richtige);
    }

}