package CRM.controller.facades;

import CRM.entity.DTO.BoardDTO;
import CRM.entity.DTO.UserDTO;
import CRM.entity.User;
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

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

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
    public Response get(Long id) {
        try {
            Validations.validate(id, Regex.ID.getRegex());

            return Response.builder()
                    .data(UserDTO.createUserDTO(userService.get(id)))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                    .build();

        } catch (AccountNotFoundException | IllegalArgumentException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                    .build();
        }
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
    public Response delete(Long id) {
        try {
            Validations.validate(id, Regex.ID.getRegex());

            return Response.builder()
                    .data(userService.delete(id))
                    .message(SuccessMessage.DELETED.toString())
                    .status(HttpStatus.NO_CONTENT)
                    .statusCode(HttpStatusCodes.STATUS_CODE_NO_CONTENT)
                    .build();

        } catch (AccountNotFoundException | NoSuchElementException | IllegalArgumentException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * Retrieves all users.
     *
     * @return A {@link Response} object containing a list of all users.
     */
    public Response getAll() {
        return Response.builder()
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
    public Response getAllInBoard(Long boardId) {
        try {
            Validations.validate(boardId, Regex.ID.getRegex());

            return Response.builder()
                    .data(UserDTO.getListOfUsersDTO(userService.getAllInBoard(boardId)))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                    .build();

        } catch (AccountNotFoundException | IllegalArgumentException | NoSuchElementException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                    .build();
        }
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
    public Response getAllBoardsOfUser(Long userId) {
        try {
            Validations.validate(userId, Regex.ID.getRegex());

            return Response.builder()
                    .data(BoardDTO.getMapWithAllBoardsForUser(userService.getAllBoardsOfUser(userId)))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                    .build();

        } catch (AccountNotFoundException | IllegalArgumentException | NoSuchElementException e) {
            return Response.builder()
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .status(HttpStatus.BAD_REQUEST)
                    .message(ExceptionMessage.NULL_INPUT.toString())
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(e.getMessage())
                    .build();
        }
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
    public Response updateUserToBoard(ObjectsIdsRequest objectsIdsRequest) {
        try {
            Validations.validateUpdateUserToBoard(objectsIdsRequest.getBoardId(), objectsIdsRequest.getUserId(),
                    objectsIdsRequest.getPermissionId());
            List<User> users = userService.updateUserToBoard(objectsIdsRequest);
            sendUserAddedNotification(objectsIdsRequest, boardService.get(objectsIdsRequest.getBoardId()).getBoardUsersSet());
            return Response.builder()
                    .message(SuccessMessage.FOUND.toString())
                    .data(UserDTO.getListOfUsersDTO(users))
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                    .build();

        } catch (AccountNotFoundException | IllegalArgumentException | NoSuchElementException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                    .build();
        }
    }

    private void sendUserAddedNotification(ObjectsIdsRequest objectsIdsRequest, Set<User> users) throws AccountNotFoundException {
        notificationSender.sendNotificationToManyUsers(
                NotificationRequest.createUserAddedRequest(boardService.get(objectsIdsRequest.getBoardId()),
                        userService.get(objectsIdsRequest.getUserId()),
                        settingsService.getNotificationSettingFromDB(Notifications.USER_ADDED.name)), users);
    }
}
