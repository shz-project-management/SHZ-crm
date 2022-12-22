package CRM.utils;

import CRM.entity.*;
import CRM.entity.requests.*;
import CRM.entity.Attribute;
import CRM.entity.requests.LoginUserRequest;
import CRM.entity.requests.RegisterUserRequest;
import CRM.utils.enums.ExceptionMessage;
import CRM.utils.enums.Regex;
import CRM.utils.enums.UpdateField;
import io.jsonwebtoken.Claims;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.NonUniqueObjectException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Field;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static CRM.utils.enums.UpdateField.*;

public class Validations {
    private static Logger logger = LogManager.getLogger(Validations.class.getName());

    /**
     * Validates the provided data against the given regular expression.
     *
     * @param data  The data to validate.
     * @param regex The regular expression to use for validation.
     * @throws IllegalArgumentException If the provided data does not match the regular expression.
     * @throws NullPointerException     If the provided data is null.
     */
    public static void validate(Object data, String regex) throws IllegalArgumentException, NullPointerException {
        logger.info("in Validations -> validate");

        if (data == null) {
            logger.error("in Validations -> validate -> " + ExceptionMessage.EMPTY_NOTNULL_FIELD);
            throw new NullPointerException(ExceptionMessage.EMPTY_NOTNULL_FIELD.toString());
        }
        String convertedData = String.valueOf(data);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(convertedData);
        if (!matcher.matches()) {
            logger.error("in Validations -> validate -> Regex -> " + ExceptionMessage.VALIDATION_FAILED + convertedData);
            throw new IllegalArgumentException(ExceptionMessage.VALIDATION_FAILED + convertedData);
        }
    }

    /**
     * Validates the given user registration request.
     *
     * @param user the user registration request to validate
     * @return true if the user registration request is valid, false otherwise
     * @throws IllegalArgumentException if the email, password, first name, or last name does not match the expected format
     */
    public static boolean validateRegisteredUser(RegisterUserRequest user) {
        validate(user.getEmail(), Regex.EMAIL.getRegex());
        validate(user.getPassword(), Regex.PASSWORD.getRegex());
        validate(user.getFirstName(), Regex.NAME.getRegex());
        validate(user.getLastName(), Regex.NAME.getRegex());
        return true;
    }

    /**
     * Validates the provided LoginUserRequest object.
     *
     * @param user The LoginUserRequest object to validate.
     */
    public static void validateLoginUser(LoginUserRequest user) {
        validate(user.getEmail(), Regex.EMAIL.getRegex());
        validate(user.getPassword(), Regex.PASSWORD.getRegex());
    }

    /**
     * Validates the given board request for creating a new board.
     *
     * @param board the board request to validate
     * @return true if the board request is valid, false otherwise
     * @throws IllegalArgumentException if the board name or creator user id does not match the expected format
     */
    public static boolean validateNewBoard(BoardRequest board) {
        validate(board.getCreatorUserId().toString(), Regex.ID.getRegex());
        validate(board.getName(), Regex.BOARD_NAME.getRegex());
        return true;
    }

    /**
     * Validates an item request object by checking each field for conformity with regex patterns and other constraints.
     *
     * @param item The item request object to validate.
     * @throws NullPointerException     if the parent item ID is null.
     * @throws IllegalArgumentException if the importance value is not within the range 0-5, or if any other field fails validation.
     */
    public static void validateCreatedItem(ItemRequest item) {
        // validate each field of the item using validate(regex, field)
        try {
            validate(item.getParentItemId(), Regex.ID.getRegex());
        } catch (NullPointerException e) {
            logger.warn("Parent item ID is null");
        }

        validate(item.getUserId(), Regex.ID.getRegex());
        validate(item.getSectionId(), Regex.ID.getRegex());
        validate(item.getStatusId(), Regex.ID.getRegex());
        validate(item.getTypeId(), Regex.ID.getRegex());

        if (item.getImportance() < 0 || item.getImportance() > 5)
            throw new IllegalArgumentException(ExceptionMessage.VALIDATION_FAILED.toString());

        if (item.getTitle() == null)
            throw new NullPointerException(ExceptionMessage.VALIDATION_FAILED.toString());

        if (item.getDescription() == null)
            item.setDescription("");
    }

    /**
     * Validates a comment request object by checking each field for conformity with regex patterns and other constraints.
     *
     * @param comment The comment request object to validate.
     * @throws IllegalArgumentException if any field fails validation.
     * @throws NullPointerException     if the title is null.
     */
    public static void validateCreatedComment(CommentRequest comment) {
        // validate each field of the item using validate(regex, field)
        validate(comment.getParentItemId(), Regex.ID.getRegex());
        validate(comment.getUserId(), Regex.ID.getRegex());

        if (comment.getTitle() == null)
            throw new NullPointerException(ExceptionMessage.VALIDATION_FAILED.toString());

        if (comment.getDescription() == null)
            comment.setDescription("");
    }

    /**
     * Checks if an item with the specified ID exists in the given repository.
     *
     * @param id   the ID of the item to check for
     * @param repo the repository to search for the item in
     * @return the item with the specified ID if it exists
     * @throws NoSuchElementException if no item with the specified ID exists in the repository
     */
    public static <T> T doesIdExists(Long id, JpaRepository repo) {
        Optional<T> element = repo.findById(id);
        if (!element.isPresent()) {
            throw new NoSuchElementException(ExceptionMessage.NO_SUCH_ID.toString());
        }
        return element.get();
    }


