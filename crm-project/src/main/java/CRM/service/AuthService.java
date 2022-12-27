package CRM.service;

import CRM.entity.User;
import CRM.entity.requests.LoginUserRequest;
import CRM.entity.requests.RegisterUserRequest;
import CRM.repository.UserRepository;
import CRM.utils.ConfirmationToken;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {
    private static Logger logger = LogManager.getLogger(AuthService.class.getName());

    @Autowired
    private UserRepository userRepository;

    /**
     * Registers a new user with the provided RegisterUserRequest object.
     *
     * @param user The RegisterUserRequest object containing the user's information.
     * @return The registered User object.
     * @throws IllegalArgumentException If the provided email is already in use.
     */
    public User register(RegisterUserRequest user) {
        logger.info("in AuthService -> register");

        // make sure the email doesn't already exist in the database. If so, throw an IllegalArgument exception.
        User storedUser = findByEmail(user.getEmail());
        if (storedUser != null)
            throw new IllegalArgumentException(ExceptionMessage.EMAIL_IN_USE.toString());

        // save and the user in the database.
        return userRepository.save(User.newUser(user));
    }

    /**
     * Attempts to log in a user using a third party service.
     * If the user does not exist in the database, it will be created.
     *
     * @param user the user to log in
     * @return a JWT token for the logged in user
     */
    public String thirdPartyLogin(RegisterUserRequest user) {
        logger.info("in AuthService -> thirdPartyLogin");

        User storedUser = findByEmail(user.getEmail());
        if (storedUser == null)
            storedUser = userRepository.save(User.newUser(user));

        return generateToken(storedUser.getId());
    }

    /**
     * Attempts to login the user with the provided email and password.
     *
     * @param user The LoginUserRequest containing the user's email and password.
     * @return A token to be used for subsequent requests.
     * @throws AccountNotFoundException If the provided email does not exist in the database.
     * @throws AuthenticationException  If the provided password does not match the stored password.
     */
    public String login(LoginUserRequest user) throws AuthenticationException, AccountNotFoundException {
        // make sure the email exists in the database. If not, throw an AccountNotFoundException exception.
        User storedUser = findByEmail(user.getEmail());
        if (storedUser == null)
            throw new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
        // make sure the given password and the stored password are equal. CONSULT: maybe this is our time to use password hashing+salting?
        // if the passwords are not equal, return Unauthorized exception.
        if (!storedUser.getPassword().equals(user.getPassword())) {
            throw new AuthenticationException(ExceptionMessage.PASSWORD_DOESNT_MATCH.toString());
        }

        // return a token back to facadeAuth -> user generateToken(userId) function.
        return generateToken(storedUser.getId());
    }

    /**
     * activate function meant to change the user column isActivated from originated value false to true.
     * Repository go to the unique row that has user email and changed that value.
     * Method used after a user clicks on the link he got on email.
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
     *
     * @param id - the ID of the login user
     * @return generated token according to: io.jsonwebtoken.Jwts library
     */
    private String generateToken(Long id) {
        return ConfirmationToken.createJWT(String.valueOf(id), "SHZ project management", "login", 0);
    }

    /**
     * called by functions to check if the token is a valid user token
     * and checks if we have the user id we got from the Validations.validateToken(token) in the database.
     *
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

    /**
     * Find a user by their ID.
     *
     * @param creatorUserId The ID of the user to find.
     * @return The user with the provided ID.
     * @throws AccountNotFoundException If no user with the provided ID is found.
     */
    public User findById(long creatorUserId) throws AccountNotFoundException {
        try {
            return Validations.doesIdExists(creatorUserId, userRepository);
        } catch (NoSuchElementException e) {
            throw new AccountNotFoundException(ExceptionMessage.NO_USER_IN_DATABASE.toString());
        }
    }


    /**
     * Find a user by their email.
     *
     * @param email The email of the user to find.
     * @return The user with the provided email, or null if no such user is found.
     */
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent())
            return null;

        return user.get();
    }
}
