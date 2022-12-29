package CRM.controller.facades;

import CRM.entity.DTO.UserDTO;
import CRM.entity.requests.LoginUserRequest;
import CRM.entity.requests.RegisterUserRequest;
import CRM.entity.response.Response;
import CRM.service.AuthService;
import CRM.utils.GithubCodeDecoder;
import CRM.utils.Validations;
import CRM.utils.enums.SuccessMessage;
import com.google.api.client.http.HttpStatusCodes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;

@Component
public class AuthFacade {

    private static Logger logger = LogManager.getLogger(AuthFacade.class.getName());

    @Autowired
    private AuthService authService;
    @Autowired
    private GithubCodeDecoder githubCodeDecoder;

    /**
     * Registers a new user in the system.
     *
     * @param user The registration information for the user.
     * @return A {@link Response} object containing the saved user, or an error message if the
     * registration failed.
     * @throws IllegalArgumentException if any of the provided registration information is invalid.
     * @throws NullPointerException     if any of the required fields in the user request are missing.
     */
    public Response<UserDTO> register(RegisterUserRequest user) {
        logger.info("in FacadeAuthController -> register");
        Validations.validateRegisteredUser(user);
        return Response.<UserDTO>builder()
                .data(UserDTO.createUserDTO(authService.register(user)))
                .message(SuccessMessage.REGISTER.toString())
                .status(HttpStatus.ACCEPTED)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
    }

    /**
     * Attempts to log in a user through a third party provider (e.g. GitHub) using the provided code.
     *
     * @param code the authorization code returned by the third party provider after the user grants access
     * @return a response object with a status code and message indicating the success or failure of the operation, and the user's login information
     * @throws IOException          if there is an error while communicating with the third party provider
     * @throws NullPointerException if the code parameter is null
     */
    public Response<String> thirdPartyLogin(String code) throws IOException {
        RegisterUserRequest user = githubCodeDecoder.getUserDataFromCode(code);
        return Response.<String>builder()
                .data(authService.thirdPartyLogin(user))
                .message(SuccessMessage.REGISTER.toString())
                .status(HttpStatus.ACCEPTED)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
    }

    /**
     * Logs a user in to the system.
     *
     * @param user The login credentials for the user.
     * @return A {@link Response} object containing a JWT token for the user, or an error message if the
     * login failed.
     * @throws AuthenticationException  if the provided login credentials are incorrect.
     * @throws AccountNotFoundException if the user with the given login credentials does not exist.
     */
    // FIXME: Make Response<String> somehow...
    public Response<String> login(LoginUserRequest user) throws AuthenticationException, AccountNotFoundException {
        logger.info("in FacadeAuthController -> login");
        Validations.validateLoginUser(user);
        return Response.<String>builder()
                .data(authService.login(user))
                .message(SuccessMessage.LOGIN.toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
    }
}
