package solutions.dmitrikonnov.etverwaltung.construct;

import lombok.Data;
import solutions.dmitrikonnov.etenums.ETAufgabenNiveau;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ETSchwellenConstructDTO implements Serializable {
    private static final long serialVersionUID = -1798070786993154676L;

    private Integer id;
    @NotNull(message = "Niveau darf nicht leer sein")
    private ETAufgabenNiveau niveau;
    @NotNull (message = "Es muss eine Mindestschwelle explizit festgelegt werden")
    private Integer mindestSchwelle;
    @NotNull (message = "Es muss eine Maximumschwelle explizit festgelegt werden")
    private Integer maximumSchwelle;
}
