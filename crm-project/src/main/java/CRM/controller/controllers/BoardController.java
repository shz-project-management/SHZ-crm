package CRM.controller.controllers;

import CRM.controller.facades.BoardFacade;
import CRM.entity.requests.BoardRequest;
import CRM.entity.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/board")
@AllArgsConstructor
@CrossOrigin
public class BoardController {

    @Autowired
    private BoardFacade boardFacade;

    /**
     * This function maps HTTP POST requests to the /create endpoint to the create function in the BoardFacade class.
     * It consumes "application/json" and expects a BoardRequest object to be passed as the request body.
     *
     * @param boardRequest The request body, containing the necessary information to create a new board.
     * @return A ResponseEntity containing a Response object with the status of the create operation and the created board object.
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Response> create(@RequestBody BoardRequest boardRequest) {
        Response response = boardFacade.create(boardRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping()
    public ResponseEntity<Response> create(@RequestParam long boardID) {
        Response response = boardFacade.delete(boardID);
        return new ResponseEntity<>(response, response.getStatus());
    }
}