package CRM.utils.enums;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * enum class that creates different notifications
 */
public enum Notifications {
    ASSIGNED_TO_ME("Items assigned to me", "One or multiple items were assigned to me", 1L),
    STATUS_CHANGED("Items status changed", "Item that is related to me has it status changed", 2L),
    COMMENT_ADDED("Items comment added", "Item that is related to me has a new comment", 3L),
    ITEM_DELETED("Items deleted", "Item that is related to me has been deleted", 4L),
    ITEM_DATA_CHANGED("Items data changed", "Item that is related to me has had a change in some data", 5L),
    USER_ADDED("User added to the system", "You have been added to a new board", 6L);

    public final String name;
    public final String description;
    public final Long number;

    private Notifications(String name, String description, Long number) {
        this.name = name;
        this.description = description;
        this.number = number;
    }
}