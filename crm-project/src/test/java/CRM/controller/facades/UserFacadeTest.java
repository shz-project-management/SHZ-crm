package CRM.controller.facades;

import CRM.entity.User;
import CRM.entity.response.Response;
import CRM.service.UserService;
import CRM.utils.enums.ExceptionMessage;
import CRM.utils.enums.SuccessMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import javax.security.auth.login.AccountNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserFacadeTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserFacade userFacade;

//    @Test
//    @DisplayName("Test get method with valid input")
//    void testGetWithValidInput() throws AccountNotFoundException {
//        User user = new User(1L, "Ziv", "Hausler", "not-the-same-password-for-sure", "ziv@gmail.com", null, null);
//        when(userService.get(user.getId())).thenReturn(user);
//
//        Response response = userFacade.get(user.getId());
//
//        assertEquals(HttpStatus.OK, response.getStatus());
//        assertEquals(200, response.getStatusCode());
//        assertEquals(SuccessMessage.FOUND.toString(), response.getMessage());
//        assertEquals(user, response.getData());
//        verify(userService).get(user.getId());
//    }

    @Test
    @DisplayName("Test get method with invalid id")
    void testGetWithInvalidId() throws AccountNotFoundException {
        given(userService.get(1L)).willThrow(IllegalArgumentException.class);

        assertEquals(400, userFacade.get(1L).getStatusCode());
    }

    @Test
    @DisplayName("Test get method when user is not found")
    void testGetUserNotFound() throws AccountNotFoundException {
        Long id = 1L;
        when(userService.get(id)).thenThrow(new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString()));

        Response response = userFacade.get(id);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals(400, response.getStatusCode());
        assertEquals(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString(), response.getMessage());
        verify(userService).get(id);
    }

    @Test
    @DisplayName("Test get method when null pointer exception is thrown")
    void testGetWithNullPointerException() throws AccountNotFoundException {
        Long id = 1L;
        when(userService.get(id)).thenThrow(new NullPointerException());

        Response response = userFacade.get(id);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals(500, response.getStatusCode());
        verify(userService).get(id);
    }

//    @Test
//    @DisplayName("Test getAll method with valid input")
//    void testGetAllWithValidInput() {
//        List<User> users = Arrays.asList(
//                new User(1L, "Ziv", "Hausler", "not-the-same-password-for-sure", "ziv1@gmail.com", null, null),
//                new User(2L, "Ziv", "Hausler", "not-the-same-password-for-sure", "ziv2@gmail.com", null, null)
//        );
//        when(userService.getAll()).thenReturn(users);
//
//        Response response = userFacade.getAll();
//
//        assertEquals(HttpStatus.OK, response.getStatus());
//        assertEquals(200, response.getStatusCode());
//        assertEquals(SuccessMessage.FOUND.toString(), response.getMessage());
//        assertEquals(users, response.getData());
//        verify(userService).getAll();
//    }

//    @Test
//    @DisplayName("Test getAllInBoard method with valid input")
//    void testGetAllInBoardWithValidInput() throws AccountNotFoundException {
//        Long boardId = 1L;
//        List<User> users = Arrays.asList(
//                new User(1L, "Ziv", "Hausler", "not-the-same-password-for-sure", "ziv1@gmail.com", null, null),
//                new User(2L, "Ziv", "Hausler", "not-the-same-password-for-sure", "ziv2@gmail.com", null, null)
//        );
//        when(userService.getAllInBoard(boardId)).thenReturn(users);
//
//        Response response = userFacade.getAllInBoard(boardId);
//
//        assertEquals(HttpStatus.OK, response.getStatus());
//        assertEquals(200, response.getStatusCode());
//        assertEquals(SuccessMessage.FOUND.toString(), response.getMessage());
//        assertEquals(users, response.getData());
//        verify(userService).getAllInBoard(boardId);
//    }

    @Test
    @DisplayName("Test getAllInBoard method with invalid id")
    void testGetAllInBoardWithInvalidId() throws AccountNotFoundException {
        given(userService.getAllInBoard(1L)).willThrow(IllegalArgumentException.class);
        assertEquals(400, userFacade.getAllInBoard(1L).getStatusCode());
    }

    @Test
    @DisplayName("Test getAllInBoard method when board is not found")
    void testGetAllInBoardBoardNotFound() throws AccountNotFoundException {
        Long boardId = 1L;
        when(userService.getAllInBoard(boardId)).thenThrow(new NoSuchElementException(ExceptionMessage.BOARD_DOES_NOT_EXISTS.toString()));

        Response response = userFacade.getAllInBoard(boardId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals(400, response.getStatusCode());
        assertEquals(ExceptionMessage.BOARD_DOES_NOT_EXISTS.toString(), response.getMessage());
        verify(userService).getAllInBoard(boardId);
    }

    @Test
    @DisplayName("Test getAllInBoard method when null pointer exception is thrown")
    void testGetAllInBoardWithNullPointerException() throws AccountNotFoundException {
        Long boardId = 1L;
        when(userService.getAllInBoard(boardId)).thenThrow(new NullPointerException());

        Response response = userFacade.getAllInBoard(boardId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals(500, response.getStatusCode());
        verify(userService).getAllInBoard(boardId);
    }

    @Test
    @DisplayName("Test valid ID value returns OK status")
    public void testValidId() throws AccountNotFoundException {
        given(userService.delete(1L)).willReturn(true);
        Response response = userFacade.delete(1L);
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @DisplayName("Test invalid ID value returns BAD REQUEST status")
    public void testInvalidId() {
        Response response = userFacade.delete(-1L);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    @DisplayName("Test null ID value returns BAD REQUEST status")
    public void testNullId() {
        Response response = userFacade.delete(null);
        assertEquals(500, response.getStatusCode());
    }

    @Test
    @DisplayName("Test non-existent ID value returns BAD REQUEST status")
    public void testNonExistentId() throws AccountNotFoundException {
        given(userService.delete(100L)).willThrow(new AccountNotFoundException());
        Response response = userFacade.delete(100L);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    @DisplayName("Test invalid user ID value returns BAD REQUEST status and IllegalArgumentException message")
    public void testInvalidUserId() {
        Response response = userFacade.addUserToBoard(1L, -1L);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    @DisplayName("Test invalid board ID value returns BAD REQUEST status and IllegalArgumentException message")
    public void testInvalidBoardId() {
        Response response = userFacade.addUserToBoard(1L, -1L);
        assertEquals(400, response.getStatusCode());
    }

//    @Test
//    @DisplayName("Test non-existent user ID value returns BAD REQUEST status and AccountNotFoundException message")
//    public void testNonExistentUserId() throws AccountNotFoundException {
//        given(userService.addUserToBoard(100L, 1L)).willThrow(new AccountNotFoundException());
//        Response response = userFacade.addUserToBoard(100L, 1L);
//        assertEquals(400, response.getStatusCode());
//    }

//    @Test
//    @DisplayName("Test non-existent board ID value returns BAD REQUEST status and NoSuchElementException message")
//    public void testNonExistentBoardId() throws AccountNotFoundException {
//        given(userService.addUserToBoard(1L, 100L)).willThrow(new NoSuchElementException());
//        Response response = userFacade.addUserToBoard(1L, 100L);
//        assertEquals(400, response.getStatusCode());
//    }

    @Test
    @DisplayName("Test null user ID and board ID values return BAD REQUEST status and NullPointerException message")
    public void testNullIds() {
        Response response = userFacade.addUserToBoard(null, null);
        assertEquals(500, response.getStatusCode());
    }
}
