package solutions.dmitrikonnov.einstufungstest.businesslayer;

import solutions.dmitrikonnov.etentities.ETTask;
import solutions.dmitrikonnov.etenums.ETTaskLevel;

import java.util.List;
import java.util.Map;

public interface ETTaskRestricter {

    public List<ETTask> restrict(List<ETTask> selectedAndReshuffeled, Map<ETTaskLevel,Short> levelToMax);
    public List<ETTask> restrict(List<ETTask> selectedAndReshuffeled);
}
