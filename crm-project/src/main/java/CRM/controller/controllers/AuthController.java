package CRM.controller.controllers;

import CRM.controller.facades.AuthFacade;
import CRM.entity.DTO.UserDTO;
import CRM.entity.requests.LoginUserRequest;
import CRM.entity.requests.RegisterUserRequest;
import CRM.entity.response.Response;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;

@Controller
@RequestMapping(value = "/user/auth")
@AllArgsConstructor
@CrossOrigin
public class AuthController {

    private static Logger logger = LogManager.getLogger(AuthController.class.getName());

    @Autowired
    AuthFacade authFacade;

    /**
     * Handles a request to register a new user.
     *
     * @param user The request body containing the user's registration information.
     * @return A response containing the newly registered user's information. The response status will reflect the result of the registration attempt.
     */
    @RequestMapping(value = "register", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Response<UserDTO>> register(@RequestBody RegisterUserRequest user) {
        logger.info("in AuthController -> register");
        Response<UserDTO> response = authFacade.register(user);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Handles a request to login a user.
     *
     * @param user The request body containing the user's login credentials.
     * @return A response containing the user's access token. The response status will reflect the result of the login attempt.
     */
    @RequestMapping(value = "login", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Response<String>> login(@RequestBody LoginUserRequest user) throws AuthenticationException, AccountNotFoundException {
        logger.info("in AuthController -> login");
        Response<String> response = authFacade.login(user);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Handles a callback from a third-party login provider.
     *
     * @param code The authorization code provided by the login provider.
     * @return A response containing the user's access token. The response status will reflect the result of the login attempt.
     */
    @PostMapping("/third-party-login")
    public ResponseEntity<Response<String>> callback(@RequestParam String code) throws IOException {
        Response<String> response = authFacade.thirdPartyLogin(code);
        return new ResponseEntity<>(response, response.getStatus());
    }
}