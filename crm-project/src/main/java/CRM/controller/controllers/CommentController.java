package CRM.controller.controllers;

import CRM.controller.facades.SharedContentFacade;
import CRM.entity.Comment;
import CRM.entity.DTO.CommentDTO;
import CRM.entity.DTO.SectionDTO;
import CRM.entity.requests.CommentRequest;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.requests.UpdateObjectRequest;
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
@RequestMapping(value = "/comment")
@AllArgsConstructor
@CrossOrigin
public class CommentController {

    private static final Logger logger = LogManager.getLogger(CommentController.class.getName());

    @Autowired
    SharedContentFacade sharedContentFacade;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Handles a request to create a new comment.
     *
     * @param comment The request body containing the comment's information.
     * @param userId  The ID of the user making the request.
     * @param boardId The ID of the board the comment belongs to.
     * @return A response indicating the result of the create operation. The response status will reflect the result of the create operation.
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Response<List<CommentDTO>>> create(@RequestBody CommentRequest comment, @RequestAttribute Long userId, @RequestAttribute Long boardId) throws AccountNotFoundException {
        Response<List<CommentDTO>> response = sharedContentFacade.create(comment, userId, boardId);
        messagingTemplate.convertAndSend("/comment/" + comment.getParentItemId() + "/" + boardId, response);
        return ResponseEntity.noContent().build();
    }

    /**
     * Handles a request to delete one or more comments.
     *
     * @param commentsIds The IDs of the comments to be deleted.
     * @param boardId     The ID of the board the comments belong to.
     * @return A response indicating the result of the delete operation. The response status will reflect the result of the delete operation.
     */
    @DeleteMapping
    public ResponseEntity<Response<Void>> delete(@RequestBody List<Long> commentsIds, @RequestAttribute Long boardId) throws AccountNotFoundException {
        Response<Void> response = sharedContentFacade.delete(commentsIds, boardId, Comment.class);
        messagingTemplate.convertAndSend("/comment/" + boardId, response);
        return ResponseEntity.noContent().build();
    }

    /**
     * Handles a request to update a comment.
     *
     * @param updateObject The request body containing the updates to be made to the comment.
     * @param boardId      The ID of the board the comment belongs to.
     * @return A response indicating the result of the update operation. The response status will reflect the result of the update operation.
     */
    @PatchMapping(value = "update")
    public ResponseEntity<Response<SectionDTO>> update(@RequestBody UpdateObjectRequest updateObject, @RequestAttribute Long boardId) throws NoSuchFieldException, AccountNotFoundException {
        Response<SectionDTO> response = sharedContentFacade.update(updateObject, Comment.class);
        messagingTemplate.convertAndSend("/comment/" + boardId, response);
        return ResponseEntity.noContent().build();
    }

    /**
     * Handles a request to retrieve a comment.
     *
     * @param commentId The ID of the comment to be retrieved.
     * @param boardId   The ID of the board the comment belongs to.
     * @param sectionId The ID of the section the comment belongs to.
     * @param parentId  The ID of the parent item the comment belongs to.
     * @return A response containing the requested comment's information. The response status will reflect the result of the retrieve operation.
     */
    @GetMapping(value = "{commentId}")
    public ResponseEntity<Response<CommentDTO>> get(@PathVariable Long commentId, @RequestParam Long boardId, @RequestParam Long sectionId, @RequestParam Long parentId) {
        ObjectsIdsRequest objectsIdsRequest = ObjectsIdsRequest.searchBoardSectionParentIds(commentId, boardId, sectionId, parentId);
        Response<CommentDTO> response = sharedContentFacade.get(objectsIdsRequest, Comment.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Handles a request to retrieve all comments belonging to a particular item.
     *
     * @param itemId The ID of the item the comments belong to.
     * @param boardId The ID of the board the comments belong to.
     * @param sectionId The ID of the section the comments belong to.
     * @return A response containing a list of the requested comments' information. The response status will reflect the result of the retrieve operation.
     */
    @GetMapping(value = "all-in-item/{itemId}")
    public ResponseEntity<Response<List<CommentDTO>>> getAllInItem(@PathVariable Long itemId, @RequestAttribute Long boardId, @RequestParam Long sectionId) {
        ObjectsIdsRequest objectsIdsRequest = ObjectsIdsRequest.boardSectionItemIds(boardId, sectionId, itemId);
        Response<List<CommentDTO>> response = sharedContentFacade.getAllInItem(objectsIdsRequest, Comment.class);
        return new ResponseEntity<>(response, response.getStatus());
    }
}