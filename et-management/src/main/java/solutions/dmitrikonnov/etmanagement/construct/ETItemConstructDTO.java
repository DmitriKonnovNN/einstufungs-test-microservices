package solutions.dmitrikonnov.etmanagement.construct;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class ETItemConstructDTO implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;
    @NotBlank
    @JsonProperty("itemTaskContent")
    private String itemTaskContent;
    @NotNull
    @JsonProperty("possibleSolutions")
    private List<String> possibleSolutions;
    @NotNull
    @JsonProperty ("solutions")
    private List<String> solutions;

}
