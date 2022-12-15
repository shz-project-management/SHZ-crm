package CRM.controller.facades;

import CRM.entity.User;
import CRM.entity.response.Response;
import CRM.service.UserService;
import CRM.utils.Validations;
import CRM.utils.enums.Regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;

@Component
public class UserFacade {

    @Autowired
    private UserService userService;

    /**
     * Retrieves a user from the database by their ID.
     * @param id the ID of the user to retrieve
     * @return a Response object with the user data, a success message, and a status code of 200
     *         If no user with the specified ID exists, the response will include a bad request message and a status code of 400
     */
    public Response get(Long id) {
        try {
            Validations.validate(String.valueOf(id), Regex.ID.getRegex());
            return new Response.Builder()
                    .data(userService.get(id))
                    .message("Successfully found a user")
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .build();
        } catch (AccountNotFoundException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(400)
                    .build();
        }
    }

}
