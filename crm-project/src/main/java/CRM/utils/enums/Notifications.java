package CRM.utils.enums;

/**
 * enum class that creates different notifications
 */
public enum Notifications {
    ASSIGNED_TO_ME("Items assigned to me", 0L),
    STATUS_CHANGED("Items status changed", 1L),
    COMMENT_ADDED("Items comment added", 2L),
    ITEM_DELETED("Items deleted", 3L),
    ITEM_DATA_CHANGED("Items data changed", 4L),
    USER_ADDED("User joined to the system", 5L);

    private final String name;
    private final Long id;

    Notifications(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "Notifications{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}