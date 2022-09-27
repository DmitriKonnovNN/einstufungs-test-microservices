package solutions.dmitrikonnov.exceptions;


import solutions.dmitrikonnov.etenums.ETTaskLevel;

public class ThresholdNotFoundException extends RuntimeException {
    private final static String SCHWELLE_NOT_FOUND_MSG =
            "Threshold for level %s not found!\n" +
            "Keine Schwelle fürs Niveau %s gefunden!";

    public ThresholdNotFoundException(ETTaskLevel level) {
        super(String.format(SCHWELLE_NOT_FOUND_MSG,level.toString(),level.toString()));
    }
}
