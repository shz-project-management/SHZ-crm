package CRM.utils.enums;

/**
 * enum class with Success Messages to send more accurate messages.
 */
public enum SuccessMessage {
    LOGIN("Successfully logged in to the system!"),
    REGISTER("The user has been successfully registered to the database"),
    CREATE("The instance has been successfully created"),
    DELETED("The board has been deleted"),
    FOUND("Successfully fetched this element in the database");

    private final String message;

    SuccessMessage(final String message) {
        this.message = message;
    }

    public String toString() {
        return message;
    }
}
