package solutions.dmitrikonnov.etmanagement.security.sUtils;

public enum UserPermission {

    TASK_READ("task:read"),
    TASK_WRITE("task:write"),
    TASK_DELETE("task:delete"),
    TASK_ALL("task:all"),
    ITEM_READ("item:read"),
    ITEM_WRITE("item:write"),
    ITEM_DELETE("item:delete"),
    ITEM_ALL("item:all"),
    LIMIT_READ("limit:read"),
    LIMIT_WRITE("limit:wirte"),
    LIMIT_DELETE("limit:delete"),
    LIMIT_ALL("limit:all"),
    MANAGEMENT_READ("management:read"),
    MANAGEMENT_WRITE("management:write"),
    MANAGEMENT_DELETE("management:delete"),
    MANAGEMENT_REGISTER("management:register"),
    MANAGEMENT_ALL("management:all");



    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
