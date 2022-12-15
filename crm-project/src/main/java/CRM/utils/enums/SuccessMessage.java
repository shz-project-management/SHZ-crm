package CRM.utils.enums;

/**
 * enum class with Success Messages to send more accurate messages.
 */
public enum SuccessMessage {
    DELETED("The board has been deleted"),
    FOUND("Successfully fetched this element in the database");

    private final String message;
    private SuccessMessage(final String message) {
        this.message = message;
    }
    public String toString() {
        return message;
    }
}
