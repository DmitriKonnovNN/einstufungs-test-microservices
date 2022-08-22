package solutions.dmitrikonnov.dto;

import lombok.Getter;
import solutions.dmitrikonnov.etenums.ETAufgabenNiveau;

@Getter
public class ETEndResultForFEinCaceOfException extends ETEndResultForFE {
    private final ETErgebnisseDto ergebnisseDto;
    private final String EXCEPTION_MSG =
            "Please, save the whole result report!" +
            "\n Bitte, behalten Sie den ganzen Ergebnisbericht";

    public ETEndResultForFEinCaceOfException(String id,
                                      ETAufgabenNiveau erreichtesNiveau,
                                      String zahlRichtigerAntworten,
                                      ETErgebnisseDto ergebnisseDto) {
        super(id, erreichtesNiveau, zahlRichtigerAntworten);
        this.ergebnisseDto = ergebnisseDto;
    }
}
