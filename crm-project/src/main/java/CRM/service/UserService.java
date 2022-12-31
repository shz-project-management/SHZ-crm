package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.repository.BoardRepository;
import CRM.repository.NotificationRepository;
import CRM.repository.NotificationSettingRepository;
import CRM.repository.UserRepository;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import CRM.utils.enums.Permission;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static CRM.utils.Util.SharedBoards;
import static CRM.utils.Util.myBoards;

@Service
public class UserService {

    private static Logger logger = LogManager.getLogger(UserService.class.getName());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private NotificationSettingRepository notificationSettingRepository;
    @Autowired
    private NotificationRepository notificationRepository;


    /**
     * findByEmail search in the database for a user based on the email we have.
     *
     * @param email - user's email
     * @return entity of user that found in database.
     */
    public User get(String email) throws AccountNotFoundException {
        return userRepository.findByEmail(email).get();
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
        removeDependenciesFromBoard(user);
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
     * Retrieves all boards belonging to the specified user.
     *
     * @param userId the id of the user
     * @return a map containing two lists of boards: "myBoards" (boards created by the user) and "SharedBoards" (boards shared with the user)
     * @throws AccountNotFoundException if the user does not exist
     */
    public Map<String, List<Board>> getAllBoardsOfUser(long userId) throws AccountNotFoundException, NoPermissionException {
        User user;
        try {
            user = Validations.doesIdExists(userId, userRepository);
        } catch (NoSuchElementException e) {
            throw new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
        }

        //get all the boards of the creator user
        Map<String, List<Board>> userBoards = new HashMap<>();
        userBoards.put(myBoards, boardRepository.findByCreatorUser_Id(user.getId()));
        userBoards.put(SharedBoards, getSharedBoardsOfUser(user));
        return userBoards;
    }

    /**
     * This method updates a user's permission on a board.
     *
     * @param request an {@link ObjectsIdsRequest} object which contains the ID of the user, board, and requested permission
     * @return a list of {@link User} objects representing all users in the board
     * @throws AccountNotFoundException if the user or board does not exist
     * @throws IllegalArgumentException if the requested permission is not allowed or the board's creator is trying to change their own permission
     */
    public Set<UserPermission> updateUserToBoard(ObjectsIdsRequest request) throws AccountNotFoundException {
        User user = getUserFromRequest(request);
        Board board = getBoardFromRequest(request);

        if (user.equals(board.getCreatorUser())) {
            throw new IllegalArgumentException(ExceptionMessage.ADMIN_CANT_CHANGE_HIS_PERMISSION.toString());
        }

        Permission permission = Permission.values()[Math.toIntExact(request.getPermissionId())];
        Set<UserPermission> userPermissionsSet = updateUserPermission(user, permission, board);

        boardRepository.save(board);
        return userPermissionsSet;
    }

    /**
     * Returns the user from the request. If the request contains an email, the method
     * attempts to find a user with that email. If the request contains a user ID, the method
     * uses the ID to find the user. If no user is found, an AccountNotFoundException is thrown.
     *
     * @param request the request containing the email or user ID
     * @return the user found in the request
     * @throws AccountNotFoundException if no user is found in the request
     */
    private User getUserFromRequest(ObjectsIdsRequest request) throws AccountNotFoundException {
        if (request.getEmail() != null) {
            Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
            if (optionalUser.isPresent()) {
                return optionalUser.get();
            }
        } else {
            return Validations.doesIdExists(request.getUserId(), userRepository);
        }
        throw new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
    }

    /**
     * Returns the board from the request. The method uses the board ID in the request
     * to find the board. If no board is found, an AccountNotFoundException is thrown.
     *
     * @param request the request containing the board ID
     * @return the board found in the request
     * @throws AccountNotFoundException if no board is found in the request
     */
    private Board getBoardFromRequest(ObjectsIdsRequest request) {
        try {
            return Validations.doesIdExists(request.getBoardId(), boardRepository);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
        }
    }

    /**
     * Retrieves the set of user permissions for a board.
     *
     * @param boardId The ID of the board for which to retrieve user permissions.
     * @return A set of user permissions for the specified board.
     * @throws IllegalArgumentException If the provided board ID is invalid or the board does not exist.
     */
    public Set<UserPermission> getAllUserPermissionsInBoard(Long boardId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        return board.getUsersPermissions();
    }

    /**
     * Create default notification settings for a new user in a board.
     *
     * @param user  the user to create default settings for
     * @param board the board to add the default settings to
     */
    private void createDefaultSettingForNewUserInBoard(User user, Board board) {
        List<NotificationSetting> notificationSettingList = notificationSettingRepository.findAll();
        for (NotificationSetting notificationSetting : notificationSettingList) {
            UserSetting userSetting = UserSetting.createUserSetting(user, notificationSetting);
            board.addUserSettingToBoard(userSetting);
        }
    }

    /**
     * Create a new user permission in a board.
     *
     * @param user       the user to create the permission for
     * @param permission the permission to assign to the user
     * @param board      the board to add the user permission to
     */
    private void createNewUserPermission(User user, Permission permission, Board board) {
        UserPermission userPermission = UserPermission.newUserPermission(user, permission);
        board.addUserPermissionToBoard(userPermission);
        createDefaultSettingForNewUserInBoard(user, board);
    }

    /**
     * Update the user's permission in a board.
     *
     * @param user              the user to update the permission for
     * @param permissionRequest the requested permission
     * @param board             the board to update the user's permission in
     * @return the set of user permissions in the board after the update
     * @throws IllegalArgumentException if the requested permission is not allowed
     */
    private Set<UserPermission> updateUserPermission(User user, Permission permissionRequest, Board board) {
        Set<UserPermission> userPermissionsSet = board.getUsersPermissions();
        UserPermission userPermissionInBoard = board.getUserPermissionById(user.getId(), userPermissionsSet);

        if (permissionRequest.equals(Permission.ADMIN)) {
            throw new IllegalArgumentException(ExceptionMessage.PERMISSION_NOT_ALLOWED.toString());
        }

        if (permissionRequest.equals(Permission.UNAUTHORIZED)) {
            userPermissionsSet.removeIf(userPerm -> userPermissionInBoard.getId().equals(userPerm.getId()));
            board.removeSettingsByUserPermission(userPermissionInBoard);
        } else if (userPermissionInBoard == null) {
            createNewUserPermission(user, permissionRequest, board);
        } else {
            userPermissionInBoard.setPermission(permissionRequest);
        }
        return userPermissionsSet;
    }

    /**
     * Private method to retrieve all boards shared with the specified user.
     *
     * @param user the user
     * @return a list of boards shared with the user
     */
    private List<Board> getSharedBoardsOfUser(User user) throws NoPermissionException {
        //get all the boards of the user he is shared with
        List<Board> allBoards = boardRepository.findAll();
        List<Board> sharedBoards = new ArrayList<>();
        for (Board board : allBoards) {
            if (board.getAllUsersInBoard().contains(user)) {
                Permission userPerm = board.getUserPermissionWithAdminByUserId(user.getId());
                if (!userPerm.equals(Permission.ADMIN))
                    sharedBoards.add(board);
            }
        }
        return sharedBoards;
    }

    boolean removeDependenciesFromBoard(User user) {

        List<Board> boardWhereUserAssigned = boardRepository.findBySections_Items_AssignedToUser(user);
        for (Board board : boardWhereUserAssigned) {
            board.removeAssignedUserFromItems(user.getId());
            boardRepository.save(board);
        }

        notificationRepository.deleteByUser(user);


        List<Board> boards = boardRepository.findAll();

        for (Board board : boards) {
            List<UserSetting> userSettingToDelete = board.getUsersSettings().stream()
                    .filter(userSetting -> Objects.equals(userSetting.getUser().getId(), user.getId()))
                    .collect(Collectors.toList());

            for (UserSetting userSetting : userSettingToDelete) {
                // Remove the UserSetting object from the set
                board.getUsersSettings().remove(userSetting);
            }

            // Save the updated board entity
            boardRepository.save(board);

            List<UserPermission> userPermissionsToDelete = board.getUsersPermissions().stream()
                    .filter(userPermission -> Objects.equals(userPermission.getUser().getId(), user.getId()))
                    .collect(Collectors.toList());

            for (UserPermission userPermission : userPermissionsToDelete) {
                // Remove the UserPermission object from the set
                board.getUsersPermissions().remove(userPermission);
            }
            // Save the updated board entity
            boardRepository.save(board);
        }

        List<Board> boardsOfCreator = boardRepository.findByCreatorUser_Id(user.getId());
        for (Board board : boardsOfCreator) {
            board.removeAttributesFromBoard();
            boardRepository.save(board);
            board.removeAssignedUsersFromBoard();
            boardRepository.save(board);
            boardRepository.delete(board);
        }
        return true;
    }
}
