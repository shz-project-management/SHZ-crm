package CRM.controller.controllers;

import CRM.controller.facades.BoardFacade;
import CRM.entity.Board;
import CRM.entity.requests.BoardRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.entity.response.Response;
import CRM.utils.Common;
import lombok.AllArgsConstructor;
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
    @PostMapping( value = "create" , consumes = "application/json")
    public ResponseEntity<Response> create(@RequestBody BoardRequest boardRequest) {
        Response response = boardFacade.create(boardRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Handle HTTP DELETE requests to delete a board.
     *
     * @param boardId The ID of the board to delete.
     * @return A ResponseEntity with the appropriate status and response body.
     */
    @DeleteMapping(value = "{boardId}")
    public ResponseEntity<Response> delete(@PathVariable Long boardId) {
        Response response = boardFacade.delete(boardId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * This method is used to handle HTTP GET requests to the specified URL (board/{id}).
     * The method takes the id of the board as a path variable and uses it to retrieve the board information from the boardFacade object.
     *
     * @param boardId The id of the board to retrieve.
     * @return A ResponseEntity object containing the Response object with the board information and the HTTP status code.
     */
    @GetMapping(value = "{boardId}")
    public ResponseEntity<Response> get(@PathVariable Long boardId, @RequestAttribute Long userId) {
        Response response = boardFacade.get(boardId, userId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Handles PUT requests to update board.
     *
     * @param boardRequest An object containing the fields to update for the board.
     * @return A response object indicating the status of the update operation.
     */
    @PatchMapping(value = "/update", consumes = "application/json")
    public ResponseEntity<Response> updateBoard(@RequestBody UpdateObjectRequest boardRequest) {
        Response response = boardFacade.updateBoard(boardRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

//    @RequestMapping(value = "query", method = RequestMethod.GET)
//    public ResponseEntity<String> query() {
//        Map<String, List<String>> filters = new HashMap<>();
//
//        List<String> types = new ArrayList<>();
//        List<String> statuses = new ArrayList<>();
//        List<String> importances = new ArrayList<>();
//
//        types.add("Bug");
//
//        statuses.add("Done");
//        statuses.add("Open");
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
}