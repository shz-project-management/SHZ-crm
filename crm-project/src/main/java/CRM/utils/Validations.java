package CRM.utils;

import CRM.entity.Comment;
import CRM.entity.Item;
import CRM.entity.requests.BoardRequest;
import CRM.entity.requests.LoginUserRequest;
import CRM.entity.requests.RegisterUserRequest;
import CRM.repository.UserRepository;
import CRM.utils.enums.ExceptionMessage;
import CRM.utils.enums.Regex;
import io.jsonwebtoken.Claims;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.security.auth.login.AccountNotFoundException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static void validateCreatedItem(Item item) {
        // validate each field of the item using validate(regex, field)
    }

    public static void validateCreatedComment(Comment comment) {
        // validate each field of the comment using validate(regex, field)
    }

    /**
     * Checks if an item with the specified ID exists in the given repository.
     *
     * @param id   the ID of the item to check for
     * @param repo the repository to search for the item in
     * @return the item with the specified ID if it exists
     * @throws NoSuchElementException if no item with the specified ID exists in the repository
     */
    public static <T> T doesIdExists(Long id, JpaRepository repo) throws AccountNotFoundException {
        Optional<T> element = repo.findById(id);
        if (!element.isPresent()) {
            if (repo.getClass().getSimpleName().equals(UserRepository.class.getSimpleName())) {
                throw new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
            }
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
}
