package CRM.controller.controllers;

import CRM.controller.facades.SectionFacade;
import CRM.entity.DTO.SectionDTO;
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
import java.util.Map;

@Controller
@RequestMapping(value = "/section")
@AllArgsConstructor
@CrossOrigin
public class SectionController {

    @Autowired
    private SectionFacade sectionFacade;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Handles a request to create a new section in a particular board.
     *
     * @param sectionRequest An object containing the necessary information to create a new section.
     * @param boardId        The ID of the board the new section will belong to.
     * @return A response indicating the result of the create operation. The response status will reflect the result of the operation.
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Response<List<SectionDTO>>> create(@RequestBody AttributeRequest sectionRequest, @RequestAttribute Long boardId) {
        Response<SectionDTO> response = sectionFacade.create(sectionRequest, boardId);
        messagingTemplate.convertAndSend("/section/" + boardId, response);
        return ResponseEntity.noContent().build();
    }

    /**
     * Handles a request to delete a particular section from a board.
     *
     * @param boardId   The ID of the board the section belongs to.
     * @param sectionId The ID of the section to be deleted.
     * @return A response indicating the result of the delete operation. The response status will reflect the result of the operation.
     */
    @DeleteMapping(value = "{sectionId}")
    public ResponseEntity<Response<Void>> delete(@RequestAttribute Long boardId, @PathVariable Long sectionId) {
        Response<Void> response = sectionFacade.delete(boardId, sectionId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Handles a request to retrieve a particular section from a board.
     *
     * @param sectionId The ID of the section to be retrieved.
     * @param boardId   The ID of the board the section belongs to.
     * @return A response containing the retrieved section, or an error message if the section could not be found. The response status will reflect the result of the operation.
     */
    @GetMapping(value = "{sectionId}")
    public ResponseEntity<Response<SectionDTO>> get(@PathVariable Long sectionId, @RequestAttribute Long boardId) {
        Response<SectionDTO> response = sectionFacade.get(boardId, sectionId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Handles a request to retrieve all sections in a board.
     *
     * @param boardId The ID of the board to retrieve the sections from.
     * @return A response containing a list of all the sections in the board, or an error message if the board could not be found. The response status will reflect the result of the operation.
     */
    @GetMapping(value = "getAll")
    public ResponseEntity<Response<List<SectionDTO>>> getAllInBoard(@RequestAttribute Long boardId) {
        Response<List<SectionDTO>> response = sectionFacade.getAllSectionsInBoard(boardId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Handles a request to retrieve items from a board that match certain filters.
     *
     * @param filters A map of filters to apply to the items, where the keys are the names of the filters and the values are the filter values.
     * @param boardId The ID of the board to retrieve the items from.
     * @return A response containing a list of items that match the specified filters, or an error message if the board could not be found or if there was an issue with the filters. The response status will reflect the result of the operation.
     */
    @PostMapping(value = "filter-items")
    public ResponseEntity<Response<List<SectionDTO>>> getFilteredItems(@RequestBody Map<String, List<String>> filters, @RequestAttribute Long boardId) {
        Response<List<SectionDTO>> response = sectionFacade.getFilteredItems(filters, boardId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * PatchMapping to update a section with a given id.
     *
     * @param updateItemRequest The UpdateObjectRequest object containing the updated section details and the list of object ids to be updated.
     * @param boardId           The id of the board to which the section belongs.
     * @return A ResponseEntity with no content.
     */
    @PatchMapping(value = "/update", consumes = "application/json")
    public ResponseEntity<Response<SectionDTO>> update(@RequestBody UpdateObjectRequest updateItemRequest, @RequestAttribute Long boardId) {
        updateItemRequest.getObjectsIdsRequest().setBoardId(boardId);
        return ResponseEntity.noContent().build();
    }
}
