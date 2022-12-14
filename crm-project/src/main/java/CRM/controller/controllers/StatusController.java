package CRM.controller.controllers;

import CRM.controller.facades.AttributeFacade;
import CRM.entity.DTO.AttributeDTO;
import CRM.entity.DTO.BoardDTO;
import CRM.entity.Status;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.entity.response.Response;
import com.google.api.client.http.HttpStatusCodes;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/status")
@AllArgsConstructor
@CrossOrigin
public class StatusController {

    @Autowired
    private AttributeFacade attributeFacade;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * This function maps HTTP POST requests to the /create endpoint to the create function in the attributeFacade class.
     * attributeFacade handles Status method and also Type, since they both inherit Attribute
     * It consumes "application/json" and expects a statusRequest object to be passed as the request body.
     *
     * @return A ResponseEntity containing a Response object with the status of the create operation and the created status object.
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Response<List<AttributeDTO>>> create(@RequestBody AttributeRequest sectionRequest, @RequestAttribute Long boardId, @RequestParam Boolean force) {
        sectionRequest.setBoardId(boardId);
        Response<List<AttributeDTO>> response = attributeFacade.create(sectionRequest, Status.class, force);

        if (response.getStatusCode().equals(HttpStatusCodes.STATUS_CODE_CONFLICT))
            return new ResponseEntity<>(response, response.getStatus());

        messagingTemplate.convertAndSend("/attribute/" + boardId, response);
        return ResponseEntity.noContent().build();
    }

    /**
     * DeleteMapping to delete a section with a given id.
     *
     * @param boardId  The id of the board to which the section belongs.
     * @param statusId The id of the section to be deleted.
     * @return A ResponseEntity with no content.
     */
    @DeleteMapping(value = "{statusId}")
    public ResponseEntity<Response<BoardDTO>> delete(@RequestAttribute Long boardId, @PathVariable Long statusId) {
        Response<BoardDTO> response = attributeFacade.delete(boardId, statusId, Status.class);
        messagingTemplate.convertAndSend("/attribute/" + boardId, response);
        return ResponseEntity.noContent().build();
    }

    /**
     * This method is used to handle HTTP GET requests to the specified URL (status/getAll/{boardId}).
     * The method takes the id of the board as a path variable and uses it to retrieve all the statuses in that board using the attributeFacade object.
     *
     * @param boardId The id of the board whose statuses are to be retrieved.
     * @return A ResponseEntity object containing the Response object with the retrieved statuses and the HTTP status code.
     */
    @GetMapping(value = "getAll/{boardId}")
    public ResponseEntity<Response<List<AttributeDTO>>> getAllInBoard(@PathVariable Long boardId) {
        Response<List<AttributeDTO>> response = attributeFacade.getAllAttributesInBoard(boardId, Status.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * PatchMapping to update a board with a given id.
     *
     * @param updateObjectRequest The UpdateObjectRequest object containing the updated board details and the list of object ids to be updated.
     * @return A ResponseEntity with no content.
     */
    @PatchMapping(value = "/update", consumes = "application/json")
    public ResponseEntity<Response<BoardDTO>> update(@RequestBody UpdateObjectRequest updateObjectRequest) throws NoSuchFieldException {
        Response<BoardDTO> response = attributeFacade.update(updateObjectRequest, Status.class);
        messagingTemplate.convertAndSend("/attribute/" + updateObjectRequest.getObjectsIdsRequest().getBoardId(), response);
        return ResponseEntity.noContent().build();
    }
}
