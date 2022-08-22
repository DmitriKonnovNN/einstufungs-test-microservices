package solutions.dmitrikonnov.dto;

import lombok.Builder;
import lombok.Getter;
import solutions.dmitrikonnov.etenums.ETAufgabenNiveau;

@Builder
@Getter
public class ETEndResultForFE {
    private final String id;

    private final ETAufgabenNiveau erreichtesNiveau;

    private final String zahlRichtigerAntworten;
}
