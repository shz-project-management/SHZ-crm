package CRM.utils;

import CRM.entity.Comment;
import CRM.entity.Item;
import CRM.entity.User;
import CRM.utils.enums.ExceptionMessage;
import CRM.utils.enums.Regex;
import io.jsonwebtoken.Claims;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validations {
    private static Logger logger = LogManager.getLogger(Validations.class.getName());

    /**
     * validate is called from AuthController when we need to validate a given input such as email or password,
     * the validation process is according to enum regex we created.
     *
     * @param regex - the type we check on from email or password.
     * @param data  - the input to check on.
     */
    public static void validate(String data, String regex) throws IllegalArgumentException, NullPointerException {
        logger.info("in Validations -> validate");

        if (data == null) {
            logger.error("in Validations -> validate -> data == null ->" + ExceptionMessage.EMPTY_NOTNULL_FIELD);
            throw new NullPointerException(ExceptionMessage.EMPTY_NOTNULL_FIELD.toString());
        }

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()) {
            logger.error("in Validations -> validate -> !matcher.matches()->" + ExceptionMessage.VALIDATION_FAILED);
            throw new IllegalArgumentException(ExceptionMessage.VALIDATION_FAILED + data);
        }
    }

    public static void validateRegisteredUser(User user){
        validate(user.getEmail(), Regex.EMAIL.getRegex());
        validate(user.getPassword(), Regex.PASSWORD.getRegex());
        validate(user.getFirstName(), Regex.NAME.getRegex());
        validate(user.getLastName(), Regex.NAME.getRegex());
    }

    public static void validateLoginUser(User user){
        validate(user.getEmail(), Regex.EMAIL.getRegex());
        validate(user.getPassword(), Regex.PASSWORD.getRegex());
    }

    public static void validateCreatedItem(Item item){
        // validate each field of the item using validate(regex, field)
    }

    public static void validateCreatedComment(Comment comment){
        // validate each field of the comment using validate(regex, field)
    }

    /**
     * validateToken is a function that check given token if it actual valid token and return id.
     *
     * @param token - the token input.
     * @return - id of user.
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

        //FIXME: instead of validating here, use our "validate" function, giving it a token and a regex that checks its validity

        token = token.substring(7, token.length());
        Claims claims = ConfirmationToken.decodeJWT(token);
        return Long.valueOf(claims.getId());
    }
}
