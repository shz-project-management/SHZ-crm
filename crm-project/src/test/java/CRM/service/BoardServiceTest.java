package CRM.service;

import CRM.entity.Board;
import CRM.entity.User;
import CRM.entity.UserInBoard;
import CRM.entity.requests.BoardRequest;
import CRM.entity.requests.RegisterUserRequest;
import CRM.entity.requests.UpdateBoardRequest;
import CRM.repository.BoardRepository;
import CRM.repository.UserInBoardRepository;
import CRM.repository.UserRepository;
import CRM.utils.enums.Permission;
import org.junit.jupiter.api.BeforeAll;
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

import java.util.*;

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
        board.setId(1L);
        given(boardRepository.findById(board.getId())).willReturn(Optional.of(board));
        assertTrue(boardService.delete(board.getId()));
    }

    @Test
    @DisplayName("Test that delete function correctly deletes all UserInBoard objects associated with board from repository")
    public void testDeleteDeletesUserInBoard() throws AccountNotFoundException {
        board.setId(1L);
        given(boardRepository.findById(board.getId())).willReturn(Optional.of(board));
        boardService.delete(board.getId());
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
    @DisplayName("Test that get function correctly retrieves board from repository")
    public void testGetRetrievesBoard() throws AccountNotFoundException {
        board.setId(1L);
        given(boardRepository.findById(board.getId())).willReturn(Optional.of(board));
        assertEquals(board, boardService.get(board.getId()));
    }

    @Test
    @DisplayName("Test that get function throws AccountNotFoundException if board does not exist in repository")
    public void testGetWithNonExistentBoardThrowsException() {
        given(boardRepository.findById(123L)).willReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> boardService.get(123L));
    }

    @Test
    @DisplayName("Test that get function throws NullPointerException if board is null")
    public void testGetWithNullBoardThrowsException() {
        assertThrows(NullPointerException.class, () -> boardService.get(null));
    }

    @Test
    @DisplayName("Test that getAll function correctly retrieves all boards from repository")
    public void testGetAllRetrievesAllBoards() {
        List<Board> boards = Collections.singletonList(board);
        given(boardRepository.findAll()).willReturn(boards);
        assertEquals(boards, boardService.getAll());
    }

    @Test
    @DisplayName("Test that getAllBoardsOfUser function correctly retrieves all boards that user is a member of")
    public void testGetAllBoardsOfUserRetrievesCorrectBoards() throws AccountNotFoundException {
        board.setId(1L);
        expectedUser.setId(1L);
        Board boardTwo = Board.createBoard(expectedUser, "second board", "nice");
        boardTwo.setId(2L);
        UserInBoard userInBoard1 = UserInBoard.adminUserInBoard(expectedUser, board);
        UserInBoard userInBoard2 = UserInBoard.adminUserInBoard(expectedUser, boardTwo);
        List<UserInBoard> userInBoards = Arrays.asList(userInBoard1, userInBoard2);
        given(userRepository.findById(expectedUser.getId())).willReturn(Optional.of(expectedUser));
        given(userInBoardRepository.findAllBoardByUser(expectedUser)).willReturn(userInBoards);
        assertEquals(Arrays.asList(board, boardTwo), boardService.getAllBoardsOfUser(expectedUser.getId()));
    }

    @Test
    @DisplayName("Test that getAllBoardsOfUser function returns empty list if user is not a member of any boards")
    public void testGetAllBoardsOfUserWithNoBoardsReturnsEmptyList() throws AccountNotFoundException {
        expectedUser.setId(1L);
        given(userRepository.findById(expectedUser.getId())).willReturn(Optional.of(expectedUser));
        given(userInBoardRepository.findAllBoardByUser(expectedUser)).willReturn(Collections.emptyList());
        assertTrue(boardService.getAllBoardsOfUser(expectedUser.getId()).isEmpty());
    }

    @Test
    @DisplayName("Test that getAllBoardsOfUser function throws NoSuchElementException if user does not exist in repository")
    public void testGetAllBoardsOfUserWithNonExistentUserThrowsException() {
        given(userRepository.findById(123L)).willReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> boardService.getAllBoardsOfUser(123L));
    }

    @Test
    @DisplayName("Test that getAllBoardsOfUser function throws NullPointerException if userId is null")
    public void testGetAllBoardsOfUserWithNullUserThrowsException() {
        assertThrows(NullPointerException.class, () -> boardService.getAllBoardsOfUser(null));
    }

    @Test
    @DisplayName("Test update board with valid input")
    public void testUpdateBoardWithValidInput() throws AccountNotFoundException {
        UpdateBoardRequest boardRequest = new UpdateBoardRequest();
        boardRequest.setId(1L);
        boardRequest.setName("Test Board");
        given(boardRepository.findById(boardRequest.getId())).willReturn(Optional.of(board));
        given(boardRepository.save(board)).willReturn(board);
        assertNotNull(boardService.updateBoard(boardRequest));
    }

    @Test
    @DisplayName("Test update board with invalid board ID")
    public void testUpdateBoardWithInvalidBoardId() {
        UpdateBoardRequest boardRequest = new UpdateBoardRequest();
        boardRequest.setId(-2L);
        boardRequest.setName("Test Board");
        assertThrows(NoSuchElementException.class, () -> boardService.updateBoard(boardRequest));
    }

    @Test
    @DisplayName("Test update board with null name")
    public void testUpdateBoardWithNullName() throws AccountNotFoundException {
        UpdateBoardRequest boardRequest = new UpdateBoardRequest();
        boardRequest.setId(1L);
        boardRequest.setName(null);
        assertThrows(NoSuchElementException.class, () -> boardService.updateBoard(boardRequest));
    }

    @Test
    @DisplayName("Test update board with null request")
    public void testUpdateBoardWithNullRequest() {
        UpdateBoardRequest boardRequest = null;
        assertThrows(NullPointerException.class, () -> boardService.updateBoard(boardRequest));
    }
}