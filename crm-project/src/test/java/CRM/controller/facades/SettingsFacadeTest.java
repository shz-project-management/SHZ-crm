package CRM.controller.facades;

import CRM.entity.DTO.SettingsDTO;
import CRM.entity.UserSetting;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.requests.SettingUpdateRequest;
import CRM.entity.response.Response;
import CRM.service.SettingsService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SettingsFacadeTest {

    @Mock
    private SettingsService settingsService;

    @InjectMocks
    private SettingsFacade settingsFacade;

    //getAllUserSettingsInBoard tests

    @Test
    @DisplayName("Getting all user settings in board success")
    public void getAllUserSettingsInBoard_ValidParameters_Success() throws AccountNotFoundException {
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        objectsIdsRequest.setUserId(1L);
        objectsIdsRequest.setBoardId(2L);
        List<UserSetting> userSettingList = Arrays.asList(new UserSetting(), new UserSetting());
        when(settingsService.getAllUserSettingsInBoard(objectsIdsRequest)).thenReturn(userSettingList);
        Response<List<SettingsDTO>> response = settingsFacade.getAllUserSettingsInBoard(objectsIdsRequest);
        assertNotNull(response.getData());
        assertTrue(response.getData() != null);
        assertEquals(SettingsDTO.createUserSettingsList(userSettingList).toString(), response.getData().toString());
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.getStatusCode());
        assertEquals(SuccessMessage.FOUND.toString(), response.getMessage());
    }

    @Test
    @DisplayName("Getting all user settings in board with null request throws NullPointerException")
    public void getAllUserSettingsInBoard_NullRequest_ServerErrorResponse() {
        assertThrows(NullPointerException.class,
                ()->settingsFacade.getAllUserSettingsInBoard(null));
    }

    @Test
    @DisplayName("Getting all user settings in board with null board throws NullPointerException")
    public void getAllUserSettingsInBoard_NullBoard_ServerErrorResponse() {
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        objectsIdsRequest.setUserId(1L);
        assertThrows(NullPointerException.class,
                ()->settingsFacade.getAllUserSettingsInBoard(objectsIdsRequest));
    }

    @Test
    @DisplayName("Getting all user settings in board with null user throws NullPointerException")
    public void getAllUserSettingsInBoard_NullUser_ServerErrorResponse() {
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        objectsIdsRequest.setBoardId(1L);
        assertThrows(NullPointerException.class,
                ()->settingsFacade.getAllUserSettingsInBoard(objectsIdsRequest));
    }

    @Test
    @DisplayName("Getting all user settings in board with illegal user id throws IllegalArgumentException")
    public void getAllUserSettingsInBoard_IllegalUserId_ServerErrorResponse() {
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        objectsIdsRequest.setUserId(-1L);
        objectsIdsRequest.setBoardId(2L);
        assertThrows(IllegalArgumentException.class,
                ()->settingsFacade.getAllUserSettingsInBoard(objectsIdsRequest));
    }

    @Test
    @DisplayName("Getting all user settings in board with illegal board id throws IllegalArgumentException")
    public void getAllUserSettingsInBoard_IllegalBoardId_ServerErrorResponse() {
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        objectsIdsRequest.setUserId(1L);
        objectsIdsRequest.setBoardId(-2L);
        assertThrows(IllegalArgumentException.class,
                ()->settingsFacade.getAllUserSettingsInBoard(objectsIdsRequest));
    }

    @Test
    @DisplayName("Getting all user settings in board for user with no settings in the board returns empty list")
    public void getAllUserSettingsInBoard_UserDoesNotBelongToBoard_ReturnsEmptyList() throws AccountNotFoundException {
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        objectsIdsRequest.setUserId(1L);
        objectsIdsRequest.setBoardId(2L);
        Response<List<SettingsDTO>> response = settingsFacade.getAllUserSettingsInBoard(objectsIdsRequest);
        assertEquals(response.getData(), new ArrayList<>());
    }

    //changeUserSettingsInBoard tests

    @Test
    @DisplayName("Change user settings in board success")
    public void changeUserSettingsInBoard_ValidParameters_Success() {
        SettingUpdateRequest settingUpdateRequest = new SettingUpdateRequest();
        settingUpdateRequest.setUserSettingId(1L);
        settingUpdateRequest.setBoardId(2L);
        settingUpdateRequest.setInApp(false);
        settingUpdateRequest.setInEmail(false);
        List<UserSetting> settingsDTOList = Arrays.asList(new UserSetting(), new UserSetting());
        when(settingsService.changeUserSettingsInBoard(settingUpdateRequest)).thenReturn(settingsDTOList);
        Response<List<SettingsDTO>> response = settingsFacade.changeUserSettingsInBoard(settingUpdateRequest);
        assertNotNull(response.getData());
        assertTrue(response.getData() instanceof List);
        assertEquals(SettingsDTO.createUserSettingsList(settingsDTOList).toString(), response.getData().toString());
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.getStatusCode());
        assertEquals(SuccessMessage.FOUND.toString(), response.getMessage());
    }

    @Test
    @DisplayName("Setting user settings in board with illegal user id throws IllegalArgumentException")
    public void changeUserSettingsInBoard_IllegalUserId_ServerErrorResponse() {
        SettingUpdateRequest settingUpdateRequest = new SettingUpdateRequest();
        settingUpdateRequest.setUserSettingId(-1L);
        settingUpdateRequest.setBoardId(2L);
        assertThrows(IllegalArgumentException.class,
                ()->settingsFacade.changeUserSettingsInBoard(settingUpdateRequest));
    }

    @Test
    @DisplayName("Setting user settings in board with illegal board id throws IllegalArgumentException")
    public void changeUserSettingsInBoard_IllegalBoardId_ServerErrorResponse() {
        SettingUpdateRequest settingUpdateRequest = new SettingUpdateRequest();
        settingUpdateRequest.setUserSettingId(-1L);
        settingUpdateRequest.setBoardId(-2L);
        assertThrows(IllegalArgumentException.class,
                ()->settingsFacade.changeUserSettingsInBoard(settingUpdateRequest));
    }

    @Test
    @DisplayName("Changing user settings in board with null request throws NullPointerException")
    public void changeUserSettingsInBoard_NullRequest_ServerErrorResponse() {
        assertThrows(NullPointerException.class,
                ()->settingsFacade.changeUserSettingsInBoard(null));
    }

    @Test
    @DisplayName("Changing user settings in board with null board throws NullPointerException")
    public void changeUserSettingsInBoard_NullBoard_ServerErrorResponse() {
        SettingUpdateRequest settingUpdateRequest = new SettingUpdateRequest();
        settingUpdateRequest.setUserSettingId(1L);
        assertThrows(NullPointerException.class,
                ()->settingsFacade.changeUserSettingsInBoard(settingUpdateRequest));
    }

    @Test
    @DisplayName("Changing user settings in board with null user throws NullPointerException")
    public void changeUserSettingsInBoard_NullUser_ServerErrorResponse() {
        SettingUpdateRequest settingUpdateRequest = new SettingUpdateRequest();
        settingUpdateRequest.setBoardId(1L);
        assertThrows(NullPointerException.class,
                ()->settingsFacade.changeUserSettingsInBoard(settingUpdateRequest));
    }
}
