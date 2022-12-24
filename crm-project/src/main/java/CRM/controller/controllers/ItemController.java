package CRM.controller.controllers;

import CRM.controller.facades.SharedContentFacade;
import CRM.entity.Comment;
import CRM.entity.Item;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.requests.ItemRequest;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.entity.response.Response;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/item")
@AllArgsConstructor
@CrossOrigin
public class ItemController {

    private static Logger logger = LogManager.getLogger(ItemController.class.getName());

    @Autowired
    private SharedContentFacade sharedContentFacade;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Response> create(@RequestBody ItemRequest item) {
        Response response = sharedContentFacade.create(item);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping(value = "{boardId}")
    public ResponseEntity<Response> delete(@RequestBody List<Long> itemsIds, @PathVariable Long boardId) {
        Response response = sharedContentFacade.delete(itemsIds, boardId, Item.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PatchMapping(value = "update")
    public ResponseEntity<Response> update(@RequestBody UpdateObjectRequest updateItemRequest){
        Response response = sharedContentFacade.update(updateItemRequest, updateItemRequest.getObjectsIdsRequest().getItemId(), Item.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(value = "{itemId}")
    public ResponseEntity<Response> get(@RequestBody ObjectsIdsRequest objectsIdsRequest ,@PathVariable Long itemId) {
        Response response = sharedContentFacade.get(objectsIdsRequest, itemId , Item.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(value = "all-in-section")
    public ResponseEntity<Response> getAllItemsInSection(@RequestBody ObjectsIdsRequest objectsIdsRequest) {
        Response response = sharedContentFacade.getAllItemsInSection(objectsIdsRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
