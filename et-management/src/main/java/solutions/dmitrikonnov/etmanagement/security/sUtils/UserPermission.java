package solutions.dmitrikonnov.etmanagement.security.sUtils;

public enum UserPermission {

    AUFGABE_READ("aufgabe:read"),
    AUFGABE_WRITE("aufgabe:write"),
    AUFGABE_DELETE("aufgabe:delete"),
    AUFGABE_ALL("aufgabe:all"),
    ITEM_READ("item:read"),
    ITEM_WRITE("item:write"),
    ITEM_DELETE("item:delete"),
    ITEM_ALL("item:all"),
    SCHWELLE_READ("schwelle:read"),
    SCHWELLE_WRITE("schwelle:wirte"),
    SCHWELLE_DELETE("schwelle:delete"),
    SCHWELLE_ALL("schwelle:all"),
    VERWALTUNG_READ("verwaltung:read"),
    VERWALTUNG_WRITE("verwaltung:write"),
    VERWALTUNG_DELETE("verwaltung:delete"),
    VERWALTUNG_ALL("verwaltung:all");



    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
