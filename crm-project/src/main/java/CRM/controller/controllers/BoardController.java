package CRM.controller.controllers;

import CRM.controller.facades.BoardFacade;
import CRM.entity.requests.BoardRequest;
import CRM.entity.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
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

    /**
     * Handle HTTP DELETE requests to delete a board.
     *
     * @param id The ID of the board to delete.
     * @return A ResponseEntity with the appropriate status and response body.
     */
    @DeleteMapping(value = "{id}")
    public ResponseEntity<Response> create(@PathVariable Long id) {
        Response response = boardFacade.delete(id);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     This method is used to handle HTTP GET requests to the specified URL (board/{id}).
     The method takes the id of the board as a path variable and uses it to retrieve the board information from the boardFacade object.
     @param id The id of the board to retrieve.
     @return A ResponseEntity object containing the Response object with the board information and the HTTP status code.
     */
    @GetMapping(value = "{id}")
    public ResponseEntity<Response> get(@PathVariable Long id){

        Response response = boardFacade.get(id);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     This method is used to handle HTTP GET requests to the specified URL (board/getAll).
     The method retrieves all the boards using the boardFacade object and returns them in a Response object.
     @return A ResponseEntity object containing the Response object with all the board information and the HTTP status code.
     */
    @GetMapping(value = "getAll")
    public ResponseEntity<Response> getAll(){

        Response response = boardFacade.getAll();
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     This method is used to handle HTTP GET requests to the specified URL (board/getAll/{userId}).
     The method takes the id of the user as a path variable and uses it to retrieve all the boards created by the user using the boardFacade object.
     @param userId The id of the user whose boards are to be retrieved.
     @return A ResponseEntity object containing the Response object with the retrieved boards and the HTTP status code.
     */
    @GetMapping(value = "getAll/{userId}")
    public ResponseEntity<Response> getAllBoardsOfUser(@PathVariable Long userId){

        Response response = boardFacade.getAllBoardsOfUser(userId);
        return new ResponseEntity<>(response, response.getStatus());
    }

}