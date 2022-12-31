package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.NotificationRequest;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.repository.BoardRepository;
import CRM.repository.NotificationRepository;
import CRM.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BoardRepository boardRepository;
    @InjectMocks
    private NotificationService notificationService;

    @Test
    @DisplayName("get all notifications in board success")
    public void getAllinBoard_ValidParameters_Success() throws AccountNotFoundException {
        Board board = Mockito.mock(Board.class);
        board.setId(2L);
        User user = Mockito.mock(User.class);
        user.setId(1L);
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        objectsIdsRequest.setUserId(1L);
        objectsIdsRequest.setBoardId(board.getId());
        List<Notification> expectedNotifications = new ArrayList<>();
        expectedNotifications.add(new Notification());
        expectedNotifications.add(new Notification());
        when(notificationRepository.findByUser_IdAndBoard_Id(objectsIdsRequest.getUserId(), objectsIdsRequest.getBoardId()))
                .thenReturn(expectedNotifications);
        when(userRepository.findById(objectsIdsRequest.getUserId())).thenReturn(Optional.of(user));
        when(boardRepository.findById(objectsIdsRequest.getBoardId())).thenReturn(Optional.of(board));
        when(board.doesBoardIncludeUser(1L)).thenReturn(true);
        List<Notification> notifications = notificationService.getAllinBoard(objectsIdsRequest);
        assertEquals(expectedNotifications, notifications);
    }

    @Test
    @DisplayName("get all user notifications in board with non exists user throws AccountNotFoundException")
    public void getAllInBoard_UserDoesNotExist_ThrowsAccountNotFoundException() {
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        Board board = Mockito.mock(Board.class);
        board.setId(2L);
        objectsIdsRequest.setUserId(1L);
        objectsIdsRequest.setBoardId(2L);
        assertThrows(AccountNotFoundException.class,
                ()->notificationService.getAllinBoard(objectsIdsRequest));
    }

    @Test
    @DisplayName("get all user notifications in board with non exists board throws AccountNotFoundException")
    public void getAllInBoard_BoardDoesNotExist_ThrowsAccountNotFoundException() {
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        User user = Mockito.mock(User.class);
        user.setId(1L);
        objectsIdsRequest.setUserId(1L);
        objectsIdsRequest.setBoardId(2L);
        assertThrows(AccountNotFoundException.class,
                ()->notificationService.getAllinBoard(objectsIdsRequest));
    }

    @Test
    @DisplayName("get all user notifications in board with user does not belong to board throws NoSuchElementException")
    public void getAllInBoard_UserDoesNotBelongToBoard_ThrowsNoSuchElementException() {
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        objectsIdsRequest.setUserId(1L);
        objectsIdsRequest.setBoardId(2L);
        User user = new User();
        user.setId(1L);
        Board board = Mockito.mock(Board.class);
        board.setId(2L);
        board.setCreatorUser(user);
        NotificationSetting notificationSetting = new NotificationSetting(1L, "Test notification");
        UserSetting userSetting1 = new UserSetting();
        userSetting1.setUser(user);
        userSetting1.setSetting(notificationSetting);
        UserSetting userSetting2 = new UserSetting();
        userSetting2.setUser(user);
        userSetting2.setSetting(notificationSetting);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(boardRepository.findById(2L)).thenReturn(Optional.of(board));
        assertThrows(NoSuchElementException.class,
                ()->notificationService.getAllinBoard(objectsIdsRequest));
    }

    @Test
    @DisplayName("Create in app notification successfully")
    public void createInAppNotification_NewNotification_Success() {
        NotificationRequest notificationRequest = new NotificationRequest();
        NotificationSetting notificationSetting = new NotificationSetting(5L, "Test");
        notificationRequest.setUser(new User());
        notificationRequest.setBoard(new Board());
        notificationRequest.setFromUser(new User());
        notificationRequest.setNotificationType(notificationSetting);
        UserSetting userSetting = new UserSetting();
        userSetting.setSetting(notificationSetting);
        Notification expectedNotification = Notification.createNewNotification(notificationRequest, userSetting);
        Notification actualNotification = notificationService.createInAppNotification(notificationRequest, userSetting);
        assertEquals(expectedNotification.getName(), actualNotification.getName());
    }

    @Test
    @DisplayName("create in app notifications with null request throws NullPointerException")
    public void createInAppNotification_NullNotificationRequest_ThrowsNullPointerException() {
        NotificationSetting notificationSetting = new NotificationSetting(5L, "Test");
        UserSetting userSetting = new UserSetting();
        userSetting.setSetting(notificationSetting);
        assertThrows(NullPointerException.class,
                ()->notificationService.createInAppNotification(null, userSetting));
    }

    @Test
    @DisplayName("create in app notifications with empty request throws NullPointerException")
    public void createInAppNotification_EmptyRequest_ThrowsNullPointerException() {
        NotificationRequest notificationRequest = new NotificationRequest();
        NotificationSetting notificationSetting = new NotificationSetting(5L, "Test");
        UserSetting userSetting = new UserSetting();
        userSetting.setSetting(notificationSetting);
        assertThrows(NullPointerException.class,
                ()->notificationService.createInAppNotification(notificationRequest, userSetting));
    }

    @Test
    @DisplayName("create in app notifications with no notification type in request throws NullPointerException")
    public void createInAppNotification_RequestWithNoNotificationType_ThrowsNullPointerException() {
        NotificationRequest notificationRequest = new NotificationRequest();
        NotificationSetting notificationSetting = new NotificationSetting(5L, "Test");
        notificationRequest.setBoard(new Board());
        notificationRequest.setUser(new User());
        notificationRequest.setFromUser(new User());
        UserSetting userSetting = new UserSetting();
        userSetting.setSetting(notificationSetting);
        assertThrows(NullPointerException.class,
                ()->notificationService.createInAppNotification(notificationRequest, userSetting));
    }

    @Test
    @DisplayName("create in app notifications with null user settings throws NullPointerException")
    public void createInAppNotification_NullUserSettings_ThrowsNullPointerException() {
        NotificationRequest notificationRequest = new NotificationRequest();
        NotificationSetting notificationSetting = new NotificationSetting(5L, "Test");
        notificationRequest.setUser(new User());
        notificationRequest.setBoard(new Board());
        notificationRequest.setFromUser(new User());
        notificationRequest.setNotificationType(notificationSetting);
        assertThrows(NullPointerException.class,
                ()->notificationService.createInAppNotification(notificationRequest, null));
    }

    @Test
    @DisplayName("create in app notifications with empty user settings throws NullPointerException")
    public void createInAppNotification_EmptyUserSettings_ThrowsNullPointerException() {
        NotificationRequest notificationRequest = new NotificationRequest();
        NotificationSetting notificationSetting = new NotificationSetting(5L, "Test");
        notificationRequest.setUser(new User());
        notificationRequest.setBoard(new Board());
        notificationRequest.setFromUser(new User());
        notificationRequest.setNotificationType(notificationSetting);
        UserSetting userSetting = new UserSetting();
        assertThrows(NullPointerException.class,
                ()->notificationService.createInAppNotification(notificationRequest, userSetting));
    }

    @Test
    @DisplayName("Delete notifications with valid parameters successfully")
    public void deleteNotifications_ValidParameters_Success() {
        Notification notification = new Notification();
        Board board = Mockito.mock(Board.class);
        board.setId(1L);
        User user = Mockito.mock(User.class);
        user.setId(2L);
        notification.setId(123L);
        notification.setUser(user);
        notification.setBoard(board);
        when(notificationRepository.findById(123L)).thenReturn(Optional.of(notification));
        List<Notification> result = notificationService.delete(List.of(123L));
        verify(notificationRepository).findById(123L);
        verify(notificationRepository).deleteAllById(List.of(123L));
        assertEquals(result, List.of());
    }

    @Test
    @DisplayName("Delete non existent notifications should throw NoSuchElementException")
    public void deleteNotifications_notificationNotFound_throwsNoSuchElementException() {
        when(notificationRepository.findById(123L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,
                ()->notificationService.delete(List.of(123L)));
    }
}
