package CRM.controller.controllers;

import CRM.controller.facades.NotificationFacade;
import CRM.entity.requests.BoardRequest;
import CRM.entity.requests.NotificationRequest;
import CRM.entity.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/notification")
@AllArgsConstructor
@CrossOrigin
public class NotificationController {

    @Autowired
    private NotificationFacade notificationFacade;

    /**
     * This function maps HTTP POST requests to the /create endpoint to the create function in the BoardFacade class.
     * It consumes "application/json" and expects a BoardRequest object to be passed as the request body.
     *
     * @param notificationRequest The request body, containing the necessary information to create a new board.
     * @return A ResponseEntity containing a Response object with the status of the create operation and the created board object.
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Response> create(@RequestBody NotificationRequest notificationRequest) {
        Response response = notificationFacade.create(notificationRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

}
