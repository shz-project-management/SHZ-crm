package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.NotificationRequest;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.repository.BoardRepository;
import CRM.repository.NotificationSettingRepository;
import CRM.repository.UserRepository;
import CRM.repository.UserSettingRepository;
import CRM.utils.Common;
import CRM.utils.NotificationSender;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import CRM.utils.enums.Notifications;
import CRM.utils.enums.Permission;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static Logger logger = LogManager.getLogger(UserService.class.getName());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private NotificationSettingRepository notificationSettingRepository;


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
        List<Board> boards = boardRepository.findAll();

        for (Board board : boards) {
            List<UserSetting> userSettingToDelete = board.getUsersSettings().stream()
                    .filter(userSetting -> userSetting.getUser().getId() == userId)
                    .collect(Collectors.toList());

            for (UserSetting userSetting : userSettingToDelete) {
                // Remove the UserSetting object from the set
                board.getUsersSettings().remove(userSetting);
            }

            // Save the updated board entity
            boardRepository.save(board);

            List<UserPermission> userPermissionsToDelete = board.getUsersPermissions().stream()
                    .filter(userPermission -> userPermission.getUser().getId() == userId)
                    .collect(Collectors.toList());

            for (UserPermission userPermission : userPermissionsToDelete) {
                // Remove the UserPermission object from the set
                board.getUsersPermissions().remove(userPermission);
            }
            // Save the updated board entity
            boardRepository.save(board);
        }

        List<Board> boardss = boardRepository.findByCreatorUser(user);
        for (Board board : boardss) {
            boardRepository.delete(board);
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

    //TODO documentation
    public List<User> updateUserToBoard(ObjectsIdsRequest objectsIdsRequest) throws AccountNotFoundException {
        User user;
        Board board;
        try {
            user = Validations.doesIdExists(objectsIdsRequest.getUserId(), userRepository);
            board = Validations.doesIdExists(objectsIdsRequest.getBoardId(), boardRepository);
        } catch (NoSuchElementException e) {
            throw new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
        }
            
        if(user.equals(board.getCreatorUser())){
            throw new IllegalArgumentException(ExceptionMessage.ADMIN_CANT_CHANGE_HIS_PERMISSION.toString());
        }

        Permission permissionRequest = Permission.values()[Math.toIntExact(objectsIdsRequest.getPermissionId())];
        Set<UserPermission> userPermissionsSet = updateUserPermission(user, permissionRequest, board);
        List<User> users = board.getAllUsersInBoard(board, userPermissionsSet);

        boardRepository.save(board);
        return users;
    }

    //TODO documentation
    private void createDefaultSettingForNewUserInBoard(User user, Board board) {
        List<NotificationSetting> notificationSettingList = notificationSettingRepository.findAll();
        for (NotificationSetting notificationSetting : notificationSettingList) {
            UserSetting userSetting = UserSetting.createUserSetting(user, notificationSetting);
            board.addUserSettingToBoard(userSetting);
        }
    }

    //TODO documentation
    private void createNewUserPermission(User user, Permission permission, Board board){
        UserPermission userPermission = UserPermission.newUserPermission(user, permission);
        board.addUserPermissionToBoard(userPermission);
        createDefaultSettingForNewUserInBoard(user, board);
    }

    //TODO documentation
    private Set<UserPermission> updateUserPermission(User user, Permission permissionRequest, Board board) {
        Set<UserPermission> userPermissionsSet = board.getUsersPermissions();
        UserPermission userPermissionInBoard = board.getUserPermissionById(board, user.getId() ,userPermissionsSet);

        if (permissionRequest.equals(Permission.ADMIN)) {
            throw new IllegalArgumentException(ExceptionMessage.PERMISSION_NOT_ALLOWED.toString());
        }

        if (permissionRequest.equals(Permission.UNAUTHORIZED)) {
            userPermissionsSet.removeIf(userPerm -> userPermissionInBoard.getId().equals(userPerm.getId()));
            board.removeSettingsByUserPermission(userPermissionInBoard);
        }
        else if (userPermissionInBoard == null) {
            createNewUserPermission(user, permissionRequest, board);
        } else{
            userPermissionInBoard.setPermission(permissionRequest);
        }
        return userPermissionsSet;
    }
}
