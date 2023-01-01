package CRM.controller.facades;

import CRM.entity.DTO.NotificationDTO;
import CRM.entity.Notification;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.response.Response;
import CRM.service.NotificationService;
import CRM.utils.enums.SuccessMessage;
import com.google.api.client.http.HttpStatusCodes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationFacadeTest {
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationFacade notificationFacade;

    @Test
    @DisplayName("Getting all user notifications in board success")
    public void getAllinBoard_ValidIds_Success() throws AccountNotFoundException {
        List<Notification> notifications = new ArrayList<>();
        Notification notification1 = new Notification();
        notification1.setId(1L);
        notification1.setName("Notification Name");
        notification1.setDescription("Notification Description");
        notification1.setNotificationDateTime(LocalDateTime.now());
        Notification notification2 = new Notification();
        notification2.setId(1L);
        notification2.setName("Notification Name");
        notification2.setDescription("Notification Description");
        notification2.setNotificationDateTime(LocalDateTime.now());
        notifications.add(notification1);
        notifications.add(notification2);
        when(notificationService.getAllinBoard(any(ObjectsIdsRequest.class))).thenReturn(notifications);
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        objectsIdsRequest.setUserId(1L);
        objectsIdsRequest.setBoardId(2L);
        Response<List<NotificationDTO>> response = notificationFacade.getAllinBoard(objectsIdsRequest);
        assertEquals(HttpStatus.FOUND, response.getStatus());
        assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.getStatusCode());
        assertEquals(SuccessMessage.FOUND.toString(), response.getMessage());
        assertEquals(notifications.size(), response.getData().size());
    }

    @Test
    @DisplayName("Getting all user notifications in board with illegal user id throws IllegalArgumentException")
    public void getAllinBoard_InvalidUserId_ServerErrorResponse() {
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        objectsIdsRequest.setUserId(-1L);
        assertThrows(IllegalArgumentException.class,
                ()->notificationFacade.getAllinBoard(objectsIdsRequest));
    }

    @Test
    @DisplayName("Getting all user notifications in board with illegal board id throws IllegalArgumentException")
    public void getAllinBoard_InvalidBoardId_ServerErrorResponse() {
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        objectsIdsRequest.setUserId(1L);
        objectsIdsRequest.setBoardId(-2L);
        assertThrows(IllegalArgumentException.class,
                ()->notificationFacade.getAllinBoard(objectsIdsRequest));
    }

    @Test
    @DisplayName("Getting all user notifications in board with null request throws NullPointerException")
    public void getAllInBoard_NullRequest_ServerErrorResponse() {
        assertThrows(NullPointerException.class,
                ()->notificationFacade.getAllinBoard(null));
    }

    @Test
    @DisplayName("Getting all user notifications in board with null board throws NullPointerException")
    public void getAllInBoard_NullBoard_ServerErrorResponse() {
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        objectsIdsRequest.setUserId(1L);
        assertThrows(NullPointerException.class,
                ()->notificationFacade.getAllinBoard(objectsIdsRequest));
    }

    @Test
    @DisplayName("Getting all user settings in board with null user throws NullPointerException")
    public void getAllInBoard_NullUser_ServerErrorResponse() {
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        objectsIdsRequest.setBoardId(1L);
        assertThrows(NullPointerException.class,
                ()->notificationFacade.getAllinBoard(objectsIdsRequest));
    }

    @Test
    @DisplayName("Getting all user notifications in board for user with no settings in the board returns empty list")
    public void getAllInBoard_UserDoesNotBelongToBoard_ReturnsEmptyList() throws AccountNotFoundException {
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        objectsIdsRequest.setUserId(1L);
        objectsIdsRequest.setBoardId(2L);
        Response<List<NotificationDTO>> response = notificationFacade.getAllinBoard(objectsIdsRequest);
        assertEquals(response.getData(), new ArrayList<>());
    }

    //delete notification tests

    @Test
    @DisplayName("delete notifications in board success")
    public void delete_ValidIds_Success() throws AccountNotFoundException {
        List<Notification> notifications = new ArrayList<>();
        Notification notification1 = new Notification();
        notification1.setId(1L);
        notification1.setName("Notification Name");
        notification1.setDescription("Notification Description");
        notification1.setNotificationDateTime(LocalDateTime.now());
        Notification notification2 = new Notification();
        notification2.setId(1L);
        notification2.setName("Notification Name");
        notification2.setDescription("Notification Description");
        notification2.setNotificationDateTime(LocalDateTime.now());
        notifications.add(notification1);
        notifications.add(notification2);
        when(notificationService.delete(any())).thenReturn(notifications);
        Response<List<NotificationDTO>> response = notificationFacade.delete(Arrays.asList(notification1.getId(), notification2.getId()));
        assertEquals(HttpStatusCodes.STATUS_CODE_NO_CONTENT, response.getStatusCode());
        assertEquals(SuccessMessage.DELETED.toString(), response.getMessage());
        assertEquals(notifications.size(), response.getData().size());
    }

    @Test
    @DisplayName("deleting notifications with empty list throws IllegalArgumentException")
    public void delete_EmptyIdList_ServerErrorResponse() {
        assertThrows(IllegalArgumentException.class,
                ()->notificationFacade.delete(new ArrayList<>()));
    }

    @Test
    @DisplayName("deleting notifications with null list throws NullPointerException")
    public void delete_NullList_ServerErrorResponse() {
        List<Long> ids = null;
        assertThrows(NullPointerException.class,
                ()->notificationFacade.delete(ids));
    }
}
