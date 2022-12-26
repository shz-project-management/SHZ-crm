package CRM.utils.enums;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * enum class that creates different notifications
 */
public enum Notifications {
    ASSIGNED_TO_ME("Item assigned to me", 1L),
    STATUS_CHANGED("Item status changed", 2L),
    COMMENT_ADDED("Comment added", 3L),
    ITEM_DELETED("Item deleted", 4L),
    ITEM_DATA_CHANGED("Item data changed", 5L),
    USER_ADDED("User added to the system", 6L);

    public final String name;
    public final Long number;

    private Notifications(String name, Long number) {
        this.name = name;
        this.number = number;
    }
}