package CRM.controller.facades;

import CRM.entity.DTO.NotificationDTO;
import CRM.entity.Notification;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.response.Response;
import CRM.service.NotificationService;
import CRM.utils.Validations;
import CRM.utils.enums.Regex;
import CRM.utils.enums.SuccessMessage;
import com.google.api.client.http.HttpStatusCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class NotificationFacade {

    @Autowired
    private NotificationService notificationService;

    /**
     * Retrieves a list of notifications for a specified user on a specified board.
     *
     * @param objectsIdsRequest the objects IDs request containing the user ID and board ID
     * @return a response object with a status code and message indicating the success or failure of the operation, and a list of notification objects
     * @throws AccountNotFoundException if the specified user does not exist
     * @throws IllegalArgumentException if the user ID or board ID is invalid or not provided
     * @throws NoSuchElementException   if the specified board does not exist
     * @throws NullPointerException     if the objects IDs request object is null
     */
    public Response getAllForUserPerBoard(ObjectsIdsRequest objectsIdsRequest) {
        try {
            Validations.validateIDs(objectsIdsRequest.getUserId(), objectsIdsRequest.getBoardId());
            List<Notification> notificationList = notificationService.getAllForUserPerBoard(objectsIdsRequest);
            return Response.builder()
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.FOUND)
                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                    .data(NotificationDTO.createNotificationsDTOList(notificationList))
                    .build();
        } catch (AccountNotFoundException | IllegalArgumentException | NoSuchElementException e) {
            return Response.builder()
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .status(HttpStatus.BAD_REQUEST)
                    .message(e.getMessage())
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(e.getMessage())
                    .build();
        }
    }

    /**
     * Deletes a list of notifications by ID.
     *
     * @param notificationsIds a list of notification IDs
     * @return a response object with a status code and message indicating the success or failure of the operation
     * @throws IllegalArgumentException if any of the notification IDs are invalid or not provided
     * @throws NoSuchElementException   if any of the specified notifications do not exist
     * @throws NullPointerException     if the notifications IDs list is null
     */
    public Response delete(List<Long> notificationsIds) {
        try {
            notificationsIds.forEach(id -> {
                try {
                    Validations.validate(id, Regex.ID.getRegex());
                } catch (IllegalArgumentException | NullPointerException e) {
                }
            });
            notificationService.delete(notificationsIds);
            return Response.builder()
                    .message(SuccessMessage.DELETED.toString())
                    .status(HttpStatus.NO_CONTENT)
                    .statusCode(HttpStatusCodes.STATUS_CODE_NO_CONTENT)
                    .build();
        } catch (IllegalArgumentException | NoSuchElementException e) {
            return Response.builder()
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .status(HttpStatus.BAD_REQUEST)
                    .message(e.getMessage())
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(e.getMessage())
                    .build();
        }
    }
}
