package solutions.dmitrikonnov.etutils;

/**
 *  * Represents a predicate (boolean-valued function) of three arguments.
 *
 *   * @param <T> the type of the first argument to the predicate
 *  * @param <U> the type of the second argument the predicate
 *  * @param <Y> the type of the third argument the predicate
 * */
@FunctionalInterface
public interface TriPredicate <T,U,Y> {

    /**
     * Evaluates this predicate on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     * @param y the third input argument
     * @return {@code true} if the input arguments match the predicate,
     * otherwise {@code false}
     */
    boolean test (T t, U u, Y y);


}
