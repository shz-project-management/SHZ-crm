package CRM.controller.controllers;

import CRM.controller.facades.AttributeFacade;
import CRM.entity.DTO.AttributeDTO;
import CRM.entity.DTO.BoardDTO;
import CRM.entity.Type;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.entity.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/type")
@AllArgsConstructor
@CrossOrigin
public class TypeController {

    @Autowired
    private AttributeFacade attributeFacade;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * This function maps HTTP POST requests to the /create endpoint to the create function in the attributeFacade class.
     * attributeFacade handles Type method and also Status, since they both inherit Attribute
     * It consumes "application/json" and expects a typeRequest object to be passed as the request body.
     *
     * @return A ResponseEntity containing a Response object with the status of the create operation and the created type object.
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<List<AttributeDTO>> create(@RequestBody AttributeRequest sectionRequest, @RequestAttribute Long boardId) {
        sectionRequest.setBoardId(boardId);
        Response<List<AttributeDTO>> response = attributeFacade.create(sectionRequest, Type.class);
        messagingTemplate.convertAndSend("/attribute/" + boardId, response);
        return ResponseEntity.noContent().build();
    }

    /**
     * Handle HTTP DELETE requests to delete a type.
     *
     * @return A ResponseEntity with the appropriate status and response body.
     */
    @DeleteMapping(value = "{typeId}")
    public ResponseEntity<Response<BoardDTO>> delete(@RequestAttribute Long boardId, @PathVariable Long typeId) {
        Response<BoardDTO> response = attributeFacade.delete(boardId, typeId, Type.class);
        messagingTemplate.convertAndSend("/attribute/" + boardId, response);
        return ResponseEntity.noContent().build();
    }

    /**
     * This method is used to handle HTTP GET requests to the specified URL (type/getAll/{boardId}).
     * The method takes the id of the board as a path variable and uses it to retrieve all the types in that board using the attributeFacade object.
     *
     * @param boardId The id of the board whose types are to be retrieved.
     * @return A ResponseEntity object containing the Response object with the retrieved types and the HTTP status code.
     */
    @GetMapping(value = "getAll/{boardId}")
    public ResponseEntity<Response<List<AttributeDTO>>> getAllInBoard(@PathVariable Long boardId) {
        Response<List<AttributeDTO>> response = attributeFacade.getAllAttributesInBoard(boardId, Type.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * PatchMapping to update a board with a given id.
     *
     * @param typeRequest The UpdateObjectRequest object containing the updated board details and the list of object ids to be updated.
     * @return A ResponseEntity with no content.
     */
    @PatchMapping(value = "/update", consumes = "application/json")
    public ResponseEntity<Response<BoardDTO>> update(@RequestBody UpdateObjectRequest typeRequest) throws NoSuchFieldException {
        Response<BoardDTO> response = attributeFacade.update(typeRequest, Type.class);
        messagingTemplate.convertAndSend("/attribute/" + typeRequest.getObjectsIdsRequest().getBoardId(), response);
        return ResponseEntity.noContent().build();
    }
}
