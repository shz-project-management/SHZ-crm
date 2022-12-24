package CRM.controller.facades;

import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.response.Response;
import CRM.service.UserService;
import CRM.utils.Validations;
import CRM.utils.enums.Permission;
import CRM.utils.enums.Regex;
import CRM.utils.enums.SuccessMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
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

            return new Response.Builder()
                    .data(userService.get(id))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .build();
        } catch (AccountNotFoundException | IllegalArgumentException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(400)
                    .build();
        } catch (NullPointerException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(500)
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
            return new Response.Builder()
                    .data(userService.delete(id))
                    .message(SuccessMessage.DELETED.toString())
                    .status(HttpStatus.NO_CONTENT)
                    .statusCode(204)
                    .build();
        } catch (AccountNotFoundException | NoSuchElementException | IllegalArgumentException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(400)
                    .build();
        } catch (NullPointerException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(500)
                    .build();
        }
    }

    /**
     * Retrieves all users.
     *
     * @return A {@link Response} object containing a list of all users.
     */
    public Response getAll() {
        return new Response.Builder()
                .data(userService.getAll())
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.OK)
                .statusCode(200)
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

            return new Response.Builder()
                    .data(userService.getAllInBoard(boardId))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .build();
        } catch (AccountNotFoundException | IllegalArgumentException | NoSuchElementException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(400)
                    .build();
        } catch (NullPointerException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(500)
                    .build();
        }
    }

    /**
     * Adds a user to a board.
     *
     * @param userId  the id of the user to add to the board
     * @param boardId the id of the board to add the user to
     * @return a Response object with a message, status, and status code indicating the result of the add operation
     * if the add operation was successful, the data field will contain a UserInBoardDTO object representing the
     * added user in the board, the message field will contain the SuccessMessage.FOUND message,
     * the status field will be HttpStatus.OK, and the statusCode field will be 200
     * if the user or board with the given id does not exist in the database, the message field will contain the
     * AccountNotFoundException message, the status field will be HttpStatus.BAD_REQUEST, and the statusCode field
     * will be 400
     * if the combination of the given user and board already exists in the database, the message field will contain
     * the IllegalArgumentException message, the status field will be HttpStatus.BAD_REQUEST, and the statusCode field
     * will be 400
     * if the userId or boardId input is invalid, the message field will contain the NoSuchElementException message,
     * the status field will be HttpStatus.BAD_REQUEST, and the statusCode field will be 400
     * if there is a null pointer exception, the message field will contain the NullPointerException message,
     * the status field will be HttpStatus.BAD_REQUEST, and the statusCode field will be 500
     */
    public Response updateUserToBoard(ObjectsIdsRequest objectsIdsRequest) {
        try {
            Validations.validateUpdateUserToBoard(objectsIdsRequest.getBoardId(), objectsIdsRequest.getUserId(), objectsIdsRequest.getPermissionId());
            userService.updateUserToBoard(objectsIdsRequest.getBoardId(), objectsIdsRequest.getUserId(), objectsIdsRequest.getPermissionId());

            return new Response.Builder()
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .build();
        } catch (AccountNotFoundException | IllegalArgumentException | NoSuchElementException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(400)
                    .build();
        } catch (NullPointerException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(500)
                    .build();
        }
    }
}
