package CRM.controller.controllers;

import CRM.controller.facades.SharedContentFacade;
import CRM.entity.Comment;
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

    // TODO:
    //  ------------------------------------------- //
    //  in each endpoint, call SharedContentFacade! //
    //  ------------------------------------------- //

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Response> create(@RequestBody Comment comment) {
        return null;
    }

    @DeleteMapping
    public ResponseEntity<Response> delete(@RequestParam Long id) {
        return null;
    }

    @PatchMapping
    public ResponseEntity<Response> update(@RequestParam Long id, @RequestParam String field, @RequestParam String content){
        return null;
    }

    @GetMapping(value = "all-in-item/{id}")
    public ResponseEntity<Response> getAllCommentsInItem(@DestinationVariable Long parentId){
        return null;
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<Response> get(@DestinationVariable Long id){
        return null;
    }
}
