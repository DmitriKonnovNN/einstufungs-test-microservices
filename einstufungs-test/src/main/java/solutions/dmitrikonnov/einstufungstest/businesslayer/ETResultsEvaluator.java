package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.dto.ETResultsDto;

import solutions.dmitrikonnov.einstufungstest.persistinglayer.LimitsRepo;
import solutions.dmitrikonnov.etentities.ETLimit;
import solutions.dmitrikonnov.etenums.ETExerciseLevel;
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


    public ETResultsDto evaluate(ETResultsDto ergebnisse) {
        List<ETLimit> mindestSchwellen = limitsRepo.findAllByOrderByLevel() ;
        if (noneCorrect(ergebnisse)){
            mindestSchwellen.forEach(record -> setNachNiveauAlleFalsch(record.getLevel(),ergebnisse.getLevelToNumberOfCorrect()));
            ergebnisse.setMaxReachedLevel(ETExerciseLevel.A0);
            return ergebnisse;
        }
        List<ETExerciseLevel> sortedCorrectAnswers = ergebnisse.getCorrectAnswersPerLevel();

        mindestSchwellen.forEach(record ->
                countRichtigeJeNiveau(record.getLevel(),sortedCorrectAnswers,ergebnisse.getLevelToNumberOfCorrect()));

        var endGueltigesErgenis = evaluate2(ergebnisse,mindestSchwellen);

        return endGueltigesErgenis;
    }


    protected ETResultsDto evaluate2 (ETResultsDto ergebnisseDto, List<ETLimit> mindestSchwellen){

        var entrySetErgebnisse = ergebnisseDto.getLevelToNumberOfCorrect().entrySet();

        // TODO: Check the performance of both implementations down below with respect to concurrent call;

        Map <ETExerciseLevel, ETLimitResult> ergebnisMap = Collections.synchronizedMap(new HashMap<>());
        //Map <ETExerciseLevel,ETSchwellenErgebnis> ergebnisMap = new ConcurrentHashMap<>();


        mindestSchwellen.forEach(schwelle -> {
            ETExerciseLevel niveau = schwelle.getLevel();
            List <ETLimitResult> list = entrySetErgebnisse.stream().filter(naSet -> naSet.getKey().equals(niveau))
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
        defineMaxLevel(ergebnisseDto,ergebnisMap);
        return ergebnisseDto;
    }


    private void defineMaxLevel (ETResultsDto ergebnisseDto, Map<ETExerciseLevel, ETLimitResult> erreichteNiveausMap) {
        List <ETExerciseLevel> sortedNiveaus = erreichteNiveausMap.keySet().stream().sorted().collect(Collectors.toList());
        boolean keineRichtig = false;
        boolean nichtErreicht = false;
        ETExerciseLevel vorangehendesNiveau = ETExerciseLevel.A0;
        ETExerciseLevel aktuellErreicht = ETExerciseLevel.A0;
        for (ETExerciseLevel sortedNiveau : sortedNiveaus) {
            if (!keineRichtig && !nichtErreicht) {
                if (erreichteNiveausMap.get(sortedNiveau).equals(NONE_CORRECT)) {
                    keineRichtig = true;
                    nichtErreicht = true;
                    vorangehendesNiveau = sortedNiveau;
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(NOT_ENOUGH)) {
                    nichtErreicht = true;
                    vorangehendesNiveau = sortedNiveau;
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(JUST_ENOUGH)) {
                    aktuellErreicht = ETExerciseLevel.getNiveauMitPostfix1(sortedNiveau); // STRICT MODE
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(ENOUGH)) {
                    aktuellErreicht = ETExerciseLevel.getNiveauMitPostfix2(sortedNiveau);
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(ALL_CORRECT)) {
                    aktuellErreicht = sortedNiveau;
                    continue;
                }
            }
            if (keineRichtig) {
                if (erreichteNiveausMap.get(sortedNiveau).equals(ALL_CORRECT)) {
                    aktuellErreicht = ETExerciseLevel.getNiveauMitPostfix2(vorangehendesNiveau);
                    keineRichtig = false;
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(ENOUGH)) {
                    aktuellErreicht = ETExerciseLevel.getNiveauMitPostfix1(vorangehendesNiveau);
                    keineRichtig = false;
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(JUST_ENOUGH)){
                    keineRichtig = false;
                    continue;
                }
                else break;
            }
            if (nichtErreicht) {
                if (erreichteNiveausMap.get(sortedNiveau).equals(ALL_CORRECT)) {
                    aktuellErreicht = ETExerciseLevel.getNiveauMitPostfix1(sortedNiveau);
                    nichtErreicht = false;
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(JUST_ENOUGH)) {
                    aktuellErreicht = ETExerciseLevel.getNiveauMitPostfix1(vorangehendesNiveau);
                    nichtErreicht = false;
                    break;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(ENOUGH)) {
                    aktuellErreicht = vorangehendesNiveau;
                    nichtErreicht = false;
                } else break;
            }
        }
        ergebnisseDto.setMaxReachedLevel(aktuellErreicht);
    }

    private boolean noneCorrect(ETResultsDto ergebnisse){
        return ergebnisse.getNumberCorrectAnswers().equals((short)0);
    }

    private void setNachNiveauAlleFalsch (ETExerciseLevel niveau, Map <ETExerciseLevel,Short> map) {
        map.put(niveau,(short)0);
    }

    private void countRichtigeJeNiveau(ETExerciseLevel niveau, List<ETExerciseLevel> answers, Map <ETExerciseLevel,Short> map) {
        Short richtige = (short)answers.stream().filter(niveau::equals).mapToInt(value -> 1).sum();

        map.put(niveau,richtige);
    }

    //.map(naSet -> evaluateLevel.apply(naSet.getValue(),schwelle))
     /*private final BiPredicate <ETSchwelle,Short> alleRichtig = (schwelle, richtig) -> richtig.equals(schwelle.getMaximumSchwelle());
    private final BiPredicate<ETSchwelle,Short> erreicht = (schwelle, richtig) -> richtig > schwelle.getMindestSchwelle();
    private final BiPredicate<ETSchwelle,Short> knappErreicht = (schwelle,richtig) -> richtig.equals(schwelle.getMindestSchwelle());
    private final BiPredicate<ETSchwelle,Short> nichtErreicht = (schwelle,richtig) -> richtig < schwelle.getMindestSchwelle() && richtig > 0;
    private final BiPredicate<ETSchwelle,Short> keineRichtig = (schwelle,richtig) -> richtig == 0;*/

    /*
    private final BiFunction<Short, ETSchwelle, ETSchwellenErgebnis> evaluateLevel = (richtig, schwelle) -> {

        return allright
                .or(reached)
                .or(allmostReached)
                .or(notReached)
                .test(schwelle,richtig);

//        if(alleRichtig.test(schwelle,richtig)) return ALLE_RICHTIG;
//        if(erreicht.test(schwelle,richtig)) return ERREICHT;
//        if(knappErreicht.test(schwelle,richtig)) return KNAPP_ERREICHT;
//        if(nichtErreicht.test(schwelle,richtig)) return NICHT_ERREICHT;
//        return KEINE_RICHTIG;
    };
*/

}
