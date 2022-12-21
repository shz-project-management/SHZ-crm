package CRM.controller.controllers;

import CRM.controller.facades.AttributeFacade;
import CRM.entity.Section;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.entity.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/section")
@AllArgsConstructor
@CrossOrigin
public class SectionController {

    @Autowired
    private AttributeFacade attributeFacade;

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
        Response response = attributeFacade.create(sectionRequest, Section.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Handle HTTP DELETE requests to delete a type.
     *
     * @param id The ID of the type to delete.
     * @return A ResponseEntity with the appropriate status and response body.
     */
    @DeleteMapping(value = "{id}")
    public ResponseEntity<Response> delete(@PathVariable Long id) {
        Response response = attributeFacade.delete(id, Section.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     This method is used to handle HTTP GET requests to the specified URL (type/{id}).
     The method takes the id of the type as a path variable and uses it to retrieve the type information from the attributeFacade object.
     @param id The id of the type to retrieve.
     @return A ResponseEntity object containing the Response object with the type information and the HTTP status code.
     */
    @GetMapping(value = "{id}")
    public ResponseEntity<Response> get(@PathVariable Long id){
        Response response = attributeFacade.get(id, Section.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * This method is used to handle HTTP GET requests to the specified URL (type/getAll).
     * The method retrieves all the types using the attributeFacade object and returns them in a Response object.
     *
     * @return A ResponseEntity object containing the Response object with all the types information and the HTTP status code.
     */
    @GetMapping(value = "getAll")
    public ResponseEntity<Response> getAll() {
        Response response = attributeFacade.getAll(Section.class);
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
    public ResponseEntity<Response> getAllTypesInBoard(@PathVariable Long boardId) {
        Response response = attributeFacade.getAllAttributesInBoard(boardId, Section.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Handles PUT requests to update type.
     *
     * @param sectionRequest An object containing the fields to update for the type.
     * @return A response object indicating the status of the update operation.
     */
    @PatchMapping(value = "/update/{typeId}", consumes = "application/json")
    public ResponseEntity<Response> update(@RequestBody UpdateObjectRequest sectionRequest, @PathVariable Long typeId) {
        Response response = attributeFacade.update(sectionRequest, typeId, Section.class);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
