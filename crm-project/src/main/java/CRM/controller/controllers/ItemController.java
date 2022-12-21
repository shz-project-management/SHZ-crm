package CRM.controller.controllers;

import CRM.controller.facades.SharedContentFacade;
import CRM.entity.Item;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.requests.ItemRequest;
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

    @DeleteMapping
    public ResponseEntity<Response> delete(@RequestBody List<Long> itemsIds) {
        Response response = sharedContentFacade.delete(itemsIds, Item.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PatchMapping(value = "update/{itemId}")
    public ResponseEntity<Response> update(@RequestBody UpdateObjectRequest updateItemRequest, @PathVariable Long itemId){
        Response response = sharedContentFacade.update(updateItemRequest, itemId, Item.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(value = "all-in-section/{boardId}")
    public ResponseEntity<Response> getAllItemsInSection(@PathVariable Long boardId){
        Response response = sharedContentFacade.getAllItemsInSection(boardId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(value = "{itemId}")
    public ResponseEntity<Response> get(@PathVariable Long itemId){
        Response response = sharedContentFacade.get(itemId, Item.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

}
