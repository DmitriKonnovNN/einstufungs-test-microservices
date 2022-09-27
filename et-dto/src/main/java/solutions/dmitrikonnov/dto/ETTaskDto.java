package solutions.dmitrikonnov.dto;

import lombok.Builder;
import lombok.Data;
import solutions.dmitrikonnov.etenums.ETTaskFrontEndType;
import solutions.dmitrikonnov.etenums.ETTaskLevel;

import java.io.Serializable;
import java.util.List;

/**
 * DTO only to be sent to FrontEnd
 * */
@Data
@Builder
public class ETTaskDto implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;
    // private final Integer taskSheetHash;
    private Integer taskHash; // AufgabeId AufgabenBogenId
    private String taskDefinition;
    private String taskContent;
    private ETTaskLevel level;
    private List<ETItemDto> items;
    private ETTaskFrontEndType frontEndType;

}
