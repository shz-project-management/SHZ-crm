package CRM.controller.facades;

import CRM.entity.Board;
import CRM.entity.DTO.BoardDTO;
import CRM.entity.User;
import CRM.entity.requests.BoardRequest;
import CRM.entity.requests.LoginUserRequest;
import CRM.entity.requests.RegisterUserRequest;
import CRM.entity.response.Response;
import CRM.service.AuthService;
import CRM.service.BoardService;
import CRM.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import javax.naming.AuthenticationException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@ExtendWith(MockitoExtension.class)
class BoardFacadeTest {

    @Mock
    private BoardService boardService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private BoardFacade boardFacade;

    private RegisterUserRequest correctRegisterUserRequest;
    private BoardRequest correctBoardRequest;
    private User user;
    private Board board;


    @BeforeEach
    void setUp() {
        correctRegisterUserRequest = new RegisterUserRequest("Ziv", "Hausler", "ziv123456", "test@gmail.com");
        user = User.newUser(correctRegisterUserRequest);
        user.setId(1L);
        board = Board.createBoard(user, "board", "nice");
    }


//    @Test
//    @DisplayName("Test the case where the boardRequest is valid and the board is successfully created")
//    public void testValidBoardRequest() throws AccountNotFoundException {
//        correctBoardRequest = new BoardRequest(1L, "board", "nice");
//        given(authService.findById(user.getId())).willReturn(user);
//        Board boardWithID = Board.createBoard(user, "board", "nice");
//        boardWithID.setId(1L);
//        given(boardService.create(board)).willReturn(boardWithID);
//        assertEquals(201, boardFacade.create(correctBoardRequest).getStatusCode());
//    }

    @Test
    @DisplayName("Test the case where the boardRequest has an invalid name")
    public void testInvalidNameBoardRequest() {
//        given(authService.findById(user.getId()));
        BoardRequest incorrectBoardRequest = new BoardRequest(1L, "@#!1", "nice");
        assertEquals(400, boardFacade.create(incorrectBoardRequest).getStatusCode());
    }


    @Test
    @DisplayName("Test the case where the boardRequest has a null name")
    public void testNullNameBoardRequest() {
        BoardRequest incorrectBoardRequest = new BoardRequest(1L, null, "nice");
        assertEquals(500, boardFacade.create(incorrectBoardRequest).getStatusCode());
    }

    @Test
    @DisplayName("Test the case where the boardRequest has a null creator user ID")
    public void testNullCreatorUserId() {
        BoardRequest incorrectBoardRequest = new BoardRequest(null, "board", "nice");
        assertEquals(500, boardFacade.create(incorrectBoardRequest).getStatusCode());
    }

    @Test
    @DisplayName("Test the case where the creator user ID in the boardRequest does not exist")
    public void testNonExistentCreatorBoardRequest() throws AccountNotFoundException {
        BoardRequest incorrectBoardRequest = new BoardRequest(100L, "board", "nice");
        given(authService.findById(incorrectBoardRequest.getCreatorUserId())).willThrow(AccountNotFoundException.class);
        assertEquals(400, boardFacade.create(incorrectBoardRequest).getStatusCode());
    }


    @Test
    @DisplayName("Test delete with valid id")
    public void testDeleteWithValidId() {
        Long id = 1L;
        assertEquals(204, boardFacade.delete(id).getStatusCode());
    }

    @Test
    @DisplayName("Test delete with invalid id")
    public void testDeleteWithInvalidId() {
        assertEquals(400, boardFacade.delete(Long.valueOf("-2")).getStatusCode());
    }

    @Test
    @DisplayName("Test delete with null id")
    public void testDeleteWithNullId() {
        assertEquals(500, boardFacade.delete(null).getStatusCode());
    }

    @Test
    @DisplayName("Test delete with non existent board id in database")
    public void testDeleteWithNonExistentBoard() throws AccountNotFoundException {
        given(boardService.delete(1L)).willThrow(NoSuchElementException.class);
        assertEquals(400, boardFacade.delete(1L).getStatusCode());
    }

    @Test
    @DisplayName("Test get with valid id")
    public void testGetWithValidId() throws AccountNotFoundException {
        Long id = 1L;
        given(boardService.get(id)).willReturn(board);
        assertEquals(200, boardFacade.get(id).getStatusCode());
    }

    @Test
    @DisplayName("Test get with non-existent id")
    public void testGetWithNonExistentId() throws AccountNotFoundException {
        Long id = 1L;
        given(boardService.get(id)).willThrow(NoSuchElementException.class);
        assertEquals(400, boardFacade.get(id).getStatusCode());
    }

    @Test
    @DisplayName("Test get with null id")
    public void testGetWithNullId() {
        Long id = null;
        assertEquals(500, boardFacade.get(id).getStatusCode());
    }

    @Test
    @DisplayName("Test get with invalid id")
    public void testGetWithInvalidId() {
        assertEquals(400, boardFacade.get(Long.valueOf("-2")).getStatusCode());
    }

    @Test
    @DisplayName("Test getAll boards")
    public void testGetAllWithMultipleBoards() {
        List<Board> expectedBoards = new ArrayList<>();
        given(boardService.getAll()).willReturn(expectedBoards);
        assertEquals(200, boardFacade.getAll().getStatusCode());
    }

    @Test
    @DisplayName("Test getAllBoardsOfUser with valid user id and no boards")
    public void testGetAllBoardsOfUserWithValidUserIdAndNoBoards() throws AccountNotFoundException {
        Long userId = 1L;
        List<Board> expectedBoards = new ArrayList<>();
        given(boardService.getAllBoardsOfUser(userId)).willReturn(expectedBoards);
        assertEquals(200, boardFacade.getAllBoardsOfUser(userId).getStatusCode());
    }

    @Test
    @DisplayName("Test get all boards with invalid user id")
    public void testGetAllBoardsWithInvalidId() {
        assertEquals(400, boardFacade.getAllBoardsOfUser(Long.valueOf("-2")).getStatusCode());
    }

    @Test
    @DisplayName("Test get with null id")
    public void testGetAllBoardsWithNullId() {
        Long id = null;
        assertEquals(500, boardFacade.getAllBoardsOfUser(id).getStatusCode());
    }

    @Test
    @DisplayName("Test get all boards with non-existent user id")
    public void testGetAllBoardsWithNonExistentUserId() throws AccountNotFoundException {
        Long id = 1L;
        given(boardService.getAllBoardsOfUser(id)).willThrow(AccountNotFoundException.class);
        assertEquals(400, boardFacade.getAllBoardsOfUser(id).getStatusCode());
    }
}