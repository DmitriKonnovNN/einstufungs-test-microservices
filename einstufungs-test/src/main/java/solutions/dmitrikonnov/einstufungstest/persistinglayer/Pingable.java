package solutions.dmitrikonnov.einstufungstest.persistinglayer;



public interface Pingable {

    <T> int ping(Class<T> clazz);
}
