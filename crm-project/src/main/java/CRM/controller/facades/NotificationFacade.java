package CRM.controller.facades;

import CRM.entity.Attribute;
import CRM.entity.DTO.AttributeDTO;
import CRM.entity.DTO.NotificationDTO;
import CRM.entity.Notification;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.requests.NotificationRequest;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.response.Response;
import CRM.service.NotificationService;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import CRM.utils.enums.Regex;
import CRM.utils.enums.SuccessMessage;
import org.hibernate.NonUniqueObjectException;
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

    //TODO:DOCUMENTATION
    public Response getAllNotificationsForUserInBoard(ObjectsIdsRequest objectsIdsRequest) {
        try {
            Validations.validateIDs(objectsIdsRequest.getUserId(), objectsIdsRequest.getBoardId());
            List<Notification> notificationList = notificationService.getAllNotificationsForUserInBoard(objectsIdsRequest);
            return new Response.Builder()
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.FOUND)
                    .statusCode(200)
                    .data(NotificationDTO.createNotificationsDTOList(notificationList))
                    .build();
        } catch (AccountNotFoundException | IllegalArgumentException | NoSuchElementException e) {
            return new Response.Builder()
                    .statusCode(400)
                    .status(HttpStatus.BAD_REQUEST)
                    .message(e.getMessage())
                    .build();
        } catch (NullPointerException e) {
            return new Response.Builder()
                    .statusCode(500)
                    .status(HttpStatus.BAD_REQUEST)
                    .message(e.getMessage())
                    .build();
        }
    }
}
