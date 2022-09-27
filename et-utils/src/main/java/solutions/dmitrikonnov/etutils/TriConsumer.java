package solutions.dmitrikonnov.etutils;

@FunctionalInterface
public interface TriConsumer <T,U,Y,R>{

    void accept(T t, U u,Y y);
}
