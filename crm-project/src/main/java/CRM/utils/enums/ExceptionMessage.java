package CRM.utils.enums;

/**
 * enum class with Exception Messages to send more accurate errors.
 */
public enum ExceptionMessage {

    // TODO: split by subjects!
    // TODO: change messages to each exception

    ADMIN_CANT_CHANGE_HIS_PERMISSION("Admin cannot change his own permission in the board he has created"),
    MULTIPLE_AUTO_INCREMENT("A table can not contain multiple auto increment fields."),
    TRUNCATE("Couldn't truncate the table properly."),
    ILLEGAL_SQL_QUERY("Sql query is not legal."),
    PERMISSION_NOT_ALLOWED("Cant assign another admin to this board"),
    ATTRIBUTE_ALREADY_IN_DB("Attribute with the same name already exists in this board"),
    NULL_INPUT("cannot set the content of the argument to null"),
    TOKEN_IS_NULL("token is null"),
    EMPTY_NOTNULL_FIELD("Not null fields must be filled out before creation"),
    FIELD_OBJECT_NOT_EXISTS("Field object doesn't exists"),
    FIELD_OBJECT_REPO_NOT_EXISTS("Field object repository doesn't exists or not supported"),
    NO_SUCH_CLASS("There is no such class in the system!"),
    TOO_SHORT_STRING("This field can not be as short as inserted"),
    PERMISSION_NOT_FOUND("Permission not found"),
    PARENT_ITEM_NOT_FOUND("Parent item not found for this comment"),
    PARENT_ITEM_ERROR("Item can't be his own parent"),
    ILLEGAL_AUTH_HEADER("Authorization header is not legal"),
    PASSWORD_DOESNT_MATCH("The given password doesn't match our database's"),

    USER_IN_BOARD_EXISTS("This user is already assigned to this board"),
    BOARD_DOES_NOT_EXISTS("This board ID does not exist in the database: "),
    NOT_MATCH("Error: email or password does not match"),
    NO_USER_IN_DATABASE("Could not locate this user in the database."),
    NO_ACCOUNT_IN_DATABASE("Could not locate this email in the database."),
    NO_DOCUMENT_IN_DATABASE("Could not locate this document in the database."),

    NO_FOLDER_IN_DATABASE("Could not locate this folder in the database."),

    NO_USER_IN_DOCUMENT_IN_DATABASE("Could not locate the find the user that uses this document in the database."),
    UNAUTHORIZED("You are unauthorized to create such a action"),
    FOLDER_EXISTS("This folder ID already exist in the database: "),
    USER_ALREADY_HAS_THIS_PERMISSION("User already has this permission in board"),
    ACCOUNT_DOES_NOT_EXISTS("Could not locate this account in the database"),
    NO_SUCH_ID("Could not locate this element by the given ID"),
    VALIDATION_FAILED("Could not approve the given information: "),
    UNAUTHORIZED_USER("You don't have the permission to do that action: "),
    WRONG_SEARCH("Something in the request wasn't properly written, try again"),
    EMAIL_IN_USE("This email address is already in use"),
    UNPROCESSABLE_ENTITY("Server Couldn't process entity"),
    CANT_ASSIGN_PERMISSION("Cant assign this permission to the given user"),
    USER_NOT_ACTIVATED("The user is not activated"),
    WRONG_INPUT_TYPE("Could not insert such input to the field"),
    USER_IS_NOT_THE_ADMIN("The user is not the admin of the document");

    private final String message;

    private ExceptionMessage(final String message) {
        this.message = message;
    }

    public String toString() {
        return message;
    }
}