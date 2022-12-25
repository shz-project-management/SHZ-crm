package CRM.controller.facades;

import CRM.entity.Attribute;
import CRM.entity.DTO.AttributeDTO;
import CRM.entity.DTO.NotificationDTO;
import CRM.entity.Notification;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.requests.NotificationRequest;
import CRM.entity.response.Response;
import CRM.service.NotificationService;
import CRM.utils.Validations;
import CRM.utils.enums.Regex;
import org.hibernate.NonUniqueObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class NotificationFacade {

    @Autowired
    private NotificationService notificationService;

    /**
     * This function creates a new attribute which could be status or type, both classes inherit from attribute and have no extra data.
     * It validates the attribute name using the NAME regex from the Regex enum,
     * finds the board it belongs to using the getBoardId from the attributeRequest object, creates a new Attribute object,
     * and calls the create function in the service that matches the class type that we get as a parameter,
     * with the help of convertFromClassToService function which gives us the relevant service based on clz Class which will hold Status or Type
     * the create function that we called will persist the attribute into the database.
     *
     * @param notificationRequest The request body, containing the necessary information to create a new attribute.
     *                         for info is the same for both classes Status and Type.
     * @return A Response object with the status of the create operation and the created attribute object, or an error message if the operation fails.
     */
    public Response create(NotificationRequest notificationRequest) {
        try {
            Validations.validateIDs(notificationRequest.getUserId(), notificationRequest.getFromUserId(),
                    notificationRequest.getBoardId());
            Validations.validateNotificationNumber(notificationRequest.getNotificationNumber());
            //validate user and board exists and user belongs to board
            Notification notification = notificationService.create(notificationRequest);
            return new Response.Builder()
                    .status(HttpStatus.CREATED)
                    .statusCode(201)
                    .data(NotificationDTO.createNotificationDTO(notification))
                    .build();
        } catch (IllegalArgumentException | NonUniqueObjectException | NoSuchElementException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(400)
                    .build();
        } catch (NullPointerException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(500)
                    .build();
        }
    }
}
