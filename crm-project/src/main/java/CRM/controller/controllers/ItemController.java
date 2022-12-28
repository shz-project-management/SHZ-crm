package CRM.controller.controllers;

import CRM.controller.facades.SharedContentFacade;
import CRM.entity.DTO.ItemDTO;
import CRM.entity.DTO.SectionDTO;
import CRM.entity.DTO.UserPermissionDTO;
import CRM.entity.Item;
import CRM.entity.requests.*;
import CRM.entity.response.Response;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
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

    /**
     * Handles a request to create a new item.
     *
     * @param item    The request body containing the information for the new item.
     * @param userId  The ID of the user creating the item.
     * @param boardId The ID of the board the item belongs to.
     * @return A response indicating the result of the create operation. The response status will reflect the result of the create operation.
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Response<SectionDTO>> create(@RequestBody ItemRequest item, @RequestAttribute Long userId, @RequestAttribute Long boardId) throws AccountNotFoundException {
        item.setBoardId(boardId);
        item.setUserId(userId);
        Response response = sharedContentFacade.create(item);
        messagingTemplate.convertAndSend("/item/" + boardId, response);
        return ResponseEntity.noContent().build();
    }

    /**
     * Handles a request to delete one or more items.
     *
     * @param itemsIds The IDs of the items to be deleted.
     * @param boardId  The ID of the board the items belong to.
     * @return A response indicating the result of the delete operation. The response status will reflect the result of the delete operation.
     */
    @DeleteMapping
    public ResponseEntity<Response<Void>> delete(@RequestBody List<Long> itemsIds, @RequestAttribute Long boardId) throws AccountNotFoundException {
        Response<Void> response = sharedContentFacade.delete(itemsIds, boardId, Item.class);
        messagingTemplate.convertAndSend("/item/" + boardId, response);
        return ResponseEntity.noContent().build();
    }

    /**
     * Handles a request to update an existing item.
     *
     * @param updateItemRequest The request body containing the updated information for the item.
     * @param userId            The ID of the user updating the item.
     * @param boardId           The ID of the board the item belongs to.
     * @return A response indicating the result of the update operation. The response status will reflect the result of the update operation.
     */
    @PatchMapping(value = "update")
    public ResponseEntity<Response<SectionDTO>> update(@RequestBody UpdateObjectRequest updateItemRequest, @RequestAttribute Long userId, @RequestAttribute Long boardId) throws NoSuchFieldException, AccountNotFoundException {
        updateItemRequest.getObjectsIdsRequest().setUserId(userId);
        updateItemRequest.getObjectsIdsRequest().setBoardId(boardId);
        Response<SectionDTO> response = sharedContentFacade.update(updateItemRequest, Item.class);
        messagingTemplate.convertAndSend("/item/" + boardId, response);
        return ResponseEntity.noContent().build();
    }

    /**
     * Handles a request to retrieve information for a particular item.
     *
     * @param itemId    The ID of the item to be retrieved.
     * @param boardId   The ID of the board the item belongs to.
     * @param sectionId The ID of the section the item belongs to.
     * @param parentId  The ID of the parent the item belongs to.
     * @return A response containing the requested item's information. The response status will reflect the result of the retrieve operation.
     */
    @GetMapping(value = "{itemId}")
    public ResponseEntity<Response<ItemDTO>> get(@PathVariable Long itemId, @RequestAttribute Long boardId, @RequestParam Long sectionId, @RequestParam Long parentId) {
        ObjectsIdsRequest objectsIdsRequest = ObjectsIdsRequest.searchBoardSectionParentIds(itemId, boardId, sectionId, parentId);
        Response<ItemDTO> response = sharedContentFacade.get(objectsIdsRequest, Item.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Handles a request to retrieve all items in a particular section.
     *
     * @param boardId   The ID of the board the section belongs to.
     * @param sectionId The ID of the section the items belong to.
     * @return A response containing the requested items' information. The response status will reflect the result of the retrieve operation.
     */
    @GetMapping(value = "all-in-section/{sectionId}")
    public ResponseEntity<Response<List<ItemDTO>>> getAllItemsInSection(@RequestAttribute Long boardId, @PathVariable Long sectionId) {
        ObjectsIdsRequest objectsIdsRequest = ObjectsIdsRequest.boardSectionIds(boardId, sectionId);
        Response<List<ItemDTO>> response = sharedContentFacade.getAllItemsInSection(objectsIdsRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Handles a request to assign an item to a particular user.
     *
     * @param objIds  An object containing the IDs of the item to be assigned and the user it is being assigned to.
     * @param boardId The ID of the board the item belongs to.
     * @return A response indicating the result of the assignment operation. The response status will reflect the result of the operation.
     */
    @PostMapping(value = "assign-to-user")
    public ResponseEntity<Response<SectionDTO>> assignToUser(@RequestBody ObjectsIdsRequest objIds, @RequestAttribute Long boardId) throws AccountNotFoundException {
        objIds.setUserId(boardId);
        Response<SectionDTO> response = sharedContentFacade.assignToUser(objIds, Item.class);
        messagingTemplate.convertAndSend("/item/" + boardId, response);
        return ResponseEntity.noContent().build();
    }
}
