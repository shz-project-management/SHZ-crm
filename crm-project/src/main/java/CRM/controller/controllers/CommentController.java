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
    public ResponseEntity<Response> delete(@RequestParam Long commentId) {
        Response response = sharedContentFacade.delete(commentId, Comment.class);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PatchMapping
    public ResponseEntity<Response> update(@RequestParam Long id, @RequestParam String field, @RequestParam String content) {
        return null;
    }

    @GetMapping(value = "all-in-item/{id}")
    public ResponseEntity<Response> getAllCommentsInItem(@DestinationVariable Long id) {
        return null;
    }

    @GetMapping(value = "{commentId}")
    public ResponseEntity<Response> get(@PathVariable Long commentId){
        Response response = sharedContentFacade.get(commentId, Comment.class);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
