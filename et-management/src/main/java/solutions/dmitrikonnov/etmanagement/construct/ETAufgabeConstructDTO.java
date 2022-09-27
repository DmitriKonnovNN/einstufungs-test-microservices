package solutions.dmitrikonnov.etmanagement.construct;

import lombok.Data;
import solutions.dmitrikonnov.etenums.ETExerciseFrontEndType;
import solutions.dmitrikonnov.etenums.ETExerciseLevel;
import solutions.dmitrikonnov.etenums.ETExerciseTyp;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * this class is suposed to be sent from client to set up the task (ETAufgabe)
 * */

@Data
public class ETAufgabeConstructDTO implements Serializable {
    private static final long serialVersionUID = -1798070786993154676L;


    @NotBlank
    private String aufgabenStellung;
    @NotNull
    private List<ETItemConstructDTO> items;

    private String aufgabenInhalt;

    private ETExerciseTyp aufgabenTyp;
    @NotNull
    private ETExerciseLevel aufgabenNiveau;

    private ETExerciseFrontEndType frontEndType;

    private Integer gewichtung;

}
