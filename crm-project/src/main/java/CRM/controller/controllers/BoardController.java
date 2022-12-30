package CRM.controller.controllers;

import CRM.controller.facades.BoardFacade;
import CRM.entity.DTO.BoardDTO;
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

import javax.naming.NoPermissionException;
import javax.security.auth.login.AccountNotFoundException;

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
     * Handles a request to create a new board.
     *
     * @param boardRequest The request body containing the board's information.
     * @param userId       The ID of the user making the request.
     * @return A response containing the newly created board's information. The response status will reflect the result of the create operation.
     */
    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<Response<BoardDTO>> create(@RequestBody BoardRequest boardRequest, @RequestAttribute Long userId) throws AccountNotFoundException {
        boardRequest.setCreatorUserId(userId);
        Response<BoardDTO> response = boardFacade.create(boardRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Handles a request to delete a board.
     *
     * @param boardId The ID of the board to be deleted.
     * @return A response indicating the result of the delete operation. The response status will reflect the result of the delete operation.
     */
    @DeleteMapping
    public ResponseEntity<Response<Void>> delete(@RequestAttribute Long boardId) {
        boardFacade.delete(boardId);
        Response<Void> response = boardFacade.delete(boardId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Handles a request to retrieve a board.
     *
     * @param boardId The ID of the board to be retrieved.
     * @param userId  The ID of the user making the request.
     * @return A response containing the requested board's information. The response status will reflect the result of the retrieve operation.
     */
    @GetMapping
    public ResponseEntity<Response<BoardDTO>> get(@RequestAttribute Long boardId, @RequestAttribute Long userId) throws NoPermissionException {
        Response<BoardDTO> response = boardFacade.get(boardId, userId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Handles a request to update a board.
     *
     * @param boardRequest The request body containing the updates to be made to the board.
     * @param boardId      The ID of the board to be updated.
     * @return A response indicating the result of the update operation. The response status will reflect the result of the update operation.
     */
    @PatchMapping(value = "update", consumes = "application/json")
    public ResponseEntity<Response<BoardDTO>> updateBoard(@RequestBody UpdateObjectRequest boardRequest, @RequestAttribute Long boardId) throws NoSuchFieldException {
        boardRequest.setObjectsIdsRequest(new ObjectsIdsRequest());
        boardRequest.getObjectsIdsRequest().setBoardId(boardId);
        Response<BoardDTO> response = boardFacade.updateBoard(boardRequest);
        messagingTemplate.convertAndSend("/board/" + boardId, response);
        return ResponseEntity.noContent().build();
    }
}