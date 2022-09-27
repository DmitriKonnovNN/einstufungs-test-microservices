package solutions.dmitrikonnov.einstufungstest.weblayer;

import solutions.dmitrikonnov.dto.ETTaskSheet;

interface ExerciseSetCache {

    void saveToCheck(Integer id, ETTaskSheet set);

    ETTaskSheet fetch(Integer id);

    void evict(Integer id);

    ETTaskSheet getPreparedExerciseSet();

}
