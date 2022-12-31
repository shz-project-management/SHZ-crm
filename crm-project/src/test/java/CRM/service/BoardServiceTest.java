package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.BoardRequest;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.requests.RegisterUserRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.repository.BoardRepository;
import CRM.repository.NotificationRepository;
import CRM.repository.NotificationSettingRepository;
import CRM.repository.UserRepository;
import CRM.utils.enums.Permission;
import CRM.utils.enums.UpdateField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.security.auth.login.AccountNotFoundException;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private NotificationSettingRepository notificationSettingRepository;
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private BoardService boardService;


    @Test
    @DisplayName("Test that create function correctly saves board to repository and returns it")
    public void create_ValidInput_Success() throws AccountNotFoundException {
        MockitoAnnotations.initMocks(this);

        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        expectedUser.setId(1L);
        BoardRequest boardRequest = new BoardRequest(1L, "name", "desc");
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setStatuses(Status.defaultStatuses());
        expectedBoard.setTypes(Type.defaultTypes());
        Set<UserPermission> userPermissionSet = new HashSet<>();
        userPermissionSet.add(UserPermission.newUserPermission(expectedBoard.getCreatorUser(), Permission.ADMIN));
        expectedBoard.setUsersPermissions(userPermissionSet);

        given(userRepository.findById(1L)).willReturn(Optional.of(expectedUser));
        given(notificationSettingRepository.findAll()).willReturn(Collections.emptyList());
        doNothing().when(entityManager).close();
        given(boardRepository.save(any(Board.class))).willAnswer(invocation -> {
            Board board = invocation.getArgument(0);
            board.setStatuses(Status.defaultStatuses());
            board.setTypes(Type.defaultTypes());
            board.getUsersPermissions().add(UserPermission.newUserPermission(board.getCreatorUser(), Permission.ADMIN));
            return board;
        });

        Board actualBoard = boardService.create(boardRequest);

        assertThat(actualBoard)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedBoard);
    }

    @Test
    @DisplayName("Test that create function correctly handles null board input")
    public void create_NullBoard_ThrowsNullPointerException() {
        BoardRequest board = null;
        assertThrows(NullPointerException.class, () -> boardService.create(board));
    }

    @Test
    @DisplayName("Test that create function correctly handles board with null creator user")
    public void create_NullCreatorUser_ThrowsAccountNotFoundException() {
        BoardRequest boardRequest = new BoardRequest(null, "name", "desc");
        assertThrows(AccountNotFoundException.class, () -> boardService.create(boardRequest));
    }

    @Test
    @DisplayName("Test that delete function correctly deletes board from repository and returns true")
    public void delete_DeletesValidBoard_Success() {
        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);
        given(boardRepository.findById(expectedBoard.getId())).willReturn(Optional.of(expectedBoard));
        given(notificationRepository.deleteByBoard(expectedBoard)).willReturn(1L);
        assertTrue(boardService.delete(expectedBoard.getId()));
    }


    @Test
    @DisplayName("Test that delete function throws AccountNotFoundException if board does not exist in repository")
    public void delete_NonExistentBoard_ThrowsNoSuchElementException() {
        given(boardRepository.findById(123L)).willReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> boardService.delete(123L));
    }

    @Test
    @DisplayName("Test that get function correctly retrieves board from repository")
    public void get_ValidInput_Success() {
        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);
        given(boardRepository.findById(expectedBoard.getId())).willReturn(Optional.of(expectedBoard));
        assertEquals(expectedBoard, boardService.get(expectedBoard.getId()));
    }

    @Test
    @DisplayName("Test that get function throws AccountNotFoundException if board does not exist in repository")
    public void get_NonExistentBoard_ThrowsNoSuchElementException() {
        given(boardRepository.findById(123L)).willReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> boardService.get(123L));
    }

    @Test
    @DisplayName("Test that getAll function correctly retrieves all boards from repository")
    public void getAll_RetrievesAllBoards_Success() {
        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);
        List<Board> boards = Collections.singletonList(expectedBoard);
        given(boardRepository.findAll()).willReturn(boards);
        assertEquals(boards, boardService.getAll());
    }

    @Test
    @DisplayName("Test update board with valid input")
    public void update_ValidInput_Success() throws NoSuchFieldException {
        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        UpdateObjectRequest boardRequest = new UpdateObjectRequest();
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardUserIds(1L, 1L);
        boardRequest.setObjectsIdsRequest(objIds);
        boardRequest.setFieldName(UpdateField.NAME);
        boardRequest.setContent("Test new board name");
        given(boardRepository.findById(1L)).willReturn(Optional.of(expectedBoard));
        given(boardRepository.save(expectedBoard)).willReturn(expectedBoard);
        assertNotNull(boardService.updateBoard(boardRequest));
    }

    @Test
    @DisplayName("Test update board with invalid board ID")
    public void update_InvalidBoardId_ThrowsNoSuchElementException() {
        UpdateObjectRequest boardRequest = new UpdateObjectRequest();
        boardRequest.setFieldName(UpdateField.NAME);
        boardRequest.setContent("Test new board name");
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardUserIds(-1L, 1L);
        boardRequest.setObjectsIdsRequest(objIds);
        assertThrows(NoSuchElementException.class, () -> boardService.updateBoard(boardRequest));
    }

    @Test
    @DisplayName("Test update board with null name")
    public void update_NullName_ThrowsNoSuchElementException() {
        UpdateObjectRequest boardRequest = new UpdateObjectRequest();
        boardRequest.setFieldName(UpdateField.NAME);
        boardRequest.setContent(null);
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardUserIds(-1L, 1L);
        boardRequest.setObjectsIdsRequest(objIds);
        assertThrows(NoSuchElementException.class, () -> boardService.updateBoard(boardRequest));
    }
}