package CRM.controller.facades;

import CRM.controller.controllers.UserController;
import CRM.entity.User;
import CRM.entity.response.Response;
import CRM.service.UserService;
import CRM.utils.enums.ExceptionMessage;
import CRM.utils.enums.SuccessMessage;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void setUp() {
        // set up any necessary mock behavior here
    }

    @Test
    @DisplayName("Test get method with valid input")
    void testGetWithValidInput() throws AccountNotFoundException {
        User user = new User(1L, "Ziv", "Hausler", "not-the-same-password-for-sure", "ziv@gmail.com", null, null);
        when(userService.get(user.getId())).thenReturn(user);

        Response response = userFacade.get(user.getId());

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(200, response.getStatusCode());
        assertEquals(SuccessMessage.FOUND.toString(), response.getMessage());
        assertEquals(user, response.getData());
        verify(userService).get(user.getId());
    }

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

    @Test
    @DisplayName("Test getAll method with valid input")
    void testGetAllWithValidInput() {
        List<User> users = Arrays.asList(
                new User(1L, "Ziv", "Hausler", "not-the-same-password-for-sure", "ziv1@gmail.com", null, null),
                new User(2L, "Ziv", "Hausler", "not-the-same-password-for-sure", "ziv2@gmail.com", null, null)
        );
        when(userService.getAll()).thenReturn(users);

        Response response = userFacade.getAll();

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(200, response.getStatusCode());
        assertEquals(SuccessMessage.FOUND.toString(), response.getMessage());
        assertEquals(users, response.getData());
        verify(userService).getAll();
    }

    @Test
    @DisplayName("Test getAllInBoard method with valid input")
    void testGetAllInBoardWithValidInput() {
        Long boardId = 1L;
        List<User> users = Arrays.asList(
                new User(1L, "Ziv", "Hausler", "not-the-same-password-for-sure", "ziv1@gmail.com", null, null),
                new User(2L, "Ziv", "Hausler", "not-the-same-password-for-sure", "ziv2@gmail.com", null, null)
        );
        when(userService.getAllInBoard(boardId)).thenReturn(users);

        Response response = userFacade.getAllInBoard(boardId);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(200, response.getStatusCode());
        assertEquals(SuccessMessage.FOUND.toString(), response.getMessage());
        assertEquals(users, response.getData());
        verify(userService).getAllInBoard(boardId);
    }

    @Test
    @DisplayName("Test getAllInBoard method with invalid id")
    void testGetAllInBoardWithInvalidId() {
        given(userService.getAllInBoard(1L)).willThrow(IllegalArgumentException.class);
        assertEquals(400, userFacade.getAllInBoard(1L).getStatusCode());
    }

    @Test
    @DisplayName("Test getAllInBoard method when board is not found")
    void testGetAllInBoardBoardNotFound() {
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
    void testGetAllInBoardWithNullPointerException() {
        Long boardId = 1L;
        when(userService.getAllInBoard(boardId)).thenThrow(new NullPointerException());

        Response response = userFacade.getAllInBoard(boardId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals(500, response.getStatusCode());
        verify(userService).getAllInBoard(boardId);
    }
}
