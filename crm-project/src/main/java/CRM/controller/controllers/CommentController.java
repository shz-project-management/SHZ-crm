package CRM.controller.controllers;

import CRM.controller.facades.SharedContentFacade;
import CRM.entity.Comment;
import CRM.entity.Item;
import CRM.entity.requests.CommentRequest;
import CRM.entity.Item;
import CRM.entity.requests.UpdateObjectRequest;
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

    @DeleteMapping(value = "/{boardId}")
    public ResponseEntity<Response> delete(@RequestBody List<Long> commentsIds, @PathVariable Long boardId) {
        Response response = sharedContentFacade.delete(commentsIds, boardId ,Comment.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PatchMapping(value = "update/{boardId}/{sectionId}/{commentId}")
    public ResponseEntity<Response> update(@RequestBody UpdateObjectRequest updateItemRequest,@PathVariable Long boardId, @PathVariable Long sectionId, @PathVariable Long commentId){
        Response response = sharedContentFacade.update(updateItemRequest, boardId, sectionId, commentId, Comment.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(value = "{boardId}/{sectionId}/{parentItemId}/{commentId}")
    public ResponseEntity<Response> get(@PathVariable Long boardId, @PathVariable Long sectionId, @PathVariable Long parentItemId, @PathVariable Long commentId){
        Response response = sharedContentFacade.get(sectionId, boardId, commentId, parentItemId, Comment.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(value = "all-in-item/{boardId}/{sectionId}/{itemId}")
    public ResponseEntity<Response> getAllInItem(@PathVariable Long boardId, @PathVariable Long sectionId, @PathVariable Long itemId){
        Response response = sharedContentFacade.getAllInItem(itemId, sectionId, boardId,  Comment.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(value = "all-in-board/{boardId}")
    public ResponseEntity<Response> getAllCommentsInBoard(@PathVariable Long boardId){
        Response response = sharedContentFacade.getAllCommentsInBoard(boardId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(value = "all-in-status/{boardId}/{sectionId}")
    public ResponseEntity<Response> getAllCommentsInSection(@PathVariable Long boardId ,@PathVariable Long sectionId){
        Response response = sharedContentFacade.getAllCommentsInSection(boardId, sectionId);
        return new ResponseEntity<>(response, response.getStatus());
    }

}
