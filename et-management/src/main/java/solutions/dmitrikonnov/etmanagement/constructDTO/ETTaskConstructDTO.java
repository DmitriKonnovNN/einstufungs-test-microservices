package solutions.dmitrikonnov.etmanagement.constructDTO;

import lombok.Data;
import solutions.dmitrikonnov.etenums.ETTaskFrontEndType;
import solutions.dmitrikonnov.etenums.ETTaskLevel;
import solutions.dmitrikonnov.etenums.ETTaskType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * this class is suposed to be sent from client to set up the task (ETAufgabe)
 * */

@Data
public class ETTaskConstructDTO implements Serializable {
    private static final long serialVersionUID = -1798070786993154676L;


    @NotBlank
    private String taskDefinition;
    @NotNull
    private List<ETItemConstructDTO> items;

    private String taskContent;

    private ETTaskType taskType;
    @NotNull
    private ETTaskLevel taskLevel;

    private ETTaskFrontEndType frontEndType;

    private Integer weigh;

}
