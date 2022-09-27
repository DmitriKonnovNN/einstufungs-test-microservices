package solutions.dmitrikonnov.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ETTaskSheetDto implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;
    private final Integer taskSheetId;
    private final List<ETTaskDto> taskList;
    private final long createdAt;
}
