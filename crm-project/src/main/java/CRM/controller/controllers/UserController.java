package CRM.controller.controllers;

import CRM.controller.facades.UserFacade;
import CRM.entity.response.Response;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/user")
@AllArgsConstructor
@CrossOrigin
public class UserController {

    private static Logger logger = LogManager.getLogger(UserController.class.getName());


    @Autowired
    private UserFacade userFacade;

    @GetMapping(value = "{id}")
    public ResponseEntity<Response> get(@PathVariable Long id){

        Response response = userFacade.get(id);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Response> delete(@PathVariable Long id){

        Response response = userFacade.delete(id);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(value = "getAll")
    public ResponseEntity<Response> getAll(){

        Response response = userFacade.getAll();
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(value = "getAll/{boardId}")
    public ResponseEntity<Response> getAllInBoard(@PathVariable Long boardId){

        Response response = userFacade.getAllInBoard(boardId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping(value = "add/{userId}/toBoard/{boardId}")
    public ResponseEntity<Response> addUserToBoard(@PathVariable Long userId, @PathVariable Long boardId){

        Response response = userFacade.addUserToBoard(userId, boardId);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
