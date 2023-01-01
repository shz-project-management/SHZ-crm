package CRM.controller.facades;

import CRM.entity.*;
import CRM.entity.requests.*;
import CRM.service.BoardService;
import CRM.utils.enums.Permission;
import CRM.utils.enums.UpdateField;
import com.google.api.client.http.HttpStatusCodes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.NoPermissionException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class BoardFacadeTest {

    @Mock
    private BoardService boardService;

    @InjectMocks
    private BoardFacade boardFacade;

    @Test
    @DisplayName("Test the case where the boardRequest is valid and the board is successfully created")
    public void create_ValidInput_Success() throws AccountNotFoundException {
        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Ziv Hausler", "ziv123456", "test@gmail.com");
        User user = User.newUser(correctRegisterUserRequest);
        user.setId(1L);
        BoardRequest correctBoardRequest = new BoardRequest(1L, "board", "nice");
        correctBoardRequest.setCreatorUserId(1L);
        Board boardWithID = Board.createBoard(user, "board", "nice");
        boardWithID.setId(1L);

        given(boardService.create(correctBoardRequest)).willReturn(boardWithID);

        assertEquals(HttpStatusCodes.STATUS_CODE_CREATED, boardFacade.create(correctBoardRequest).getStatusCode());
    }

    @Test
    @DisplayName("Test the case where the boardRequest has a null name")
    public void create_NullName_ThrowsNullPointerException() {
        BoardRequest incorrectBoardRequest = new BoardRequest(1L, null, "nice");

        assertThrows(NullPointerException.class, () -> boardFacade.create(incorrectBoardRequest));
    }

    @Test
    @DisplayName("Test the case where the boardRequest has a null creator user ID")
    public void create_NullCreatorUser_ThrowsNullPointerException() {
        BoardRequest incorrectBoardRequest = new BoardRequest(null, "board", "nice");

        assertThrows(NullPointerException.class, () -> boardFacade.create(incorrectBoardRequest));
    }

    @Test
    @DisplayName("Test the case where the creator user ID in the boardRequest does not exist")
    public void create_UserNotFoundInDB_ThrowsAccountNotFoundException() throws AccountNotFoundException {
        BoardRequest incorrectBoardRequest = new BoardRequest(100L, "board", "nice");
        incorrectBoardRequest.setCreatorUserId(1L);

        given(boardService.create(incorrectBoardRequest)).willThrow(AccountNotFoundException.class);

        assertThrows(AccountNotFoundException.class, () -> boardFacade.create(incorrectBoardRequest));
    }

    @Test
    @DisplayName("Test delete with valid id")
    public void delete_ValidInput_Success() {
        assertEquals(HttpStatusCodes.STATUS_CODE_NO_CONTENT, boardFacade.delete(1L).getStatusCode());
    }

    @Test
    @DisplayName("Test delete with invalid id")
    public void delete_InvalidBoardId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> boardFacade.delete(-2L));
    }

    @Test
    @DisplayName("Test delete with null id")
    public void delete_NullBoardId_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> boardFacade.delete(null));
    }

    @Test
    @DisplayName("Test delete with non existent board id in database")
    public void delete_BoardIdNotExistsInDB_NoSuchElementException() {
        given(boardService.delete(1L)).willThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> boardFacade.delete(1L));
    }

    @Test
    @DisplayName("Test get with valid id")
    public void get_ValidInput_Success() throws NoPermissionException {
        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Ziv Hausler", "ziv123456", "test@gmail.com");
        User user = User.newUser(correctRegisterUserRequest);
        user.setId(1L);
        Board board = Board.createBoard(user, "board", "nice");
        board.setId(1L);
        Status status = Status.createStatusAttribute("name", "desc");
        Type type = Type.createTypeAttribute("name", "desc");

        board.getStatuses().add(status);
        board.getTypes().add(type);
        board.getSections().add(Section.createSection(new AttributeRequest()));

        board.getUsersPermissions().add(UserPermission.newUserPermission(user, Permission.ADMIN));
        given(boardService.get(1L)).willReturn(board);

        assertEquals(HttpStatusCodes.STATUS_CODE_OK, boardFacade.get(board.getId(), user.getId()).getStatusCode());
    }

    @Test
    @DisplayName("Test get with non-existent id")
    public void get_BoardIdNotExistsInDB_ThrowsNoSuchElementException() {
        given(boardService.get(1L)).willThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> boardFacade.get(1L, 1L));
    }

    @Test
    @DisplayName("Test get with null user id")
    public void get_NullUserId_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> boardFacade.get(1L, null));
    }

    @Test
    @DisplayName("Test get with invalid id")
    public void get_InvalidUserId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> boardFacade.get(-2L, 1L));
    }

    @Test
    @DisplayName("Test getAll boards in db")
    public void getAll_Success() {
        List<Board> expectedBoards = new ArrayList<>();

        given(boardService.getAll()).willReturn(expectedBoards);

        assertEquals(HttpStatusCodes.STATUS_CODE_OK, boardFacade.getAll().getStatusCode());
    }

    @Test
    @DisplayName("Test update board with valid input")
    public void update_ValidInput_Success() throws NoSuchFieldException {
        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Ziv Hausler", "ziv123456", "test@gmail.com");
        User user = User.newUser(correctRegisterUserRequest);
        user.setId(1L);
        Board board = Board.createBoard(user, "board", "nice");
        board.setId(1L);
        UpdateObjectRequest boardRequest = new UpdateObjectRequest();
        boardRequest.setFieldName(UpdateField.NAME);
        boardRequest.setContent("Test");
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardUserIds(1L, 1L);
        boardRequest.setObjectsIdsRequest(objIds);

        given(boardService.updateBoard(boardRequest)).willReturn(board);

        assertEquals(HttpStatusCodes.STATUS_CODE_OK, boardFacade.updateBoard(boardRequest).getStatusCode());
    }

    @Test
    @DisplayName("Test update board with invalid board ID")
    public void update_InvalidBoardId_ThrowsIllegalArgumentException() {
        UpdateObjectRequest boardRequest = new UpdateObjectRequest();
        boardRequest.setFieldName(UpdateField.NAME);
        boardRequest.setContent("Test new board name");
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardUserIds(-2L, 1L);
        boardRequest.setObjectsIdsRequest(objIds);

        assertThrows(IllegalArgumentException.class, () -> boardFacade.updateBoard(boardRequest));
    }

    @Test
    @DisplayName("Test update board with non-existent board")
    public void update_BoardIdNotFoundInDB_ThrowsNoSuchElementException() throws NoSuchFieldException {
        UpdateObjectRequest boardRequest = new UpdateObjectRequest();
        boardRequest.setFieldName(UpdateField.NAME);
        boardRequest.setContent("Test new board name");
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardUserIds(123L, 1L);
        boardRequest.setObjectsIdsRequest(objIds);

        given(boardService.updateBoard(boardRequest)).willThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> boardFacade.updateBoard(boardRequest));
    }

    @Test
    @DisplayName("Test update board with non-existent field to update")
    public void update_InvalidFieldUpdateInput_ThrowsNoSuchFieldException() throws NoSuchFieldException {
        UpdateObjectRequest boardRequest = new UpdateObjectRequest();
        boardRequest.setFieldName(UpdateField.NAME);
        boardRequest.setContent("Teste");
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardUserIds(123L, 1L);
        boardRequest.setObjectsIdsRequest(objIds);

        given(boardService.updateBoard(boardRequest)).willThrow(NoSuchFieldException.class);

        assertThrows(NoSuchFieldException.class, () -> boardFacade.updateBoard(boardRequest));
    }


    @Test
    @DisplayName("Test update board with null input")
    public void update_NullInput_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> boardFacade.updateBoard(null));
    }
}