    /**
     * Validates the provided token and returns the user id associated with it.
     *
     * @param token The token to validate.
     * @return The user id associated with the token.
     * @throws NullPointerException     If the provided token is null.
     * @throws IllegalArgumentException If the provided token is not a valid format.
     */
    public static Long validateToken(String token) {
        logger.info("in Validations -> validateToken");

        if (token == null) {
            logger.error("in Validations -> validateToken -> " + ExceptionMessage.NULL_INPUT);
            throw new NullPointerException(ExceptionMessage.NULL_INPUT.toString());
        }
        if (!token.startsWith("Bearer ")) {
            logger.error("in Validations -> validateToken -> " + ExceptionMessage.ILLEGAL_AUTH_HEADER);
            throw new IllegalArgumentException(ExceptionMessage.ILLEGAL_AUTH_HEADER.toString());
        }

        token = token.substring(7, token.length());
        Claims claims = ConfirmationToken.decodeJWT(token);
        return Long.valueOf(claims.getId());
    }

    /**
     * Set the content to a field if the field exists in an object using reflection.
     *
     * @param object    the object containing the field
     * @param fieldName the field to be updated
     * @param content   the content to be set to the field
     * @throws NoSuchFieldException if the field does not exist in the object
     */
    public static <T> void setContentToFieldIfFieldExists(T object, UpdateField fieldName, Object content) throws NoSuchFieldException {
        String fieldNameModified = fieldName.toString().replaceAll("_", "");
        try {
            System.out.println(fieldNameModified);
            if(checkIfFieldExistsInEntity(object, fieldNameModified, content)) return;
            if(checkIfFieldExistsInParentEntity(object, fieldNameModified, content)) return;

            throw new NoSuchFieldException(ExceptionMessage.FIELD_OBJECT_NOT_EXISTS.toString());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new NoSuchFieldException(ExceptionMessage.FIELD_OBJECT_NOT_EXISTS.toString());
        }
    }

    /**
     * Throws a NonUniqueObjectException with a specific message and attribute ID when an attribute already exists for a board.
     *
     * @param attribute The attribute that already exists.
     * @param className The name of the class where the attribute was found to exist.
     * @throws NonUniqueObjectException with the attribute ID and class name as parameters.
     */
    public static void throwAttributeAlreadyExistsForBoard(Attribute attribute, String className) {
        throw new NonUniqueObjectException(ExceptionMessage.ATTRIBUTE_ALREADY_IN_DB.toString(), attribute.getId(), className);
    }

    /**
     * Check if a field is a custom object field.
     *
     * @param fieldName the field to be checked
     * @return true if the field is a custom object field, false otherwise
     */
    public static boolean checkIfFieldIsCustomObject(UpdateField fieldName) {
        return fieldName.equals(STATUS) || fieldName.equals(TYPE) || fieldName.equals(PARENT_ITEM);
    }

    /**
     * Check if a field is a non-primitive field.
     *
     * @param fieldName the field to be checked
     * @return true if the field is a non-primitive field, false otherwise
     */
    public static boolean checkIfFieldIsNonPrimitive(UpdateField fieldName) {
        return fieldName.equals(DUE_DATE);
    }

    /**
     * Check if the parent item being set is not the same as the item being updated.
     *
     * @param fieldName    the field being updated
     * @param itemId       the id of the item being updated
     * @param parentItemId the id of the parent item being set
     * @throws IllegalArgumentException if the field being updated is the parent item field and the parent item id is the same as the item id
     */
    public static void checkIfParentItemIsNotTheSameItem(UpdateField fieldName, Long itemId, Long parentItemId) {
        if (fieldName.equals(PARENT_ITEM) && Objects.equals(itemId, parentItemId)) {
            throw new IllegalArgumentException(ExceptionMessage.PARENT_ITEM_ERROR.toString());
        }
    }

    /**
     * Check if a field exists in an entity object.
     *
     * @param object the entity object
     * @param fieldName the name of the field to be checked
     * @param content the content to be set to the field
     * @throws IllegalAccessException if the field is not accessible
     */
    private static <T> boolean checkIfFieldExistsInEntity(T object, String fieldName, Object content) throws IllegalAccessException {
        for (Field field : object.getClass().getDeclaredFields()) {
            if(checkIfFieldExistsInEntityHelper(field, object, fieldName, content)) return true;
        }
        return false;
    }

    /**
     * Check if a field exists in the parent entity of an object.
     *
     * @param object the object
     * @param fieldName the name of the field to be checked
     * @param content the content to be set to the field
     * @throws IllegalAccessException if the field is not accessible
     */
    private static <T> boolean checkIfFieldExistsInParentEntity(T object, String fieldName, Object content) throws IllegalAccessException {
        if (Util.fatherClasses().contains(object.getClass().getSuperclass())) {
            for (Field field : object.getClass().getSuperclass().getDeclaredFields()) {
                if(checkIfFieldExistsInEntityHelper(field, object, fieldName, content)) return true;
            }
        }
        return false;
    }

    /**
     * Helper function for checking if a field exists in an entity object.
     *
     * @param field the field to be checked
     * @param object the entity object
     * @param fieldName the name of the field to be checked
     * @param content the content to be set to the field
     * @return true if the field exists in the entity object, false otherwise
     * @throws IllegalAccessException if the field is not accessible
     */
    private static <T> boolean checkIfFieldExistsInEntityHelper(Field field, T object, String fieldName, Object content) throws IllegalAccessException {
            field.setAccessible(true);
            Object value = field.get(object);
            if (!field.getName().equalsIgnoreCase(fieldName)) {
                return false;
            }
            if (value != null && !(value.getClass().equals(content.getClass()))) {
                value.getClass().cast(content);
            }
            field.set(object, content);
            return true;
    }
}
