package CRM.service;

import CRM.entity.Board;
import CRM.entity.User;
import CRM.entity.UserInBoard;
import CRM.entity.requests.BoardRequest;
import CRM.entity.requests.RegisterUserRequest;
import CRM.repository.BoardRepository;
import CRM.repository.UserInBoardRepository;
import CRM.repository.UserRepository;
import CRM.utils.enums.Permission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import javax.security.auth.login.AccountNotFoundException;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private UserInBoardRepository userInBoardRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BoardService boardService;

    private RegisterUserRequest correctRegisterUserRequest;
    private User expectedUser;
    private Board board;


    @BeforeEach
    void setUp() {
        correctRegisterUserRequest = new RegisterUserRequest("Shai", "Levi", "shai123456", "shai@gmail.com");
        expectedUser = User.newUser(correctRegisterUserRequest);
        board = Board.createBoard(expectedUser, "board", "nice");
    }


    @Test
    @DisplayName("Test that create function correctly saves board to repository and returns it")
    public void testCreateSavesBoard() {
        given(boardRepository.save(board)).willReturn(board);
        assertEquals(board, boardService.create(board));
    }

    @Test
    @DisplayName("Test that create function correctly handles null board input")
    public void testCreateWithNullBoardThrowsException() {
        Board board = null;
        assertThrows(NullPointerException.class, () -> boardService.create(board));
    }

    @Test
    @DisplayName("Test that create function correctly handles board with null creator user")
    public void testCreateWithNullCreatorUserThrowsException() {
        board.setCreatorUser(null);
        assertThrows(NullPointerException.class, () -> boardService.create(board));
    }


    @Test
    @DisplayName("Test that delete function correctly deletes board from repository and returns true")
    public void testDeleteDeletesBoard() throws AccountNotFoundException {
        Long boardId = board.getId();
        given(boardRepository.findById(boardId)).willReturn(Optional.of(board));
        assertTrue(boardService.delete(boardId));
    }

    @Test
    @DisplayName("Test that delete function correctly deletes all UserInBoard objects associated with board from repository")
    public void testDeleteDeletesUserInBoard() throws AccountNotFoundException {
        Long boardId = board.getId();
        given(boardRepository.findById(boardId)).willReturn(Optional.of(board));
        boardService.delete(boardId);
        assertTrue(userInBoardRepository.findAllBoardByUser(board.getCreatorUser()).isEmpty());
    }

    @Test
    @DisplayName("Test that delete function throws AccountNotFoundException if board does not exist in repository")
    public void testDeleteWithNonExistentBoardThrowsException() {
        given(boardRepository.findById(123L)).willReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> boardService.delete(123L));
    }

    @Test
    @DisplayName("Test that delete function throws NullPointerException if board is null")
    public void testDeleteWithNullBoardThrowsException() {
        assertThrows(NullPointerException.class, () -> boardService.delete(null));
    }


    @Test
    void get() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getAllBoardsOfUser() {
    }
}