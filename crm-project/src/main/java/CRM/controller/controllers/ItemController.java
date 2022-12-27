package CRM.controller.controllers;

import CRM.controller.facades.SharedContentFacade;
import CRM.entity.Item;
import CRM.entity.requests.*;
import CRM.entity.response.Response;
import CRM.utils.enums.UpdateField;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @PostMapping(consumes = "application/json")
    public ResponseEntity<Response> create(@RequestBody ItemRequest item, @RequestAttribute Long userId, @RequestAttribute Long boardId) {
        item.setBoardId(boardId);
        item.setUserId(userId);
        Response response = sharedContentFacade.create(item);
        messagingTemplate.convertAndSend("/item/" + boardId, response);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Response> delete(@RequestBody List<Long> itemsIds, @RequestAttribute Long boardId) {
        Response response = sharedContentFacade.delete(itemsIds, boardId, Item.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PatchMapping(value = "update")
    public ResponseEntity<Response> update(@RequestBody UpdateObjectRequest updateItemRequest, @RequestParam UpdateField field,
                                           @RequestAttribute Long userId, @RequestAttribute Long boardId) {
        updateItemRequest.getObjectsIdsRequest().setUserId(userId);
        updateItemRequest.getObjectsIdsRequest().setBoardId(boardId);
        Response response = sharedContentFacade.update(updateItemRequest, Item.class);
        messagingTemplate.convertAndSend("/item/" + boardId, response);
        return ResponseEntity.noContent().build();    }

    @GetMapping(value = "{itemId}")
    public ResponseEntity<Response> get(@PathVariable Long itemId, @RequestAttribute Long boardId,
                                        @RequestParam Long sectionId, @RequestParam Long parentId) {
        ObjectsIdsRequest objectsIdsRequest = ObjectsIdsRequest.searchBoardSectionParentIds(itemId, boardId, sectionId, parentId);
        Response response = sharedContentFacade.get(objectsIdsRequest, Item.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(value = "all-in-section/{sectionId}")
    public ResponseEntity<Response> getAllItemsInSection(@RequestAttribute Long boardId, @PathVariable Long sectionId) {
        ObjectsIdsRequest objectsIdsRequest = ObjectsIdsRequest.boardSectionIds(boardId, sectionId);
        Response response = sharedContentFacade.getAllItemsInSection(objectsIdsRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping(value = "assign-to-user")
    public ResponseEntity<Response> assignToUser(@RequestBody ObjectsIdsRequest objIds, @RequestAttribute Long boardId) {
        objIds.setUserId(boardId);
        Response response = sharedContentFacade.assignToUser(objIds, Item.class);
        messagingTemplate.convertAndSend("/item/" + boardId, response);
        return ResponseEntity.noContent().build();
    }


}
