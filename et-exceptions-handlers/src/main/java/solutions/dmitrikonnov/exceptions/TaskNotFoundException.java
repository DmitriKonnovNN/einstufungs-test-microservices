package solutions.dmitrikonnov.exceptions;

public class TaskNotFoundException extends RuntimeException{
    private final static String AUFGABE_NOT_FOUND_MSG =
            "Task with id  %d not found!\n" +
                    "Keine Aufgabe mit Id %d gefunden!";

    public TaskNotFoundException(Integer id) {
        super(String.format(AUFGABE_NOT_FOUND_MSG,id,id));
    }
}
