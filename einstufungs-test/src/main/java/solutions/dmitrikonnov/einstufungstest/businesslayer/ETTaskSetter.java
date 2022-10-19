package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.etentities.ETTask;
import solutions.dmitrikonnov.etlimitsrepo.EtLimitsRepo;
import solutions.dmitrikonnov.ettaskrepo.ETTaskRepo;

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
public class ETTaskSetter {

    private final ETTaskRepo taskRepo;
    private final ETTaskRestricter taskRestricter;
    private final EtLimitsRepo limitsRepo;

    //TODO: try out parallstream() instead of just stream()
    public List<ETTask> setList() {
        var maxLimitMap = limitsRepo.findMaximumLimitByLevels();
        if(maxLimitMap.isEmpty()){
            log.warn("LimitsRepo is empty!");
        return Collections.emptyList();}
        var allTasks = taskRepo.findAllByOrderByTaskLevelAsc();
        if(allTasks.isEmpty()){
            log.error("No Aufgaben found!");
            return Collections.emptyList();
        }
        return allTasks
                .stream()
                .collect(Collectors.groupingBy(ETTask::getTaskLevel, TreeMap::new, Collectors.toList()))
                .values()
                .parallelStream()
                .peek(Collections::shuffle)
                .map(tasks-> taskRestricter.restrict(tasks,maxLimitMap))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

    }

}
