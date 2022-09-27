package solutions.dmitrikonnov.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ETItemDto implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;
    private Integer itemId;
    private String itemTaskContent;
    private Set<String> possibleAnswers;

}
