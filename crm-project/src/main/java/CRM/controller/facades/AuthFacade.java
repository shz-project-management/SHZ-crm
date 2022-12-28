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
    public Response register(RegisterUserRequest user) {
        logger.info("in FacadeAuthController -> register");
        try {
            Validations.validateRegisteredUser(user);

            return Response.builder()
                    .data(UserDTO.createUserDTO(authService.register(user)))
                    .message(SuccessMessage.REGISTER.toString())
                    .status(HttpStatus.ACCEPTED)
                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                    .build();

        } catch (IllegalArgumentException e) {
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
     * Attempts to log in a user through a third party provider (e.g. GitHub) using the provided code.
     *
     * @param code the authorization code returned by the third party provider after the user grants access
     * @return a response object with a status code and message indicating the success or failure of the operation, and the user's login information
     * @throws IOException          if there is an error while communicating with the third party provider
     * @throws NullPointerException if the code parameter is null
     */
    public Response thirdPartyLogin(String code) {
        try {
            RegisterUserRequest user = githubCodeDecoder.getUserDataFromCode(code);

            return Response.builder()
                    .data(authService.thirdPartyLogin(user))
                    .message(SuccessMessage.REGISTER.toString())
                    .status(HttpStatus.ACCEPTED)
                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                    .build();

        } catch (IOException e) {
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
     * Logs a user in to the system.
     *
     * @param user The login credentials for the user.
     * @return A {@link Response} object containing a JWT token for the user, or an error message if the
     * login failed.
     * @throws NullPointerException     if any of the required fields in the user request are missing.
     * @throws IllegalArgumentException if any of the provided login credentials are invalid.
     * @throws AuthenticationException  if the provided login credentials are incorrect.
     * @throws AccountNotFoundException if the user with the given login credentials does not exist.
     */
    public Response login(LoginUserRequest user) {
        logger.info("in FacadeAuthController -> login");

        try {
            // validate the data given e.g. user.getEmail -> Validations.validate(Regex.EMAIL.getRegex(), email);
            // reminder: Validations.validate function throws an exception if failed, and doesn't return anything.
            //           so make sure to put it in a try/catch
            Validations.validateLoginUser(user);

            // after all validations are made, call the authService to login the user with the relevant information.
            // the return data in Response class has to include a JWT token.
            return Response.builder()
                    .message(SuccessMessage.LOGIN.toString())
                    .data(authService.login(user))
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                    .build();

        } catch (IllegalArgumentException | AccountNotFoundException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .build();
        } catch (AuthenticationException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_UNAUTHORIZED)
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
     * Activate function is responsible for activating email links.
     * If the link is not expired, make the user activated in the database.
     * If the link is expired, resend a new link to the user with a new token.
     * @param token - A link with activation token
     * @return Response with data and status 200 if good or 400 if something went wrong.
     */
//    public Response activate(String token) {
//        logger.info("in FacadeAuthController -> activate");
//        try {
//            String parsedToken = null;
//            parsedToken = URLDecoder.decode(token, StandardCharsets.UTF_8.toString()).replaceAll(" ", ".");
//            Claims claims = null;
//            claims = ConfirmationToken.decodeJWT(parsedToken);
//            if (userService.findById(Long.valueOf(claims.getId())).getActivated()) {
//                return new Response.Builder()
//                        .message("account already activated")
//                        .status(HttpStatus.CONFLICT)
//                        .statusCode(400)
//                        .build();
//            }
//            authService.activate(Long.valueOf(claims.getId()));
//            return new Response.Builder()
//                    .message("account activated successfully!")
//                    .status(HttpStatus.OK)
//                    .statusCode(200)
//                    .build();
//        } catch (ExpiredJwtException e) {
//            try {
//                String id = e.getClaims().getId();
//                User user = userService.findById(Long.valueOf(id));
//                EmailUtil.reactivateLink(user);
//                return new Response.Builder()
//                        .message("the link expired, new activation link has been sent")
//                        .statusCode(410)
//                        .status(HttpStatus.GONE)
//                        .build();
//            } catch (AccountNotFoundException ex) {
//                logger.error("in FacadeAuthController -> activate ->AccountNotFoundException-> " + e.getMessage());
//                return new Response.Builder()
//                        .message("activation link expired, failed to send new link")
//                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .statusCode(500)
//                        .build();
//            }
//
//        } catch (UnsupportedEncodingException | UnsupportedJwtException | MalformedJwtException | SignatureException |
//                 IllegalArgumentException e) {
//            logger.error("in FacadeAuthController -> activate -> " + e.getMessage());
//            return new Response.Builder()
//                    .message("failed to activate account")
//                    .status(HttpStatus.BAD_REQUEST)
//                    .statusCode(400)
//                    .build();
//        } catch (AccountNotFoundException e) {
//            logger.error("in FacadeAuthController -> activate -> AccountNotFoundException ->" + e.getMessage());
//            return new Response.Builder()
//                    .message("invalid token")
//                    .status(HttpStatus.FORBIDDEN)
//                    .statusCode(403)
//                    .build();
//        }
//    }
}
