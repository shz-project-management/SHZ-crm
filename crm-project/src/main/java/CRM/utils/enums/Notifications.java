package CRM.utils.enums;

/**
 * enum class that creates different notifications
 */
public enum Notifications {
    ASSIGNED_TO_ME("Items assigned to me", "One or multiple items were assigned to me"),
    STATUS_CHANGED("Items status changed", "Item that is related to me has it status changed"),
    COMMENT_ADDED("Items comment added", "Item that is related to me has a new comment"),
    ITEM_DELETED("Items deleted", "Item that is related to me has been deleted"),
    ITEM_DATA_CHANGED("Items data changed", "Item that is related to me has had a change in some data"),
    USER_ADDED("User added to the system", "A new user has been added to this board");

    public final String name;
    public final String description;

    private Notifications(String name, String description) {
        this.name = name;
        this.description = description;
    }
}