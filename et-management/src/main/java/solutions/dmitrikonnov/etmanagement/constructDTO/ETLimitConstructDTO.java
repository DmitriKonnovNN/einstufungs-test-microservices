package solutions.dmitrikonnov.etmanagement.constructDTO;

import lombok.Data;

import solutions.dmitrikonnov.etenums.ETTaskLevel;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ETLimitConstructDTO implements Serializable {
    private static final long serialVersionUID = -1798070786993154676L;

    private Integer id;
    @NotNull(message = "Niveau darf nicht leer sein")
    private ETTaskLevel level;
    @NotNull (message = "Es muss eine Mindestschwelle explizit festgelegt werden")
    private Integer minLimit;
    @NotNull (message = "Es muss eine Maximumschwelle explizit festgelegt werden")
    private Integer maxLimit;
}
