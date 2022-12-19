package CRM.service;

import CRM.entity.Board;
import CRM.entity.User;
import CRM.entity.UserInBoard;
import CRM.repository.BoardRepository;
import CRM.repository.UserInBoardRepository;
import CRM.repository.UserRepository;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static Logger logger = LogManager.getLogger(UserService.class.getName());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInBoardRepository userInBoardRepository;
    @Autowired
    private BoardRepository boardRepository;

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
        // make sure such an id even exists
        // Ask for the repo to find the user, by the given id input
        try {
            return Validations.doesIdExists(userId, userRepository);
        }catch (NoSuchElementException e){
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
        }catch (NoSuchElementException e){
            throw new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
        }
        // remove all user dependencies from the db
        removeAllUserDependencies(user);
        // lastly, remove the user from the database
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
        Board board = Validations.doesIdExists(boardId, boardRepository);
        List<UserInBoard> usersInBoard = userInBoardRepository.findAllUserByBoard(board);
        return usersInBoard.stream().map(UserInBoard::getUser).collect(Collectors.toList());
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
    public UserInBoard addUserToBoard(long userId, long boardId) throws AccountNotFoundException {
        User user;
        try {
            user = Validations.doesIdExists(userId, userRepository);
        } catch (NoSuchElementException e) {
            throw new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
        }

        Board board = Validations.doesIdExists(boardId, boardRepository);

        // make sure this combination of user and board doesn't not exist in the db yet
        if (userInBoardRepository.findByBoardAndUser(user, board).isPresent())
            throw new IllegalArgumentException(ExceptionMessage.USER_IN_BOARD_EXISTS.toString());

        // if not, store the new one in the db
        UserInBoard userInBoard = UserInBoard.userInBoardUser(user, board);
        return userInBoardRepository.save(userInBoard);
    }

    /**
     * Removes all dependencies related to the given user from the database.
     *
     * @param user the user whose dependencies are to be removed
     *             This method performs the following actions:
     *             Removes all entries of the given user from the UserInBoard table
     *             Removes all comments made by the given user from the database
     *             Removes all attributes of the given user from the database
     *             Removes all boards created by the given user from the database
     */
    boolean removeAllUserDependencies(User user) {
        // second, remove all entries of this user from UserInBoard table
        removeUserDependenciesFromUserInBoardTable(user);

        // remove all user's comments from the db

        // remove all user's attributes from the db

        // third, remove every board created by this user
        removeUserDependenciesFromBoardTable(user);
        return true;
    }

    /**
     * Removes all boards from the database that were created by the given user.
     *
     * @param user the user whose boards are to be removed from the database
     */
    boolean removeUserDependenciesFromBoardTable(User user) {
        List<Board> boardList = boardRepository.findAllByUser(user);
        boardList.forEach(board -> boardRepository.delete(board));
        return true;
    }

    /**
     * Removes all entries in the UserInBoard table that are related to the given user or the boards they have created.
     *
     * @param user the user whose related entries in the UserInBoard table are to be removed
     */
    private boolean removeUserDependenciesFromUserInBoardTable(User user) {
        List<Board> boardList = boardRepository.findAllByUser(user);
        boardList.forEach(board -> userInBoardRepository.deleteAllByUserOrBoard(board.getCreatorUser(), board));
        return true;
    }
}
