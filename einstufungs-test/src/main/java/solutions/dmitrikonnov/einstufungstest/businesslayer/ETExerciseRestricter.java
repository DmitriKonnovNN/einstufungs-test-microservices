package solutions.dmitrikonnov.einstufungstest.businesslayer;

import solutions.dmitrikonnov.etentities.ETExercise;
import solutions.dmitrikonnov.etenums.ETExerciseLevel;

import java.util.List;
import java.util.Map;

public interface ETExerciseRestricter {

    public List<ETExercise> restrict(List<ETExercise> selectedAndReshuffeled, Map<ETExerciseLevel,Short> levelToMax);
    public List<ETExercise> restrict(List<ETExercise> selectedAndReshuffeled);
}
