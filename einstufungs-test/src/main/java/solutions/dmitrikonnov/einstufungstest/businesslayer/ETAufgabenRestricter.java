package solutions.dmitrikonnov.einstufungstest.businesslayer;

import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETAufgabe;

import java.util.List;
import java.util.Map;

public interface ETAufgabenRestricter {

    public List<ETAufgabe> restrict(List<ETAufgabe> selectedAndReshuffeled, Map<ETAufgabenNiveau,Short> niveauToMax);
    public List<ETAufgabe> restrict(List<ETAufgabe> selectedAndReshuffeled);
}
