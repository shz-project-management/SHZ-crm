package CRM.controller.controllers;

import CRM.controller.facades.NotificationFacade;
import CRM.entity.DTO.NotificationDTO;
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

import javax.security.auth.login.AccountNotFoundException;
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

    /**
     * Handles a request to retrieve all notifications in a particular board.
     *
     * @param boardId The ID of the board the notifications belong to.
     * @param userId The ID of the user the notifications are being retrieved for.
     * @return A response containing the requested notifications' information. The response status will reflect the result of the retrieve operation.
     */
    @GetMapping(value = "getAll-inBoard")
    public ResponseEntity<Response<List<NotificationDTO>>> getAllinBoard(@RequestAttribute Long boardId, @RequestAttribute Long userId) throws AccountNotFoundException {
        ObjectsIdsRequest objectsIdsRequest = ObjectsIdsRequest.boardUserIds(boardId, userId);
        Response<List<NotificationDTO>> response = notificationFacade.getAllinBoard(objectsIdsRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Handles a request to delete a list of notifications.
     *
     * @param notificationsIds A list of the IDs of the notifications to be deleted.
     * @param boardId The ID of the board the notifications belong to.
     * @return A response indicating the result of the delete operation. The response status will reflect the result of the operation.
     */
    @DeleteMapping(value = "delete")
    public ResponseEntity<Response<Void>> delete(@RequestBody List<Long> notificationsIds, @RequestAttribute Long boardId) {
        Response<Void> response = notificationFacade.delete(notificationsIds);
        messagingTemplate.convertAndSend("/notification/" + boardId, response);
        return ResponseEntity.noContent().build();
    }
}
