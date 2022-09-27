package solutions.dmitrikonnov.einstufungstest.businesslayer;


import lombok.extern.slf4j.Slf4j;
import solutions.dmitrikonnov.etentities.ETExercise;
import solutions.dmitrikonnov.etenums.ETExerciseLevel;

import java.util.List;
import java.util.Map;

@Slf4j
public class ETExerciseRestricterSimple implements ETExerciseRestricter {
    private static final int aufgabenzahlProNiveau = 3;

    @Override
    public List<ETExercise> restrict(List<ETExercise> selectedAndReshuffeled, Map<ETExerciseLevel, Short> niveauToMax) {
        log.error("Method is not implemented! No restriction occured!");
        try {
            throw new RuntimeException("List<ETAufgabe> restrict(List<ETAufgabe> selectedAndReshuffeled, Map<ETAufgabenNiveau, Integer> niveauToMax) ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return selectedAndReshuffeled;
    }

    @Override
    public List<ETExercise> restrict(List<ETExercise> selectedAndReshuffeled) {
        selectedAndReshuffeled.subList(aufgabenzahlProNiveau,selectedAndReshuffeled.size()).clear();
        return selectedAndReshuffeled;
    }

}


