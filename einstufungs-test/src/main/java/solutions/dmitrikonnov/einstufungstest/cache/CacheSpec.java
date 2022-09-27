package solutions.dmitrikonnov.einstufungstest.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CacheSpec {

    private String name;
    private Integer timeout;
    private Integer max = 200;


}
