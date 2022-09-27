package solutions.dmitrikonnov.einstufungstest.businesslayer;


import lombok.extern.slf4j.Slf4j;
import solutions.dmitrikonnov.etentities.ETTask;
import solutions.dmitrikonnov.etenums.ETTaskLevel;

import java.util.List;
import java.util.Map;

@Slf4j
public class ETTaskRestricterSimple implements ETTaskRestricter {
    private static final int aufgabenzahlProNiveau = 3;

    @Override
    public List<ETTask> restrict(List<ETTask> selectedAndReshuffeled, Map<ETTaskLevel, Short> niveauToMax) {
        log.error("Method is not implemented! No restriction occured!");
        try {
            throw new RuntimeException("List<ETAufgabe> restrict(List<ETAufgabe> selectedAndReshuffeled, Map<ETAufgabenNiveau, Integer> niveauToMax) ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return selectedAndReshuffeled;
    }

    @Override
    public List<ETTask> restrict(List<ETTask> selectedAndReshuffeled) {
        selectedAndReshuffeled.subList(aufgabenzahlProNiveau,selectedAndReshuffeled.size()).clear();
        return selectedAndReshuffeled;
    }

}


