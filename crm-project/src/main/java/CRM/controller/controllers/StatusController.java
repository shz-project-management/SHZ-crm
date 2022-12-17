package CRM.controller.controllers;

import CRM.controller.facades.AttributeFacade;
import CRM.entity.requests.AttributeRequest;
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
     * This function maps HTTP POST requests to the /create endpoint to the create function in the statusFacade class.
     * It consumes "application/json" and expects a statusRequest object to be passed as the request body.
     *
     * @param statusRequest The request body, containing the necessary information to create a new status.
     * @return A ResponseEntity containing a Response object with the status of the create operation and the created status object.
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Response> create(@RequestBody AttributeRequest statusRequest) {
        Response response = attributeFacade.create(statusRequest);
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
        Response response = attributeFacade.delete(id);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
