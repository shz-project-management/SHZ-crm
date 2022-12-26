package CRM.controller.facades;

import CRM.entity.DTO.UserDTO;
import CRM.entity.User;
import CRM.entity.UserPermission;
import CRM.entity.requests.NotificationRequest;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.response.Response;
import CRM.service.UserService;
import CRM.utils.Common;
import CRM.utils.NotificationSender;
import CRM.utils.Validations;
import CRM.utils.enums.Notifications;
import CRM.utils.enums.Permission;
import CRM.utils.enums.Regex;
import CRM.utils.enums.SuccessMessage;
import com.google.api.client.http.HttpStatusCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class UserFacade {

    @Autowired
    private UserService userService;

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
     * Deletes a user from the database.
     *
     * @param id the id of the user to delete
     * @return a Response object with a message, status, and status code indicating the result of the delete operation
     * if the delete operation was successful, the message field will contain the SuccessMessage.FOUND message,
     * the status field will be HttpStatus.OK, and the statusCode field will be 200
     * if the user with the given id does not exist in the database, the message field will contain the
     * AccountNotFoundException message, the status field will be HttpStatus.BAD_REQUEST, and the statusCode field
     * will be 400
     * if the id input is invalid, the message field will contain the IllegalArgumentException message,
     * the status field will be HttpStatus.BAD_REQUEST, and the statusCode field will be 400
     * if there is a null pointer exception, the message field will contain the NullPointerException message,
     * the status field will be HttpStatus.BAD_REQUEST, and the statusCode field will be 500
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

    //TODO documentation
    public Response updateUserToBoard(ObjectsIdsRequest objectsIdsRequest) {
        try {
            Validations.validateUpdateUserToBoard(objectsIdsRequest.getBoardId(), objectsIdsRequest.getUserId(),
                    objectsIdsRequest.getPermissionId());
            List<User> users = userService.updateUserToBoard(objectsIdsRequest);

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
}
