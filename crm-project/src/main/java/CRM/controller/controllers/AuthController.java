package CRM.controller.controllers;

import CRM.controller.facades.AuthFacade;
import CRM.entity.User;
import CRM.entity.requests.LoginUserRequest;
import CRM.entity.requests.RegisterUserRequest;
import CRM.entity.response.Response;
import CRM.utils.Common;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "/user/auth")
@AllArgsConstructor
@CrossOrigin
public class AuthController {

    private static Logger logger = LogManager.getLogger(AuthController.class.getName());

    @Autowired
    AuthFacade authFacade;

    /**
     * Register function is responsible for creating new users and adding them to the database.
     * Users will use their personal information to create a new account: email, password, name.
     * @param user - User with email, name and password
     * @return ResponseEntity with our Response with data and status 201 if good or 400 if something went wrong.
     */
    @RequestMapping(value = "register", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Response> register(@RequestBody RegisterUserRequest user) {
        logger.info("in AuthController -> register");

        Response response = authFacade.register(user);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Login function is responsible for logging user into the system.
     * This function accepts only 2 parameters: email, password.
     * If the credentials match to the database's information, it will allow the user to use its functionalities.
     * A token will be returned in a successful request.
     * @param user - user's details with email and password to check if correct
     * @return ResponseEntity with our Response with user's token and status 200 if good or 400 if something went wrong.
     */
    @RequestMapping(value = "login", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Response> login(@RequestBody LoginUserRequest user) {
        logger.info("in AuthController -> login");

        Response response = authFacade.login(user);
        return new ResponseEntity<>(response, response.getStatus());
    }

//    @RequestMapping(value = "query", method = RequestMethod.GET)
//    public ResponseEntity<String> query() {
//
//        Map<String, List<String>> filters = new HashMap<>();
//
//        List<String> types = new ArrayList<>();
//        List<String> statuses = new ArrayList<>();
//        List<String> importances = new ArrayList<>();
//
//        types.add("task");
//        types.add("subTask");
//
//        statuses.add("done");
//        statuses.add("open");
//
//        importances.add("1");
//        importances.add("3");
//        importances.add("5");
//
//        filters.put("type", types);
//        filters.put("status", statuses);
//        filters.put("importance", importances);
//
//        return new ResponseEntity<>(Common.generateQuery(filters), HttpStatus.OK);
//    }

//    /**
//     * Activate function is responsible for activating email links.
//     * If the link is not expired, make the user activated in the database.
//     * If the link is expired, resend a new link to the user with a new token.
//     *
//     * @param token - A link with activation token
//     * @return ResponseEntity<Response>  with data and status 200 if good or 400 if something went wrong.
//     */
//    @RequestMapping(value = "activate", method = RequestMethod.POST)
//    public ResponseEntity<Response> activate(@RequestParam String token) {
//        logger.info("in AuthController -> activate");
//        Response response = facadeAuth.activate(token);
//        return new ResponseEntity<>(response, response.getStatus());
//    }



}
