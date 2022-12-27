package CRM.controller.controllers;

import CRM.controller.facades.BoardFacade;
import CRM.entity.requests.BoardRequest;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.entity.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/board")
@AllArgsConstructor
@CrossOrigin
public class BoardController {

    @Autowired
    private BoardFacade boardFacade;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * This function maps HTTP POST requests to the /create endpoint to the create function in the BoardFacade class.
     * It consumes "application/json" and expects a BoardRequest object to be passed as the request body.
     *
     * @param boardRequest The request body, containing the necessary information to create a new board.
     * @return A ResponseEntity containing a Response object with the status of the create operation and the created board object.
     */
    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<Response> create(@RequestBody BoardRequest boardRequest, @RequestAttribute Long userId) {
        boardRequest.setCreatorUserId(userId);
        Response response = boardFacade.create(boardRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Handle HTTP DELETE requests to delete a board.
     *
     * @param boardId The ID of the board to delete.
     * @return A ResponseEntity with the appropriate status and response body.
     */
    @DeleteMapping
    public ResponseEntity<Response> delete(@RequestAttribute Long boardId) {
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
    @GetMapping
    public ResponseEntity<Response> get(@RequestAttribute Long boardId, @RequestAttribute Long userId) {
        Response response = boardFacade.get(boardId, userId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Handles PUT requests to update board.
     *
     * @param boardRequest An object containing the fields to update for the board.
     * @return A response object indicating the status of the update operation.
     */
    @PatchMapping(value = "update", consumes = "application/json")
    public ResponseEntity<Response> updateBoard(@RequestBody UpdateObjectRequest boardRequest, @RequestAttribute Long boardId) {
        boardRequest.setObjectsIdsRequest(new ObjectsIdsRequest());
        boardRequest.getObjectsIdsRequest().setBoardId(boardId);
        Response response = boardFacade.updateBoard(boardRequest);
        messagingTemplate.convertAndSend("/board/" + boardId, response);
        return ResponseEntity.noContent().build();
    }


}