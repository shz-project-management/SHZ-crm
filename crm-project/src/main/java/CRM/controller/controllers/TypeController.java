package CRM.controller.controllers;

import CRM.controller.facades.AttributeFacade;
import CRM.entity.Status;
import CRM.entity.Type;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.response.Response;
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
     * @param typeRequest The request body, containing the necessary information to create a new type.
     * @return A ResponseEntity containing a Response object with the status of the create operation and the created type object.
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Response> create(@RequestBody AttributeRequest typeRequest) {
        Response response = attributeFacade.create(typeRequest, Type.class);
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
        Response response = attributeFacade.delete(id, Type.class);
        return new ResponseEntity<>(response, response.getStatus());
    }
}