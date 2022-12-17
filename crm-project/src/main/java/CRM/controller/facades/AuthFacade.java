package CRM.controller.facades;

import CRM.entity.User;
import CRM.entity.requests.LoginUserRequest;
import CRM.entity.requests.RegisterUserRequest;
import CRM.entity.response.Response;
import CRM.service.AuthService;
import CRM.utils.Validations;
import CRM.utils.enums.Regex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import javax.security.auth.login.AccountNotFoundException;

@Component
public class AuthFacade {

    private static Logger logger = LogManager.getLogger(AuthFacade.class.getName());

    @Autowired
    private AuthService authService;

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
            // validate the data given e.g. user.getEmail -> Validations.validate(Regex.EMAIL.getRegex(), email);
            // reminder: Validations.validate function throws an exception if failed, and doesn't return anything.
            //           so make sure to put it in a try/catch
            Validations.validateRegisteredUser(user);

            // after all validations are made, call the authService to register the user with the relevant information.
            // there's no need to send an activation email at the moment (according to Assaf).
            // If we have time, we'll add it later on.
            return new Response.Builder()
                    .data(authService.register(user))
                    .message("The user has been successfully saved in the database")
                    .status(HttpStatus.ACCEPTED)
                    .statusCode(201)
                    .build();

        } catch (IllegalArgumentException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(400).build();
        } catch (NullPointerException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(500).build();
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
            return new Response.Builder()
                    .message("Successfully logged in to the system")
                    .data(authService.login(user))
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .build();

        } catch (IllegalArgumentException | AccountNotFoundException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(400).build();
        } catch (AuthenticationException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(401).build();
        } catch (NullPointerException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(500).build();
        }
    }

    /**
     * Activate function is responsible for activating email links.
     * If the link is not expired, make the user activated in the database.
     * If the link is expired, resend a new link to the user with a new token.
     *
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
