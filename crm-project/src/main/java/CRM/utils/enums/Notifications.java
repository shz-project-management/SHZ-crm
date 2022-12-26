package CRM.utils.enums;

import lombok.Getter;

/**
 * enum class that creates different notifications
 */
@Getter
public enum Notifications {
    ASSIGNED_TO_ME("Item assigned to me"),
    STATUS_CHANGED("Item status changed"),
    COMMENT_ADDED("Comment added"),
    ITEM_DELETED("Item deleted"),
    ITEM_DATA_CHANGED("Item data changed"),
    USER_ADDED("User added to the system");

    public final String name;

    private Notifications(String name) {
        this.name = name;
    }
}