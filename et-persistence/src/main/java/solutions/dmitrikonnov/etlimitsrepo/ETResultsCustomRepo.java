package solutions.dmitrikonnov.etlimitsrepo;

import java.util.Date;
import java.util.Map;


public interface ETResultsCustomRepo {
    Map<Integer,Boolean> findAllMapsItemCorrectness(Date createdOn);

}