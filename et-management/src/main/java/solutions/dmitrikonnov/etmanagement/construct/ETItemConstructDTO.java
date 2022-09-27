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
    @JsonProperty("itemAufgabenInhalt")
    private String itemAufgabenInhalt;
    @NotNull
    @JsonProperty("moeglicheAntworten")
    private List<String> moeglicheAntworten;
    @NotNull
    @JsonProperty ("loesungen")
    private List<String> loesungen;

}
