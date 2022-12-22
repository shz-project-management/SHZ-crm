package CRM.service;

import CRM.entity.*;
import CRM.repository.BoardRepository;
import CRM.repository.SettingRepository;
import CRM.repository.UserRepository;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import CRM.utils.enums.Permission;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    private static Logger logger = LogManager.getLogger(UserService.class.getName());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private SettingRepository settingRepository;

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
     *
     * @param userId the ID of the user to retrieve
     * @return a User object with the specified ID
     * @throws AccountNotFoundException if no user with the specified ID exists in the database
     */
    public User get(long userId) throws AccountNotFoundException {
        try {
            // make sure such an id even exists
            // Ask for the repo to find the user, by the given id input
            return Validations.doesIdExists(userId, userRepository);

        } catch (NoSuchElementException e) {
            throw new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
        }
    }

    /**
     * Deletes a user from the database.
     *
     * @param userId the id of the user to delete
     * @return true if the user was successfully deleted, false otherwise
     * @throws AccountNotFoundException if the user with the given id does not exist in the database
     */
    public Boolean delete(long userId) throws AccountNotFoundException {
        // Make sure such an id even exists. Ask for the repo to find the user, by the given id input
        User user;
        try {
            user = Validations.doesIdExists(userId, userRepository);

        } catch (NoSuchElementException e) {
            throw new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
        }

        userRepository.delete(user);
        return true;
    }

    /**
     * Returns a list of all the users that exist in the database.
     *
     * @return a list of all users in the repository
     */
    public List<User> getAll() {
        return userRepository.findAll();
    }

    /**
     * This method is used to retrieve all the users in a board with the specified id.
     *
     * @param boardId The id of the board whose users are to be retrieved.
     * @return A list containing all the users in the board with the specified id.
     * @throws NoSuchElementException   if the board with the specified id is not found.
     * @throws IllegalArgumentException if the specified board id is invalid.
     * @throws NullPointerException     if the specified board id is null.
     */
    public List<User> getAllInBoard(long boardId) throws AccountNotFoundException {
        // make sure this id even exists
        Board board = Validations.doesIdExists(boardId, boardRepository);

        // get all users from this board: use board.getAllUsersInBoard()
        return board.getAllUsersInBoard();
    }

    /**
     * Adds a user to a board.
     *
     * @param userId  the id of the user to add to the board
     * @param boardId the id of the board to add the user to
     * @return the UserInBoard object representing the user being added to the board
     * @throws AccountNotFoundException if the user or board with the given id does not exist in the database
     * @throws IllegalArgumentException if the combination of the given user and board already exists in the database
     */
    public void addUserToBoard(long userId, long boardId) throws AccountNotFoundException {
        User user;
        Board board;
        try {
            user = Validations.doesIdExists(userId, userRepository);
            board = Validations.doesIdExists(boardId, boardRepository);

        } catch (NoSuchElementException e) {
            throw new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
        }

        UserPermission userPermission = new UserPermission(0L, user, Permission.USER);

        board.addUserPermissionToBoard(userPermission);

        createDefaultSettingForNewUserInBoard(user, board);
        boardRepository.save(board);
    }

    /**
     * Creates default notifications for every new user in every board,
     * using constant notifications
     */
    private void createDefaultSettingForNewUserInBoard(User user, Board board){
        for (NotificationSetting notificationSetting : settingRepository.findAll()) {
            UserSetting userSetting = new UserSetting(0L, user, notificationSetting, true, true);
            board.addUserSettingToBoard(userSetting);
        }
    }
}
