package CRM.controller.controllers;

import CRM.controller.facades.SharedContentFacade;
import CRM.entity.Comment;
import CRM.entity.requests.CommentRequest;
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
@RequestMapping(value = "/comment")
@AllArgsConstructor
@CrossOrigin
public class CommentController {

    private static Logger logger = LogManager.getLogger(CommentController.class.getName());

    @Autowired
    SharedContentFacade sharedContentFacade;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Response> create(@RequestBody CommentRequest comment) {

        Response response = sharedContentFacade.create(comment);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping(value = "{boardId}")
    public ResponseEntity<Response> delete(@RequestBody List<Long> commentsIds, @PathVariable Long boardId) {
        Response response = sharedContentFacade.delete(commentsIds, boardId ,Comment.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PatchMapping(value = "update")
    public ResponseEntity<Response> update(@RequestBody UpdateObjectRequest updateObject){
        Response response = sharedContentFacade.update(updateObject, updateObject.getObjectsIdsRequest().getCommentId() , Comment.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(value = "{commentId}")
    public ResponseEntity<Response> get(@RequestBody ObjectsIdsRequest objectsIdsRequest, @PathVariable Long commentId){
        Response response = sharedContentFacade.get(objectsIdsRequest, commentId ,Comment.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(value = "all-in-item")
    public ResponseEntity<Response> getAllInItem(@RequestParam Long boardId, @RequestParam Long sectionId, @RequestParam Long itemId){
        Response response = sharedContentFacade.getAllInItem(boardId, sectionId, itemId, Comment.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(value = "all-in-board/{boardId}")
    public ResponseEntity<Response> getAllCommentsInBoard(@PathVariable Long boardId){
        Response response = sharedContentFacade.getAllCommentsInBoard(boardId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(value = "all-in-section")
    public ResponseEntity<Response> getAllCommentsInSection(ObjectsIdsRequest objectsIdsRequest){
        Response response = sharedContentFacade.getAllCommentsInSection(objectsIdsRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

}
