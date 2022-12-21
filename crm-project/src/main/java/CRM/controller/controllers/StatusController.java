package CRM.controller.controllers;

import CRM.controller.facades.AttributeFacade;
import CRM.entity.Status;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.entity.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/status")
@AllArgsConstructor
@CrossOrigin
public class StatusController {

    @Autowired
    private AttributeFacade attributeFacade;

    /**
     * This function maps HTTP POST requests to the /create endpoint to the create function in the attributeFacade class.
     * attributeFacade handles Status method and also Type, since they both inherit Attribute
     * It consumes "application/json" and expects a statusRequest object to be passed as the request body.
     *
     * @param statusRequest The request body, containing the necessary information to create a new status.
     * @return A ResponseEntity containing a Response object with the status of the create operation and the created status object.
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Response> create(@RequestBody AttributeRequest statusRequest) {
        Response response = attributeFacade.create(statusRequest, Status.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Handle HTTP DELETE requests to delete a status.
     *
     * @param id The ID of the status to delete.
     * @return A ResponseEntity with the appropriate status and response body.
     */
    @DeleteMapping(value = "{id}")
    public ResponseEntity<Response> delete(@PathVariable Long id) {
        Response response = attributeFacade.delete(id, Status.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     This method is used to handle HTTP GET requests to the specified URL (status/{id}).
     The method takes the id of the status as a path variable and uses it to retrieve the status information from the attributeFacade object.
     @param id The id of the status to retrieve.
     @return A ResponseEntity object containing the Response object with the status information and the HTTP status code.
     */
    @GetMapping(value = "{id}")
    public ResponseEntity<Response> get(@PathVariable Long id){
        Response response = attributeFacade.get(id, Status.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * This method is used to handle HTTP GET requests to the specified URL (status/getAll).
     * The method retrieves all the statuses using the attributeFacade object and returns them in a Response object.
     *
     * @return A ResponseEntity object containing the Response object with all the statuses information and the HTTP status code.
     */
    @GetMapping(value = "getAll")
    public ResponseEntity<Response> getAll() {
        Response response = attributeFacade.getAll(Status.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * This method is used to handle HTTP GET requests to the specified URL (status/getAll/{boardId}).
     * The method takes the id of the board as a path variable and uses it to retrieve all the statuses in that board using the attributeFacade object.
     *
     * @param boardId The id of the board whose statuses are to be retrieved.
     * @return A ResponseEntity object containing the Response object with the retrieved statuses and the HTTP status code.
     */
    @GetMapping(value = "getAll/{boardId}")
    public ResponseEntity<Response> getAllStatusesInBoard(@PathVariable Long boardId) {
        Response response = attributeFacade.getAllAttributesInBoard(boardId, Status.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Handles PATCH requests to update status.
     *
     * @param statusRequest An object containing the fields to update for the status.
     * @return A response object indicating the status of the update operation.
     */
    @PatchMapping(value = "/update/{statusId}", consumes = "application/json")
    public ResponseEntity<Response> update(@RequestBody UpdateObjectRequest statusRequest, @PathVariable Long statusId) {
        Response response = attributeFacade.update(statusRequest, statusId, Status.class);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
