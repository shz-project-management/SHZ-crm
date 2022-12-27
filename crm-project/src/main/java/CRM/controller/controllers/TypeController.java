package CRM.controller.controllers;

import CRM.controller.facades.AttributeFacade;
import CRM.entity.Status;
import CRM.entity.Type;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.entity.response.Response;
import CRM.utils.enums.UpdateField;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/type")
@AllArgsConstructor
@CrossOrigin
public class TypeController {

    @Autowired
    private AttributeFacade attributeFacade;

    /**
     * This function maps HTTP POST requests to the /create endpoint to the create function in the attributeFacade class.
     * attributeFacade handles Type method and also Status, since they both inherit Attribute
     * It consumes "application/json" and expects a typeRequest object to be passed as the request body.
     *
     * @return A ResponseEntity containing a Response object with the status of the create operation and the created type object.
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Response> create(@RequestBody AttributeRequest sectionRequest, @RequestAttribute Long boardId) {
        sectionRequest.setBoardId(boardId);
        Response response = attributeFacade.create(sectionRequest, Type.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Handle HTTP DELETE requests to delete a type.
     *
     * @return A ResponseEntity with the appropriate status and response body.
     */
    @DeleteMapping(value = "{sectionId}")
    public ResponseEntity<Response> delete(@RequestAttribute Long boardId, @PathVariable Long sectionId) {
        Response response = attributeFacade.delete(boardId, sectionId, Type.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

//    /**
//     This method is used to handle HTTP GET requests to the specified URL (type/{id}).
//     The method takes the id of the type as a path variable and uses it to retrieve the type information from the attributeFacade object.
//     @return A ResponseEntity object containing the Response object with the type information and the HTTP status code.
//     */
//    @GetMapping(value = "{boardId}/{sectionId}")
//    public ResponseEntity<Response> get(@PathVariable Long boardId, @PathVariable Long sectionId) {
//        Response response = attributeFacade.get(sectionId, boardId, Type.class);
//        return new ResponseEntity<>(response, response.getStatus());
//    }

    /**
     * This method is used to handle HTTP GET requests to the specified URL (type/getAll/{boardId}).
     * The method takes the id of the board as a path variable and uses it to retrieve all the types in that board using the attributeFacade object.
     *
     * @param boardId The id of the board whose types are to be retrieved.
     * @return A ResponseEntity object containing the Response object with the retrieved types and the HTTP status code.
     */
    @GetMapping(value = "getAll/{boardId}")
    public ResponseEntity<Response> getAllInBoard(@PathVariable Long boardId) {
        Response response = attributeFacade.getAllAttributesInBoard(boardId, Type.class);
        return new ResponseEntity<>(response, response.getStatus());
    }


    //TODO documentation
    @PatchMapping(value = "/update", consumes = "application/json")
    public ResponseEntity<Response> update(@RequestBody UpdateObjectRequest typeRequest, @RequestParam UpdateField field) {
        Response response = attributeFacade.update(typeRequest, Type.class);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
