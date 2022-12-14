package CRM.controller.facades;

import CRM.entity.DTO.BoardDTO;
import CRM.entity.DTO.UserDTO;
import CRM.entity.DTO.UserPermissionDTO;
import CRM.entity.User;
import CRM.entity.UserPermission;
import CRM.entity.requests.NotificationRequest;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.response.Response;
import CRM.service.BoardService;
import CRM.service.SettingsService;
import CRM.service.UserService;
import CRM.utils.NotificationSender;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import CRM.utils.enums.Notifications;
import CRM.utils.enums.Regex;
import CRM.utils.enums.SuccessMessage;
import com.google.api.client.http.HttpStatusCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.naming.NoPermissionException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.*;

@Component
public class UserFacade {

    @Autowired
    private UserService userService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private NotificationSender notificationSender;
    @Autowired
    private SettingsService settingsService;

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id The unique ID of the user to retrieve.
     * @return A {@link Response} object containing the retrieved user, or an error message if the user
     * could not be found.
     * @throws AccountNotFoundException if the user with the given ID does not exist.
     */
    public Response<UserDTO> get(Long id) throws AccountNotFoundException {
        Validations.validate(id, Regex.ID.getRegex());
        return Response.<UserDTO>builder()
                .data(UserDTO.createUserDTO(userService.get(id)))
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
    }

    /**
     * Method to delete a user.
     *
     * @param id the id of the user to delete
     * @return a response object with a data field equal to the number of deleted users,
     * a message field indicating success or failure, and a status field indicating the status of the request
     * @throws AccountNotFoundException if the user associated with the id cannot be found
     * @throws NoSuchElementException   if the user associated with the id is not a member of a board
     * @throws IllegalArgumentException if the id is invalid
     * @throws NullPointerException     if the id parameter is null
     */
    public Response<Boolean> delete(Long id) throws AccountNotFoundException {
        Validations.validate(id, Regex.ID.getRegex());
        return Response.<Boolean>builder()
                .data(userService.delete(id))
                .message(SuccessMessage.DELETED.toString())
                .status(HttpStatus.NO_CONTENT)
                .statusCode(HttpStatusCodes.STATUS_CODE_NO_CONTENT)
                .build();
    }

    /**
     * Retrieves all users.
     *
     * @return A {@link Response} object containing a list of all users.
     */
    public Response<List<UserDTO>> getAll() {
        return Response.<List<UserDTO>>builder()
                .data(UserDTO.getListOfUsersDTO(userService.getAll()))
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
    }

    /**
     * Provides an HTTP response for a request for a list of all users in a specified board.
     *
     * @param boardId the ID of the board to retrieve users from
     * @return a {@link Response} object containing the list of users or an error message
     */
    public Response<List<UserDTO>> getAllInBoard(Long boardId) throws AccountNotFoundException {
        Validations.validate(boardId, Regex.ID.getRegex());
        return Response.<List<UserDTO>>builder()
                .data(UserDTO.getListOfUsersDTO(userService.getAllInBoard(boardId)))
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
    }

    /**
     * Retrieves all the user permissions for the board with the given ID.
     *
     * @param boardId the ID of the board to retrieve the user permissions for
     * @return a response object containing the user permissions for the board
     * @throws IllegalArgumentException if the board ID is invalid
     */
    public Response<List<UserPermissionDTO>> getAllUserPermissionsInBoard(Long boardId) {
        Validations.validate(boardId, Regex.ID.getRegex());
        return Response.<List<UserPermissionDTO>>builder()
                .data(UserPermissionDTO.getListOfUserPermissionFromDB(new ArrayList<>(userService.getAllUserPermissionsInBoard(boardId))))
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
    }

    /**
     * This method is used to retrieve all the boards created by a user with the specified id.
     *
     * @param userId The id of the user whose boards are to be retrieved.
     * @return A Response object containing all the retrieved boards or an error message if the user is not found or the id is invalid.
     * @throws IllegalArgumentException if the specified user id is invalid.
     * @throws NullPointerException     if the specified user id is null.
     * @throws NoSuchElementException   if the user with the specified id is not found.
     */
    public Response<Map<String,List<BoardDTO>>> getAllBoardsOfUser(Long userId) throws NoPermissionException, AccountNotFoundException {
        Validations.validate(userId, Regex.ID.getRegex());
        return Response.<Map<String,List<BoardDTO>>>builder()
                .data(BoardDTO.getMapWithAllBoardsForUser(userService.getAllBoardsOfUser(userId)))
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
    }

    /**
     * Method to update a user's membership in a board.
     *
     * @param objectsIdsRequest an object containing the id of the user, board, and permission level to update
     * @return a response object with a data field equal to a list of user DTOs, a message field indicating
     * success or failure, and a status field indicating the status of the request
     * @throws AccountNotFoundException if the user or board associated with the request cannot be found
     * @throws IllegalArgumentException if the objectsIdsRequest or its contained ids are invalid
     * @throws NoSuchElementException   if the board or permission level associated with the request cannot be found
     * @throws NullPointerException     if the objectsIdsRequest parameter is null
     */
    public Response<List<UserPermissionDTO>> updateUserToBoard(ObjectsIdsRequest objectsIdsRequest) throws AccountNotFoundException {
        Validations.validateUpdateUserToBoard(objectsIdsRequest.getBoardId(), objectsIdsRequest.getUserId(),
                objectsIdsRequest.getPermissionId());
        Set<UserPermission> usersPermissions = userService.updateUserToBoard(objectsIdsRequest);
        sendUserAddedNotification(objectsIdsRequest, boardService.get(objectsIdsRequest.getBoardId()).getBoardUsersSet());
        return Response.<List<UserPermissionDTO>>builder()
                .message(SuccessMessage.FOUND.toString())
                .data(UserPermissionDTO.getListOfUserPermissionFromDB(new ArrayList<>(usersPermissions)))
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
    }

    /**
     * Sends a notification to a set of users that a user has been added to a board.
     *
     * @param objectsIdsRequest an object containing the board and user IDs, and the user's email
     * @param users             a set of users to send the notification to
     * @throws AccountNotFoundException if the user specified in the request could not be found
     */
    private void sendUserAddedNotification(ObjectsIdsRequest objectsIdsRequest, Set<User> users) throws AccountNotFoundException {
        User user = null;
        if (objectsIdsRequest.getEmail() != null)
            user = userService.get(objectsIdsRequest.getEmail());
        else if (objectsIdsRequest.getUserId() != null)
            user = userService.get(objectsIdsRequest.getUserId());
        else throw new NullPointerException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
        notificationSender.sendNotificationToManyUsers(
                NotificationRequest.createUserAddedRequest(boardService.get(objectsIdsRequest.getBoardId()), user,
                        settingsService.getNotificationSettingFromDB(Notifications.USER_ADDED.name)), users);
    }
}
