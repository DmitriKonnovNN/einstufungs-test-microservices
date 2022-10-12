package solutions.dmitrikonnov.etlimitsrepo;



public interface Pingable {

    <T> int ping(Class<T> clazz);
}
