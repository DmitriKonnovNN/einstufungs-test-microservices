package solutions.dmitrikonnov.etutils;

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a function that accepts two arguments and returns a result of concerned evaluation.
 *
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <R> the type of the result of the evaluation
 *
 * @see Function
 *
 */
@FunctionalInterface
public interface BiEvaluater<T,U,R> {
    R evaluate(T t, U u);

    default <V> BiEvaluater<T, U, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t, U u) -> after.apply(evaluate(t, u));
    }
}
