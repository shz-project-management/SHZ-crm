package CRM.service;

import CRM.entity.Board;
import CRM.entity.NotificationSetting;
import CRM.entity.User;
import CRM.entity.UserSetting;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.requests.SettingUpdateRequest;
import CRM.repository.BoardRepository;
import CRM.repository.NotificationSettingRepository;
import CRM.repository.UserRepository;
import CRM.repository.UserSettingRepository;
import CRM.utils.enums.Notifications;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.security.auth.login.AccountNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SettingsServiceTest {
    @Mock
    private UserSettingRepository userSettingRepository;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private NotificationSettingRepository notificationSettingRepository;
    @InjectMocks
    private SettingsService settingsService;

    @Test
    @DisplayName("get all user settings in board success")
    public void getAllUserSettingsInBoard_ValidParameters_Success() throws AccountNotFoundException {
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
        List<UserSetting> expectedUserSettings = Arrays.asList(userSetting1, userSetting2);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(boardRepository.findById(2L)).thenReturn(Optional.of(board));
        when(board.doesBoardIncludeUser(user.getId())).thenReturn(true);
        when(userSettingRepository.getUserSettingsInBoard(1L, 2L)).thenReturn(expectedUserSettings);
        List<UserSetting> actualUserSettings = settingsService.getAllUserSettingsInBoard(objectsIdsRequest);
        assertEquals(expectedUserSettings, actualUserSettings);
    }

    @Test
    @DisplayName("get all user settings in board with non exists user throws AccountNotFoundException")
    public void getAllUserSettingsInBoard_UserDoesNotExist_ThrowsAccountNotFoundException() {
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        Board board = Mockito.mock(Board.class);
        board.setId(2L);
        objectsIdsRequest.setUserId(1L);
        objectsIdsRequest.setBoardId(2L);
        assertThrows(AccountNotFoundException.class,
                ()->settingsService.getAllUserSettingsInBoard(objectsIdsRequest));
    }

    @Test
    @DisplayName("get all user settings in board with non exists board throws AccountNotFoundException")
    public void getAllUserSettingsInBoard_BoardDoesNotExist_ThrowsAccountNotFoundException() {
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        User user = Mockito.mock(User.class);
        user.setId(1L);
        objectsIdsRequest.setUserId(1L);
        objectsIdsRequest.setBoardId(2L);
        assertThrows(AccountNotFoundException.class,
                ()->settingsService.getAllUserSettingsInBoard(objectsIdsRequest));
    }

    @Test
    @DisplayName("get all user settings in board with user does not belong to board throws NoSuchElementException")
    public void getAllUserSettingsInBoard_UserDoesNotBelongToBoard_ThrowsNoSuchElementException() {
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
                ()->settingsService.getAllUserSettingsInBoard(objectsIdsRequest));
    }

    @Test
    @DisplayName("get notification settings from DB valid parameters success")
    public void getNotificationSettingFromDB_ValidParameters_Success() {
        String notificationName = Notifications.COMMENT_ADDED.toString();
        NotificationSetting expectedNotificationSetting = new NotificationSetting();
        expectedNotificationSetting.setName(notificationName);
        when(notificationSettingRepository.findByName(notificationName)).thenReturn(Optional.of(expectedNotificationSetting));
        NotificationSetting actualNotificationSetting = notificationSettingRepository.findByName(notificationName).get();
        assertEquals(expectedNotificationSetting, actualNotificationSetting);
    }

    @Test
    @DisplayName("get notification settings from DB with no settings return null")
    public void getNotificationSettingFromDB_NoSettings_ReturnNull() {
        String notificationName = Notifications.COMMENT_ADDED.toString();
        when(notificationSettingRepository.findByName(notificationName)).thenReturn(Optional.empty());
        NotificationSetting notificationSetting = settingsService.getNotificationSettingFromDB(notificationName);
        assertNull(notificationSetting);
    }

    @Test
    @DisplayName("change user settings in board with valid parameters success")
    public void changeUserSettingsInBoard_ValidParameters_Success() {
        SettingUpdateRequest settingUpdateRequest = new SettingUpdateRequest();
        settingUpdateRequest.setUserSettingId(1L);
        settingUpdateRequest.setInApp(true);
        settingUpdateRequest.setBoardId(2L);
        UserSetting userSetting = new UserSetting();
        userSetting.setId(1L);
        userSetting.setInApp(false);
        User user = new User();
        user.setId(3L);
        userSetting.setUser(user);
        Board board = new Board();
        board.setId(2L);
        userSetting.setSetting(new NotificationSetting());
        user.setUsersSettings(Set.of(userSetting));
        when(userSettingRepository.findById(1L)).thenReturn(Optional.of(userSetting));
        settingsService.changeUserSettingsInBoard(settingUpdateRequest);
        assertEquals(1, user.getUsersSettings().size());
    }
}
