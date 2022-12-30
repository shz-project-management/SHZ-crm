package CRM.controller.facades;

import CRM.entity.Board;
import CRM.entity.NotificationSetting;
import CRM.entity.User;
import CRM.entity.UserPermission;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.requests.RegisterUserRequest;
import CRM.service.BoardService;
import CRM.service.SettingsService;
import CRM.service.UserService;
import CRM.utils.NotificationSender;
import CRM.utils.enums.ExceptionMessage;
import CRM.utils.enums.Notifications;
import CRM.utils.enums.Permission;
import com.google.api.client.http.HttpStatusCodes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.NoPermissionException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class UserFacadeTest {

    @Mock
    private UserService userService;
    @Mock
    private BoardService boardService;
    @Mock
    private SettingsService settingsService;
    @Mock
    private NotificationSender notificationSender;

    @InjectMocks
    private UserFacade userFacade;

    @Test
    @DisplayName("Test get method with valid input")
    void get_WithValidInput_Success() throws AccountNotFoundException {
        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Ziv Hausler", "ziv123456", "test@gmail.com");
        User user = User.newUser(correctRegisterUserRequest);
        user.setId(1L);

        given(userService.get(user.getId())).willReturn(user);

        assertEquals(HttpStatusCodes.STATUS_CODE_OK, userFacade.get(user.getId()).getStatusCode());
    }

    @Test
    @DisplayName("Test get method with invalid id throws illegal argument")
    void get_UserWithInvalidId_ThrowsIllegalArgumentException() throws AccountNotFoundException {
        given(userService.get(1L)).willThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> userFacade.get(1L));
    }

    @Test
    @DisplayName("Test get method when user is not found throws account not found exception")
    void get_UserAccountNotFoundInDB_ThrowsAccountNotFoundException() throws AccountNotFoundException {
        long id = 1L;

        given(userService.get(id)).willThrow(new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString()));

        assertThrows(AccountNotFoundException.class, () -> userFacade.get(id));
    }

    @Test
    @DisplayName("Test get method when null pointer exception is thrown throws null pointer exception")
    void get_UserIdNullInput_ThrowsNullPointerException() {
        Long id = null;

        assertThrows(NullPointerException.class, () -> userFacade.get(id));
    }

    @Test
    @DisplayName("Test getAll method with valid input")
    void getAll_UsersWithValidInput_Success() {
        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Ziv Hausler", "ziv123456", "test@gmail.com");
        List<User> users = List.of(User.newUser(correctRegisterUserRequest));

        given(userService.getAll()).willReturn(users);

        assertEquals(HttpStatusCodes.STATUS_CODE_OK, userFacade.getAll().getStatusCode());
    }

    @Test
    @DisplayName("Test getAllInBoard method with valid input")
    void getAllInBoard_WithValidInput_Success() throws AccountNotFoundException {
        long boardId = 1L;
        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Ziv Hausler", "ziv123456", "test@gmail.com");
        List<User> users = List.of(User.newUser(correctRegisterUserRequest));

        given(userService.getAllInBoard(boardId)).willReturn(users);

        assertEquals(HttpStatusCodes.STATUS_CODE_OK, userFacade.getAllInBoard(boardId).getStatusCode());
    }

    @Test
    @DisplayName("Test getAllInBoard method with invalid id throws illegal argument")
    void getAllInBoard_InvalidUserId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> userFacade.getAllInBoard(-1L));
    }

    @Test
    @DisplayName("Test getAllInBoard method when board is not found")
    void getAllInBoard_BoardNotFoundInDB_ThrowsNoSuchElementException() throws AccountNotFoundException {
        long boardId = 1L;

        given(userService.getAllInBoard(boardId)).willThrow(new NoSuchElementException(ExceptionMessage.BOARD_DOES_NOT_EXISTS.toString()));

        assertThrows(NoSuchElementException.class, () -> userFacade.getAllInBoard(boardId));
    }

    @Test
    @DisplayName("Test getAllInBoard method when null pointer exception is thrown")
    void getAllInBoard_NullBoarId_ThrowsNullPointerExcption() {
        Long boardId = null;

        assertThrows(NullPointerException.class, () -> userFacade.getAllInBoard(boardId));
    }

    @Test
    @DisplayName("Test delete with valid ID value returns No Content status")
    public void delete_ValidInput_Success() throws AccountNotFoundException {
        given(userService.delete(1L)).willReturn(true);

        assertEquals(HttpStatusCodes.STATUS_CODE_NO_CONTENT, userFacade.delete(1L).getStatusCode());
    }

    @Test
    @DisplayName("Test invalid ID value returns BAD REQUEST status")
    public void delete_InvalidInput_BadRequestResponse() {
        assertEquals(HttpStatusCodes.STATUS_CODE_BAD_REQUEST, userFacade.delete(-1L).getStatusCode());
    }

    @Test
    @DisplayName("Test null ID value returns BAD REQUEST status")
    public void delete_NullInput_ServerErrorResponse() {
        assertEquals(HttpStatusCodes.STATUS_CODE_SERVER_ERROR, userFacade.delete(null).getStatusCode());
    }

