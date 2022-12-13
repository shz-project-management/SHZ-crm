package CRM.service;

import CRM.entity.User;
import CRM.repository.UserRepository;
import CRM.utils.ConfirmationToken;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;

@Service
@AllArgsConstructor
public class AuthService {
    private static Logger logger = LogManager.getLogger(AuthService.class.getName());

    @Autowired
    private UserRepository userRepository;

    /**
     * register   method is used to register new users to the app with given inputs
     * @param user    - user entity with the user's information
     * @return - entity of the user we have just registered.
     */
    public User register(User user) {
        logger.info("in AuthService -> register");

        // make sure the email doesn't already exist in the database. If so, throw an IllegalArgument exception.
        // save the user in the database.

        // return the user back to the facadeAuth

        return null;
    }

    /**
     * login function method is used to log-in users to the app and check if inputs was correct according to database.
     * first check if we have the email in the database and then proceed to generate token.
     *
     * @param email    - mail of user
     * @param password - password
     * @return - token for user to be unique on app
     */
    public String login(String email, String password) throws AccountNotFoundException {

        // make sure the email exists in the database. If not, throw an AccountNotFoundException exception.

        // make sure the given password and the stored password are equal. TODO: maybe this is our time to use password hashing+salting?
        // if the passwords are not equal, return IllegalArgument exception.

        // return a token back to facadeAuth -> user generateToken(userId) function.

        return null;
    }

    /**
     * activate function meant to change the user column isActivated from originated value false to true.
     * Repository go to the unique row that has user email and changed that value.
     * Method used after a user clicks on the link he got on email.
     *
     * @param id - user email
     * @return - should be always 1, which is rows affected in the database.
     */
//    public int activate(Long id) {
//        logger.info("in AuthService -> activate");
//        return userRepository.updateIsActivated(true, id);
//        return 0;
//    }

    /**
     * generateToken is a function that creates a unique JWT token for every logged-in user.
     * @param user - user
     * @return generated token according to: io.jsonwebtoken.Jwts library
     */
    private String generateToken(User user) {
        return ConfirmationToken.createJWT(String.valueOf(user.getId()), "SHZ project management", "login", 0);
    }

    /**
     * called by functions to check if the token is a valid user token
     * and checks if we have the user id we got from the Validations.validateToken(token) in the database.
     * @return - id of user
     */
    public Long checkTokenToUserInDB(String token) throws AccountNotFoundException {
        logger.info("in AuthService -> isValid");
        long id = Validations.validateToken(token);
        if (!userRepository.existsById(id)) {
            logger.error("in AuthService -> isValid ->" + ExceptionMessage.NO_USER_IN_DATABASE);
            throw new AccountNotFoundException(ExceptionMessage.NO_USER_IN_DATABASE.toString());
        }
        return id;
    }
}
