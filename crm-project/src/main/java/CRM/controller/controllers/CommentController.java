package CRM.controller.controllers;

import CRM.controller.facades.SharedContentFacade;
import CRM.entity.Comment;
import CRM.entity.requests.CommentRequest;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.entity.response.Response;
import CRM.utils.enums.UpdateField;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/comment")
@AllArgsConstructor
@CrossOrigin
public class CommentController {

    private static Logger logger = LogManager.getLogger(CommentController.class.getName());

    @Autowired
    SharedContentFacade sharedContentFacade;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Response> create(@RequestBody CommentRequest comment, @RequestAttribute Long userId,
                                           @RequestAttribute Long boardId) {
        Response response = sharedContentFacade.create(comment, userId, boardId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping
    public ResponseEntity<Response> delete(@RequestBody List<Long> commentsIds, @RequestAttribute Long boardId) {
        Response response = sharedContentFacade.delete(commentsIds, boardId, Comment.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PatchMapping(value = "update")
    public ResponseEntity<Response> update(@RequestBody UpdateObjectRequest updateObject, @RequestParam UpdateField field) {
        Response response = sharedContentFacade.update(updateObject, Comment.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(value = "{commentId}")
    public ResponseEntity<Response> get(@PathVariable Long commentId, @RequestParam Long boardId,
                                        @RequestParam Long sectionId, @RequestParam Long parentId) {
        ObjectsIdsRequest objectsIdsRequest = ObjectsIdsRequest.searchBoardSectionParentIds(commentId, boardId, sectionId, parentId);
        Response response = sharedContentFacade.get(objectsIdsRequest, Comment.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(value = "all-in-item/{itemId}")
    public ResponseEntity<Response> getAllInItem( @PathVariable Long itemId, @RequestAttribute Long boardId, @RequestParam Long sectionId) {
        ObjectsIdsRequest objectsIdsRequest = ObjectsIdsRequest.boardSectionItemIds(boardId, sectionId, itemId);
        Response response = sharedContentFacade.getAllInItem(objectsIdsRequest, Comment.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(value = "all-in-board")
    public ResponseEntity<Response> getAllCommentsInBoard(@RequestAttribute Long boardId) {
        Response response = sharedContentFacade.getAllCommentsInBoard(boardId);
        return new ResponseEntity<>(response, response.getStatus());
    }

//    @GetMapping(value = "all-in-section")
//    public ResponseEntity<Response> getAllCommentsInSection(@RequestParam Long boardId, @RequestParam Long sectionId) {
//        ObjectsIdsRequest objectsIdsRequest = ObjectsIdsRequest.boardSectionIds(boardId, sectionId);
//        Response response = sharedContentFacade.getAllCommentsInSection(objectsIdsRequest);
//        return new ResponseEntity<>(response, response.getStatus());
//    }

}
