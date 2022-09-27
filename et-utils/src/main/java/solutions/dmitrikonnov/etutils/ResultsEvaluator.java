package solutions.dmitrikonnov.etutils;

import solutions.dmitrikonnov.etentities.ETLimit;
import solutions.dmitrikonnov.etenums.ETLimitResult;

import static solutions.dmitrikonnov.etenums.ETLimitResult.*;


public interface ResultsEvaluator extends BiEvaluater<ETLimit,Short, ETLimitResult> {

    static ResultsEvaluator isAllCorrect(){
        return (limit, correct)-> (correct>=(limit.getMaxLimit())) ?
                ALL_CORRECT : NONE_CORRECT;
    }
    static ResultsEvaluator isReached(){
        return  (limit, correct)-> (correct>limit.getMinLimit()) ?
                ENOUGH: NONE_CORRECT;
    }
    static ResultsEvaluator isJustReached(){
        return  (limit, correct)-> (correct.equals(limit.getMinLimit())) ?
                JUST_ENOUGH : NONE_CORRECT;
    }
    static ResultsEvaluator isNotReached(){
        return  (limit, correct)-> (correct <limit.getMinLimit() && correct >0)?
                NOT_ENOUGH : NONE_CORRECT;
    }
    default ResultsEvaluator or (ResultsEvaluator other){
        return (limit, correct) -> {
            ETLimitResult result = this.evaluate(limit,correct);
            return result.equals(NONE_CORRECT) ? other.evaluate(limit,correct) : result;
        };
    }
}


