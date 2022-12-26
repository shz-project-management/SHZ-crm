package CRM.controller.controllers;

import CRM.controller.facades.SharedContentFacade;
import CRM.entity.Comment;
import CRM.entity.Item;
import CRM.entity.requests.*;
import CRM.entity.response.Response;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        Response response = sharedContentFacade.update(updateItemRequest, Item.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(value = "{itemId}")
    public ResponseEntity<Response> get(@PathVariable Long itemId, @RequestParam Long boardId,
                                        @RequestParam Long sectionId, @RequestParam Long parentId) {
        ObjectsIdsRequest objectsIdsRequest = ObjectsIdsRequest.searchBoardSectionParentIds(itemId, boardId, sectionId, parentId);
        Response response = sharedContentFacade.get(objectsIdsRequest, Item.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(value = "all-in-section/{sectionId}")
    public ResponseEntity<Response> getAllItemsInSection(@RequestParam Long boardId, @PathVariable Long sectionId) {
        ObjectsIdsRequest objectsIdsRequest = ObjectsIdsRequest.boardSectionIds(boardId, sectionId);
        Response response = sharedContentFacade.getAllItemsInSection(objectsIdsRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping(value = "assign-to-user")
    public ResponseEntity<Response> assignToUser(@RequestBody ObjectsIdsRequest objIds, @RequestAttribute Long userId) {
        objIds.setUserId(userId);
        Response response = sharedContentFacade.assignToUser(objIds, Item.class);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
