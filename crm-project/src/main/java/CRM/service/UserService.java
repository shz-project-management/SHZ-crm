package CRM.service;

import CRM.entity.User;
import CRM.repository.UserInBoardRepository;
import CRM.repository.UserRepository;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.NoSuchElementException;

@Service
public class UserService {

    private static Logger logger = LogManager.getLogger(UserService.class.getName());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInBoardRepository serInBoardRepository;

    /**
     * findByEmail search in the database for a user based on the email we have.
     *
     * @param email - user's email
     * @return entity of user that found in database.
     */
    public User get(String email) throws AccountNotFoundException {
        // Ask for the repo to find the user, by the given email address input
        // Since the repo returns an option, check if this option is not empty

        // If it is empty, throw "AccountNotFound" exception

        // Return the user back to the controller
        return null;
    }

    /**
     * Retrieves a user from the database by their ID.
     * @param userId the ID of the user to retrieve
     * @return a User object with the specified ID
     * @throws AccountNotFoundException if no user with the specified ID exists in the database
     */
    public User get(Long userId) throws AccountNotFoundException {

        try {
            // make sure such an id even exists
            // Ask for the repo to find the user, by the given id input
            return Validations.doesIdExists(userId, userRepository);

        } catch (NoSuchElementException e) {
            // If it is empty, throw "AccountNotFound" exception
            throw new AccountNotFoundException(ExceptionMessage.NO_ACCOUNT_IN_DATABASE.toString());
        }
    }

}
