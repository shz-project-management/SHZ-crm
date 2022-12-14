package CRM.controller.controllers;

import CRM.controller.facades.AttributeFacade;
import CRM.controller.facades.SharedContentFacade;
import CRM.entity.Item;
import CRM.entity.response.Response;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
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
    public ResponseEntity<Response> create(@RequestBody Item item) {
        return null;
    }

    @DeleteMapping
    public ResponseEntity<Response> delete(@RequestParam Long itemId) {
        return null;
    }

    @PatchMapping
    public ResponseEntity<Response> update(@RequestParam Long itemId, @RequestParam String field, @RequestParam String content){
        return null;
    }

    @PatchMapping(value = "{itemId}/type")
    public ResponseEntity<Response> updateType(@DestinationVariable Long itemId, @RequestParam Long typeId){
        return null;
    }

    @PatchMapping(value = "{itemId}/status")
    public ResponseEntity<Response> updateStatus(@DestinationVariable Long itemId, @RequestParam Long statusId){
        return null;
    }

    @GetMapping(value = "all-in-board/{itemId}")
    public ResponseEntity<Response> getAllItemsInBoard(@DestinationVariable Long boardId){
        return null;
    }

    @GetMapping(value = "all-in-item/{itemId}")
    public ResponseEntity<Response> getAllItemsInItem(@DestinationVariable Long itemId){
        sharedContentFacade.getAllInItem(itemId, Item.class);
        return null;
    }

    @GetMapping(value = "{itemId}")
    public ResponseEntity<Response> get(@DestinationVariable Long itemId){
        return null;
    }
}
