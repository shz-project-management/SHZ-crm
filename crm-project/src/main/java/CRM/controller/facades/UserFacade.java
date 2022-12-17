package CRM.controller.facades;

import CRM.entity.response.Response;
import CRM.service.UserService;
import CRM.utils.Validations;
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
     * @param id The unique ID of the user to retrieve.
     * @return A {@link Response} object containing the retrieved user, or an error message if the user
     * could not be found.
     * @throws AccountNotFoundException if the user with the given ID does not exist.
     */
    public Response get(Long id) {
        try {
            Validations.validate(String.valueOf(id), Regex.ID.getRegex());
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
     * Retrieves all users.
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
     * @param boardId the ID of the board to retrieve users from
     * @return a {@link Response} object containing the list of users or an error message
     */
    public Response getAllInBoard(Long boardId) {
        try {
            Validations.validate(String.valueOf(boardId), Regex.ID.getRegex());
            return new Response.Builder()
                    .data(userService.getAllInBoard(boardId))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .build();
        } catch (IllegalArgumentException | NoSuchElementException e) {
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
