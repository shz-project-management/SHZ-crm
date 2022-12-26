package CRM.controller.controllers;

import CRM.controller.facades.AttributeFacade;
import CRM.controller.facades.SectionFacade;
import CRM.entity.Item;
import CRM.entity.Section;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.entity.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    /**
     * This function maps HTTP POST requests to the /create endpoint to the create function in the attributeFacade class.
     * attributeFacade handles Type method and also Status, since they both inherit Attribute
     * It consumes "application/json" and expects a typeRequest object to be passed as the request body.
     *
     * @param sectionRequest The request body, containing the necessary information to create a new type.
     * @return A ResponseEntity containing a Response object with the status of the create operation and the created type object.
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Response> create(@RequestBody AttributeRequest sectionRequest) {
        Response response = sectionFacade.create(sectionRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Handle HTTP DELETE requests to delete a type.
     *
     * @return A ResponseEntity with the appropriate status and response body.
     */
    @DeleteMapping(value = "{boardId}/{sectionId}")
    public ResponseEntity<Response> delete(@PathVariable Long boardId,@PathVariable Long sectionId) {
        Response response = sectionFacade.delete(boardId, sectionId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * This method is used to handle HTTP GET requests to the specified URL (type/{id}).
     * The method takes the id of the type as a path variable and uses it to retrieve the type information from the attributeFacade object.
     *
     * @return A ResponseEntity object containing the Response object with the type information and the HTTP status code.
     */
    @GetMapping(value = "{sectionId}")
    public ResponseEntity<Response> get(@PathVariable Long sectionId, @RequestParam Long boardId) {
        Response response = sectionFacade.get(boardId, sectionId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * This method is used to handle HTTP GET requests to the specified URL (type/getAll/{boardId}).
     * The method takes the id of the board as a path variable and uses it to retrieve all the types in that board using the attributeFacade object.
     *
     * @param boardId The id of the board whose types are to be retrieved.
     * @return A ResponseEntity object containing the Response object with the retrieved types and the HTTP status code.
     */
    @GetMapping(value = "getAll/{boardId}")
    public ResponseEntity<Response> getAllInBoard(@PathVariable Long boardId) {
        Response response = sectionFacade.getAllSectionsInBoard(boardId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping(value = "filter-items/{boardId}")
    public ResponseEntity<Response> getFilteredItems(@RequestBody Map<String, List<String>> filters, @PathVariable Long boardId) {
        Response response = sectionFacade.getFilteredItems(filters, boardId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    //TODO documentation
    @PatchMapping(value = "/update", consumes = "application/json")
    public ResponseEntity<Response> update(@RequestBody UpdateObjectRequest updateItemRequest) {
        Response response = sectionFacade.update(updateItemRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
