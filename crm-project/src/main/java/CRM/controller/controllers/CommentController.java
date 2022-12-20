package CRM.controller.controllers;

import CRM.controller.facades.SharedContentFacade;
import CRM.entity.Comment;
import CRM.entity.Item;
import CRM.entity.requests.CommentRequest;
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

    @DeleteMapping
    public ResponseEntity<Response> delete(@RequestBody List<Long> commentsIds) {
        Response response = sharedContentFacade.delete(commentsIds, Comment.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PatchMapping
    public ResponseEntity<Response> update(@RequestParam Long id, @RequestParam String field, @RequestParam String content) {
        return null;
    }

    @GetMapping(value = "{commentId}")
    public ResponseEntity<Response> get(@PathVariable Long commentId){
        Response response = sharedContentFacade.get(commentId, Comment.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(value = "all-in-item/{itemId}")
    public ResponseEntity<Response> getAllInItem(@PathVariable Long itemId){
        Response response = sharedContentFacade.getAllInItem(itemId, Comment.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(value = "all-in-board/{boardId}")
    public ResponseEntity<Response> getAllCommentsInBoard(@PathVariable Long boardId){
        Response response = sharedContentFacade.getAllCommentsInBoard(boardId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(value = "all-in-status/{statusId}")
    public ResponseEntity<Response> getAllCommentsInStatus(@PathVariable Long statusId){
        Response response = sharedContentFacade.getAllCommentsInStatus(statusId);
        return new ResponseEntity<>(response, response.getStatus());
    }

}
