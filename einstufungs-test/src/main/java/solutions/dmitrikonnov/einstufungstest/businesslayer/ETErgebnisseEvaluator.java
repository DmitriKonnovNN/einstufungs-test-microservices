package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETErgebnisseDto;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETSchwellenErgebnis;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETSchwelle;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.LimitsRepo;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static solutions.dmitrikonnov.einstufungstest.domainlayer.ETSchwellenErgebnis.*;
import static solutions.dmitrikonnov.einstufungstest.utils.ResultsEvaluator.*;

@Service
@AllArgsConstructor
public class ETErgebnisseEvaluator {

    private final LimitsRepo limitsRepo;


    public ETErgebnisseDto evaluate(ETErgebnisseDto ergebnisse) {
        List<ETSchwelle> mindestSchwellen = limitsRepo.findAllByOrderByLevel() ;
        if (noneCorrect(ergebnisse)){
            mindestSchwellen.forEach(record -> setNachNiveauAlleFalsch(record.getNiveau(),ergebnisse.getNiveauZurZahlRichtiger()));
            ergebnisse.setMaxErreichtesNiveau(ETAufgabenNiveau.A0);
            return ergebnisse;
        }
        List<ETAufgabenNiveau> sortedCorrectAnswers = ergebnisse.getRichtigeLoesungenNachNiveau();

        mindestSchwellen.forEach(record ->
                countRichtigeJeNiveau(record.getNiveau(),sortedCorrectAnswers,ergebnisse.getNiveauZurZahlRichtiger()));

        var endGueltigesErgenis = evaluate2(ergebnisse,mindestSchwellen);

        return endGueltigesErgenis;
    }


    protected ETErgebnisseDto evaluate2 (ETErgebnisseDto ergebnisseDto, List<ETSchwelle> mindestSchwellen){

        var entrySetErgebnisse = ergebnisseDto.getNiveauZurZahlRichtiger().entrySet();

        // TODO: Check the performance of both implementations down below with respect to concurrent call;

        Map <ETAufgabenNiveau, ETSchwellenErgebnis> ergebnisMap = Collections.synchronizedMap(new HashMap<>());
        //Map <ETAufgabenNiveau,ETSchwellenErgebnis> ergebnisMap = new ConcurrentHashMap<>();


        mindestSchwellen.forEach(schwelle -> {
            ETAufgabenNiveau niveau = schwelle.getNiveau();
            List <ETSchwellenErgebnis> list = entrySetErgebnisse.stream().filter(naSet -> naSet.getKey().equals(niveau))
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


    private void defineMaxLevel (ETErgebnisseDto ergebnisseDto, Map<ETAufgabenNiveau,ETSchwellenErgebnis> erreichteNiveausMap) {
        List <ETAufgabenNiveau> sortedNiveaus = erreichteNiveausMap.keySet().stream().sorted().collect(Collectors.toList());
        boolean keineRichtig = false;
        boolean nichtErreicht = false;
        ETAufgabenNiveau vorangehendesNiveau = ETAufgabenNiveau.A0;
        ETAufgabenNiveau aktuellErreicht = ETAufgabenNiveau.A0;
        for (ETAufgabenNiveau sortedNiveau : sortedNiveaus) {
            if (!keineRichtig && !nichtErreicht) {
                if (erreichteNiveausMap.get(sortedNiveau).equals(KEINE_RICHTIG)) {
                    keineRichtig = true;
                    nichtErreicht = true;
                    vorangehendesNiveau = sortedNiveau;
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(NICHT_ERREICHT)) {
                    nichtErreicht = true;
                    vorangehendesNiveau = sortedNiveau;
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(KNAPP_ERREICHT)) {
                    aktuellErreicht = ETAufgabenNiveau.getNiveauMitPostfix1(sortedNiveau); // STRICT MODE
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(ERREICHT)) {
                    aktuellErreicht = ETAufgabenNiveau.getNiveauMitPostfix2(sortedNiveau);
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(ALLE_RICHTIG)) {
                    aktuellErreicht = sortedNiveau;
                    continue;
                }
            }
            if (keineRichtig) {
                if (erreichteNiveausMap.get(sortedNiveau).equals(ALLE_RICHTIG)) {
                    aktuellErreicht = ETAufgabenNiveau.getNiveauMitPostfix2(vorangehendesNiveau);
                    keineRichtig = false;
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(ERREICHT)) {
                    aktuellErreicht = ETAufgabenNiveau.getNiveauMitPostfix1(vorangehendesNiveau);
                    keineRichtig = false;
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(KNAPP_ERREICHT)){
                    keineRichtig = false;
                    continue;
                }
                else break;
            }
            if (nichtErreicht) {
                if (erreichteNiveausMap.get(sortedNiveau).equals(ALLE_RICHTIG)) {
                    aktuellErreicht = ETAufgabenNiveau.getNiveauMitPostfix1(sortedNiveau);
                    nichtErreicht = false;
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(KNAPP_ERREICHT)) {
                    aktuellErreicht = ETAufgabenNiveau.getNiveauMitPostfix1(vorangehendesNiveau);
                    nichtErreicht = false;
                    break;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(ERREICHT)) {
                    aktuellErreicht = vorangehendesNiveau;
                    nichtErreicht = false;
                } else break;
            }
        }
        ergebnisseDto.setMaxErreichtesNiveau(aktuellErreicht);
    }

    private boolean noneCorrect(ETErgebnisseDto ergebnisse){
        return ergebnisse.getZahlRichtigerAntworten().equals((short)0);
    }

    private void setNachNiveauAlleFalsch (ETAufgabenNiveau niveau, Map <ETAufgabenNiveau,Short> map) {
        map.put(niveau,(short)0);
    }

    private void countRichtigeJeNiveau(ETAufgabenNiveau niveau, List<ETAufgabenNiveau> answers, Map <ETAufgabenNiveau,Short> map) {
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
