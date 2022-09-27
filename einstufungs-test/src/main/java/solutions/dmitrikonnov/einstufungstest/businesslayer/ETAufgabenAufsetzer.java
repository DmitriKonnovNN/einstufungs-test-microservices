package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETAufgabe;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.LimitsRepo;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Die Aufgabe des Aufsetzers ist es, Aufgaben aus dem Pool für alle Niveaus und Fertigkeiten
 * im erforderlichen Umfang teilweise randomisiert zu wählen. Somit wird der Ursprungsaufgabenbogen aufgesetzt, der
 * anschließend zweimal reshuffelt wird.*/
@Service
@AllArgsConstructor
@Slf4j
public class ETAufgabenAufsetzer {

    private final ETAufgabenRepo ETAufgabenRepo;
    private final ETAufgabenRestricter aufgabenRestricter;
    private final LimitsRepo limitsRepo;

    //TODO: try out parallstream() instead of just stream()
    public List<ETAufgabe> listeAufsetzen() {
        var maxSchwellenMap = limitsRepo.findMaximumLimitByLevels();
        if(maxSchwellenMap.isEmpty()){
            log.warn("LimitsRepo is empty!");
        return Collections.emptyList();}
        var allAufgaben = ETAufgabenRepo.findAllByOrderByAufgabenNiveauAsc();
        if(allAufgaben.isEmpty()){
            log.error("No Aufgaben found!");
            return Collections.emptyList();
        }
        return allAufgaben
                .stream()
                .collect(Collectors.groupingBy(ETAufgabe::getAufgabenNiveau, TreeMap::new, Collectors.toList()))
                .values()
                .parallelStream()
                .peek(Collections::shuffle)
                .map(aufgaben->aufgabenRestricter.restrict(aufgaben,maxSchwellenMap))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

    }

}