//    @Test
//    @DisplayName("Test non-existent ID value returns BAD REQUEST status")
//    public void delete_ServiceThrowsAccountNotFound_BadRequestResponse() throws AccountNotFoundException {
//        given(userService.delete(100L)).willThrow(new AccountNotFoundException());
//
//        assertEquals(HttpStatusCodes.STATUS_CODE_BAD_REQUEST, userFacade.delete(100L).getStatusCode());
//    }

    @Test
    @DisplayName("get all user permissions in board - success")
    public void getAllUserPermissionsInBoard_ValidInput_Success() {
        long boardId = 1L;
        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Ziv Hausler", "ziv123456", "test@gmail.com");
        Set<UserPermission> userPermissions = new HashSet<>();
        userPermissions.add(UserPermission.newUserPermission(User.newUser(correctRegisterUserRequest), Permission.ADMIN));

        given(userService.getAllUserPermissionsInBoard(boardId)).willReturn(userPermissions);

        assertEquals(HttpStatusCodes.STATUS_CODE_OK, userFacade.getAllUserPermissionsInBoard(boardId).getStatusCode());
    }

    @Test
    @DisplayName("get all user permissions in board - invalid board id throws illeagl argument")
    public void getAllUserPermissionsInBoard_InvalidBoardId_ThrowsIllegalArgumentException() {
        long boardId = -1L;

        assertThrows(IllegalArgumentException.class, () -> userFacade.getAllUserPermissionsInBoard(boardId));
    }

    @Test
    @DisplayName("get all user permissions in board with valid board that not found in db, throws no such element")
    public void getAllUserPermissionsInBoard_BoardNotFound_ThrowsNoSuchElementException() {
        long boardId = 1L;

        given(userService.getAllUserPermissionsInBoard(boardId)).willThrow(new NoSuchElementException());

        assertThrows(NoSuchElementException.class, () -> userFacade.getAllUserPermissionsInBoard(boardId));
    }

    @Test
    @DisplayName("get all user permissions in board with null id - null pointer exception")
    public void getAllUserPermissionsInBoard_NullBoardId_ThrowsNullPointerException() {
        Long boardId = null;

        assertThrows(NullPointerException.class, () -> userFacade.getAllUserPermissionsInBoard(boardId));
    }

    @Test
    @DisplayName("Get all boards of user - success")
    public void getAllBoardsOfUser_ValidInput_Success() throws NoPermissionException, AccountNotFoundException {
        long userId = 1L;
        Map<String, List<Board>> mapBoards = new HashMap<>();
        List<Board> boardList = new ArrayList<>();
        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Ziv Hausler", "ziv123456", "test@gmail.com");
        boardList.add(Board.createBoard(User.newUser(correctRegisterUserRequest), "name", "desc"));
        mapBoards.put("myBoards", boardList);

        given(userService.getAllBoardsOfUser(userId)).willReturn(mapBoards);

        assertEquals(HttpStatusCodes.STATUS_CODE_OK, userFacade.getAllBoardsOfUser(userId).getStatusCode());
    }

    @Test
    @DisplayName("Get all boards of user - invalid input throws illegal argument")
    public void getAllBoardsOfUser_InvalidUserIDInput_ThrowsIllegalArgumentException() {
        long userId = -1L;

        assertThrows(IllegalArgumentException.class, () -> userFacade.getAllBoardsOfUser(userId));
    }

    @Test
    @DisplayName("Get all boards of user - null pointer exception")
    public void getAllBoardsOfUser_NullUserId_ThrowsNullPointerException() {
        Long userId = null;

        assertThrows(NullPointerException.class, () -> userFacade.getAllBoardsOfUser(userId));
    }

    @Test
    @DisplayName("update user to board success using user id")
    public void updateUserToBoard_ValidInputWithUserId_Success() throws AccountNotFoundException {
        ObjectsIdsRequest objectsIdsRequest = mock(ObjectsIdsRequest.class);
        given(objectsIdsRequest.getBoardId()).willReturn(1L);
        given(objectsIdsRequest.getUserId()).willReturn(2L);
        given(objectsIdsRequest.getPermissionId()).willReturn(3L);

        Set<UserPermission> userPermissions = new HashSet<>();
        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Ziv Hausler", "ziv123456", "test@gmail.com");
        userPermissions.add(UserPermission.newUserPermission(User.newUser(correctRegisterUserRequest), Permission.ADMIN));

        given(userService.updateUserToBoard(objectsIdsRequest)).willReturn(userPermissions);

        NotificationSetting notificationSetting = mock(NotificationSetting.class);
        given(settingsService.getNotificationSettingFromDB(Notifications.USER_ADDED.name)).willReturn(notificationSetting);

        Board board = mock(Board.class);
        given(boardService.get(objectsIdsRequest.getBoardId())).willReturn(board);

        Set<User> users = new HashSet<>();
        User user = mock(User.class);
        users.add(user);

        given(board.getBoardUsersSet()).willReturn(users);
        assertEquals(HttpStatusCodes.STATUS_CODE_OK, userFacade.updateUserToBoard(objectsIdsRequest).getStatusCode());
    }

    @Test
    @DisplayName("update user to board success using user email")
    public void updateUserToBoard_ValidInputWithEmail_Success() throws AccountNotFoundException {
        ObjectsIdsRequest objectsIdsRequest = mock(ObjectsIdsRequest.class);
        given(objectsIdsRequest.getBoardId()).willReturn(1L);
        given(objectsIdsRequest.getUserId()).willReturn(2L);
        given(objectsIdsRequest.getPermissionId()).willReturn(3L);
        given(objectsIdsRequest.getEmail()).willReturn("shai@gmail.com");

        Set<UserPermission> userPermissions = new HashSet<>();
        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Ziv Hausler", "ziv123456", "test@gmail.com");
        userPermissions.add(UserPermission.newUserPermission(User.newUser(correctRegisterUserRequest), Permission.ADMIN));

        given(userService.updateUserToBoard(objectsIdsRequest)).willReturn(userPermissions);

        NotificationSetting notificationSetting = mock(NotificationSetting.class);
        given(settingsService.getNotificationSettingFromDB(Notifications.USER_ADDED.name)).willReturn(notificationSetting);

        Board board = mock(Board.class);
        given(boardService.get(objectsIdsRequest.getBoardId())).willReturn(board);

        Set<User> users = new HashSet<>();
        User user = mock(User.class);
        users.add(user);

        given(userService.get(objectsIdsRequest.getEmail())).willReturn(user);
        given(board.getBoardUsersSet()).willReturn(users);

        assertEquals(HttpStatusCodes.STATUS_CODE_OK, userFacade.updateUserToBoard(objectsIdsRequest).getStatusCode());
    }

    @Test
    @DisplayName("update user to board failed using invalid board id throws illegal argument")
    public void updateUserToBoard_InvalidBoardIdInput_ThrowsIllegalArgumentException() {
        ObjectsIdsRequest objectsIdsRequest = mock(ObjectsIdsRequest.class);
        given(objectsIdsRequest.getBoardId()).willReturn(-1L);
        given(objectsIdsRequest.getUserId()).willReturn(2L);
        given(objectsIdsRequest.getPermissionId()).willReturn(3L);

        assertThrows(IllegalArgumentException.class, () -> userFacade.updateUserToBoard(objectsIdsRequest));
    }

    @Test
    @DisplayName("update user to board failed using null board id throws null pointer")
    public void updateUserToBoard_NullBoardIdInput_ThrowsNullPointerException() {
        ObjectsIdsRequest objectsIdsRequest = mock(ObjectsIdsRequest.class);
        given(objectsIdsRequest.getBoardId()).willReturn(null);
        given(objectsIdsRequest.getUserId()).willReturn(2L);
        given(objectsIdsRequest.getPermissionId()).willReturn(3L);

        assertThrows(NullPointerException.class, () -> userFacade.updateUserToBoard(objectsIdsRequest));
    }
}
