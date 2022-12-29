package CRM.controller.facades;

import CRM.entity.DTO.NotificationDTO;
import CRM.entity.Notification;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.response.Response;
import CRM.service.NotificationService;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import CRM.utils.enums.Regex;
import CRM.utils.enums.SuccessMessage;
import com.google.api.client.http.HttpStatusCodes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class NotificationFacade {

    private static Logger logger = LogManager.getLogger(NotificationFacade.class.getName());

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
    public Response<List<NotificationDTO>> getAllinBoard(ObjectsIdsRequest objectsIdsRequest) throws AccountNotFoundException {
        Validations.validateIDs(objectsIdsRequest.getUserId(), objectsIdsRequest.getBoardId());
        return Response.<List<NotificationDTO>>builder()
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.FOUND)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .data(NotificationDTO.createNotificationsDTOList(notificationService.getAllinBoard(objectsIdsRequest)))
                .build();
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
    public Response<List<NotificationDTO>> delete(List<Long> notificationsIds) {
        if (notificationsIds.size() == 0) throw new IllegalArgumentException(ExceptionMessage.EMPTY_LIST.toString());
        notificationsIds.forEach(notificationId -> {
            try {
                Validations.validate(notificationId, Regex.ID.getRegex());
            } catch (IllegalArgumentException | NullPointerException e) {
                logger.warn("Couldn't locate this notification");
            }
        });
        List<Notification> remainingNotifications = notificationService.delete(notificationsIds);
        return Response.<List<NotificationDTO>>builder()
                .message(SuccessMessage.DELETED.toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_NO_CONTENT)
                .data(NotificationDTO.createNotificationsDTOList(remainingNotifications))
                .build();
    }
}
