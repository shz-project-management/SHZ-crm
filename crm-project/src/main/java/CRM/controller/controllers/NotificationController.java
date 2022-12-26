package CRM.controller.controllers;

import CRM.controller.facades.NotificationFacade;
import CRM.entity.requests.BoardRequest;
import CRM.entity.requests.NotificationRequest;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/notification")
@AllArgsConstructor
@CrossOrigin
public class NotificationController {

    @Autowired
    private NotificationFacade notificationFacade;

    //TODO:DOCUMENTATION
    @GetMapping(value = "getAll-user-in-board")
    public ResponseEntity<Response> getAllForUserPerBoard(@RequestBody ObjectsIdsRequest objectsIdsRequest) {
        Response response = notificationFacade.getAllForUserPerBoard(objectsIdsRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping(value = "delete/{notificationId}")
    public ResponseEntity<Response> delete(@PathVariable Long notificationId) {
        Response response = notificationFacade.delete(notificationId);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
