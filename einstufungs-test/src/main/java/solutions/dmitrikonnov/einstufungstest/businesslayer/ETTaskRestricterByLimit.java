package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.etentities.ETTask;
import solutions.dmitrikonnov.etenums.ETTaskLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ETTaskRestricterByLimit implements ETTaskRestricter {


    @Override
    public List<ETTask> restrict(List<ETTask> selectedAndReshuffeled, Map<ETTaskLevel,Short> levelToMax) {
        var actualLevel = selectedAndReshuffeled.get(0).getExerciseLevel();
        var actualMaxSchwelle = levelToMax.get(actualLevel);
        int itemsCounter = 0;
        final List<ETTask>strictSelected = new ArrayList<>();

        for (int i = 0; i < selectedAndReshuffeled.size() && itemsCounter<actualMaxSchwelle; i++) {
            itemsCounter += selectedAndReshuffeled.get(i).getNumberItems();
            strictSelected.add(selectedAndReshuffeled.get(i));
        }
        log.debug("Restricter: actual Maxschwelle: {}, itemsCounter {} .", actualMaxSchwelle,itemsCounter);

        return strictSelected;
    }

    @Override
    public List<ETTask> restrict(List<ETTask> selectedAndReshuffeled)  {
        log.error("Method is not implemented! No restriction occured!");
        try {
            throw new RuntimeException("Method List<ETAufgabe> restrict(List<ETAufgabe> selectedAndReshuffeled) is not implemented!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return selectedAndReshuffeled;
    }
}
