package CRM.controller.controllers;

import CRM.controller.facades.AttributeFacade;
import CRM.controller.facades.SharedContentFacade;
import CRM.entity.Item;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.requests.ItemRequest;
import CRM.entity.response.Response;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/item")
@AllArgsConstructor
@CrossOrigin
public class ItemController {

    private static Logger logger = LogManager.getLogger(ItemController.class.getName());

    @Autowired
    private SharedContentFacade sharedContentFacade;
    @Autowired
    private AttributeFacade attributeFacade;

    // TODO:
    //  ------------------------------------------- //
    //  in each endpoint, call SharedContentFacade! //
    //  ------------------------------------------- //

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Response> create(@RequestBody ItemRequest item) {

        Response response = sharedContentFacade.create(item);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping
    public ResponseEntity<Response> delete(@RequestParam Long itemId) {

        Response response = sharedContentFacade.delete(itemId, Item.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PatchMapping
    public ResponseEntity<Response> update(@RequestParam ItemRequest itemRequest){
        return null;
    }

    @PatchMapping(value = "{itemId}/type")
    public ResponseEntity<Response> updateType(@PathVariable Long itemId, @RequestParam Long typeId, @RequestBody AttributeRequest attributeRequest){
        return null;
    }

    @PatchMapping(value = "{itemId}/status")
    public ResponseEntity<Response> updateStatus(@PathVariable Long itemId, @RequestParam Long statusId, @RequestBody AttributeRequest attributeRequest){
        return null;
    }

    @GetMapping(value = "all-in-board/{itemId}")
    public ResponseEntity<Response> getAllItemsInBoard(@PathVariable Long boardId){
        return null;
    }

    @GetMapping(value = "{itemId}")
    public ResponseEntity<Response> get(@PathVariable Long itemId){
        Response response = sharedContentFacade.get(itemId, Item.class);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
