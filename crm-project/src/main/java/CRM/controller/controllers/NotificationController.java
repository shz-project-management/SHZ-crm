package CRM.controller.controllers;

import CRM.controller.facades.NotificationFacade;
import CRM.entity.requests.BoardRequest;
import CRM.entity.requests.NotificationRequest;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/notification")
@AllArgsConstructor
@CrossOrigin
public class NotificationController {

    @Autowired
    private NotificationFacade notificationFacade;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    //TODO:DOCUMENTATION
    @GetMapping(value = "getAll-inBoard")
    public ResponseEntity<Response> getAllinBoard(@RequestAttribute Long boardId, @RequestAttribute Long userId) {
        ObjectsIdsRequest objectsIdsRequest = ObjectsIdsRequest.boardUserIds(boardId, userId);
        Response response = notificationFacade.getAllinBoard(objectsIdsRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping(value = "delete")
    public ResponseEntity<Response> delete(@RequestBody List<Long> notificationsIds, @RequestAttribute Long boardId) {
        Response response = notificationFacade.delete(notificationsIds);
        messagingTemplate.convertAndSend("/notification/" + boardId, response);
        return ResponseEntity.noContent().build();
    }
}
