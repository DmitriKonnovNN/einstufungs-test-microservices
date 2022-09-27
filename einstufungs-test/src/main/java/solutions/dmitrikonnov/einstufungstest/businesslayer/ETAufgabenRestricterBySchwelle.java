package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETAufgabe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ETAufgabenRestricterBySchwelle implements ETAufgabenRestricter {


    @Override
    public List<ETAufgabe> restrict(List<ETAufgabe> selectedAndReshuffeled, Map<ETAufgabenNiveau,Short> niveauToMax) {
        var actualNiveau = selectedAndReshuffeled.get(0).getAufgabenNiveau();
        var actualMaxSchwelle = niveauToMax.get(actualNiveau);
        int itemsCounter = 0;
        final List<ETAufgabe>strictSelected = new ArrayList<>();

        for (int i = 0; i < selectedAndReshuffeled.size() && itemsCounter<actualMaxSchwelle; i++) {
            itemsCounter += selectedAndReshuffeled.get(i).getZahlItems();
            strictSelected.add(selectedAndReshuffeled.get(i));
        }
        log.debug("Restricter: actual Maxschwelle: {}, itemsCounter {} .", actualMaxSchwelle,itemsCounter);

        return strictSelected;
    }

    @Override
    public List<ETAufgabe> restrict(List<ETAufgabe> selectedAndReshuffeled)  {
        log.error("Method is not implemented! No restriction occured!");
        try {
            throw new RuntimeException("Method List<ETAufgabe> restrict(List<ETAufgabe> selectedAndReshuffeled) is not implemented!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return selectedAndReshuffeled;
    }
}
