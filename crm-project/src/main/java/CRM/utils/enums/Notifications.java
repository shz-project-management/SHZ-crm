package CRM.utils.enums;

import lombok.Getter;

/**
 * enum class that creates different notifications
 */
@Getter
public enum Notifications {
    ASSIGNED_TO_ME("Item has been assigned to me"),
    STATUS_CHANGED("Item that is related to me had it's status changed"),
    TYPE_CHANGED("Item that is related to me had it's type changed"),
    COMMENT_ADDED("Item that is related to me has a new comment"),
    ITEM_DELETED("Item that is related to me has been deleted"),
    ITEM_DATA_CHANGED("Item that is related to me had some data change"),
    USER_ADDED("A new user has been added to a board I am taking part in");

    public final String name;

    private Notifications(String name) {
        this.name = name;
    }
}
