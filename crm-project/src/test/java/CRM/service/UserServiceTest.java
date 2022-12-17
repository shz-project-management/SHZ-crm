package CRM.service;

import CRM.entity.Board;
import CRM.entity.User;
import CRM.entity.UserInBoard;
import CRM.repository.BoardRepository;
import CRM.repository.UserInBoardRepository;
import CRM.repository.UserRepository;
import CRM.utils.enums.Permission;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.security.auth.login.AccountNotFoundException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private UserInBoardRepository userInBoardRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Test get method when user exists")
    public void testGetMethodWhenUserExists() throws AccountNotFoundException {
        User storedUser = new User(1L, "Ziv", "Hausler", "not-the-same-password-for-sure", "ziv@gmail.com", null, null);
        given(userRepository.findById(1L)).willReturn(Optional.of(storedUser));
        assertEquals(1L, userService.get(storedUser.getId()).getId());
    }

    @Test
    @DisplayName("Test get method when user does not exist")
    public void testGetMethodWhenUserDoesNotExist() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> userService.get(2L));
    }

    @Test
    @DisplayName("Test get method when Validations.doesIdExists throws AccountNotFoundException")
    public void testGetMethodWhenValidationsThrowsAccountNotFoundException() throws AccountNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> userService.get(1L));
    }

    @Test
    @DisplayName("Test get method when Validations.doesIdExists returns a user with a different id")
    public void testGetMethodWhenValidationsReturnsUserWithDifferentId() throws AccountNotFoundException {
        User differentUser = new User(2L, "Ziv", "Hausler", "not-the-same-password-for-sure", "ziv@gmail.com", null, null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(differentUser));
        assertNotEquals(1L, userService.get(1L).getId());
    }

    @Test
    @DisplayName("Test get method when Validations.doesIdExists returns null")
    public void testGetMethodWhenValidationsReturnsNull() throws AccountNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> userService.get(1L));
    }

    @Test
    @DisplayName("Test get method when user id is negative")
    public void testGetMethodWhenUserIdIsNegative() {
        assertThrows(AccountNotFoundException.class, () -> userService.get(-1L));
    }

    @Test
    @DisplayName("Test get method when user id is 0")
    public void testGetMethodWhenUserIdIsZero() {
        assertThrows(AccountNotFoundException.class, () -> userService.get(0L));
    }

    @Test
    @DisplayName("Test getAll method when there are no users in the repository")
    public void testGetAllMethodWhenNoUsers() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        List<User> users = userService.getAll();
        assertTrue(users.isEmpty());
    }

    @Test
    @DisplayName("Test getAll method when there are multiple users in the repository")
    public void testGetAllMethodWhenMultipleUsers() {
        // Set up the mock userRepository to return a list of users when findAll is called
        when(userRepository.findAll()).thenReturn(Arrays.asList(
                new User(1L, "Ziv1", "Hausler", "ziv123456", "ziv1@gmail.com", null, null),
                new User(2L, "Ziv2", "Hausler", "ziv123456", "ziv2@gmail.com", null, null),
                new User(3L, "Ziv3", "Hausler", "ziv123456", "ziv3@gmail.com", null, null)
        ));

        // Call the getAll method
        List<User> users = userService.getAll();

        // Assert that the returned list has the correct size and contains the correct users
        assertEquals(3, users.size());
        assertEquals(1L, users.get(0).getId());
        assertEquals("Ziv1", users.get(0).getFirstName());
        assertEquals("Hausler", users.get(0).getLastName());
        assertEquals(2L, users.get(1).getId());
        assertEquals("Ziv2", users.get(1).getFirstName());
        assertEquals("Hausler", users.get(1).getLastName());
        assertEquals(3L, users.get(2).getId());
        assertEquals("Ziv3", users.get(2).getFirstName());
        assertEquals("Hausler", users.get(2).getLastName());
    }

    @Test
    @DisplayName("Test getAllInBoard method when board exists and there are multiple users in the board")
    public void testGetAllInBoardMethodWhenBoardExistsAndMultipleUsers() throws AccountNotFoundException {
        User user1 = new User(1L, "Ziv1", "Hausler", "ziv123456", "ziv1@gmail.com", null, null);
        Board board1 = Board.createBoard(user1, "hello1", "world1");
        when(boardRepository.findById(1L)).thenReturn(Optional.of(board1));
        when(userInBoardRepository.findAllUserByBoard(board1))
                .thenReturn(Arrays.asList(
                 UserInBoard.userInBoardUserChoosePermission(new User(1L, "Ziv1", "Hausler", "ziv123456", "ziv1@gmail.com", null, null), Board.createBoard(user1, "hello", "world"), Permission.ADMIN),
                 UserInBoard.userInBoardUserChoosePermission(new User(2L, "Ziv2", "Hausler", "ziv123456", "ziv2@gmail.com", null, null), Board.createBoard(user1, "hello", "world"), Permission.USER),
                 UserInBoard.userInBoardUserChoosePermission(new User(3L, "Ziv3", "Hausler", "ziv123456", "ziv3@gmail.com", null, null), Board.createBoard(user1, "hello", "world"), Permission.USER)
        ));
        List<User> users = userService.getAllInBoard(1L);

        assertEquals(3, users.size());
        assertEquals(1L, users.get(0).getId());
        assertEquals("Ziv1", users.get(0).getFirstName());
        assertEquals("Hausler", users.get(0).getLastName());
        assertEquals(2L, users.get(1).getId());
        assertEquals("Ziv2", users.get(1).getFirstName());
        assertEquals("Hausler", users.get(1).getLastName());
        assertEquals(3L, users.get(2).getId());
        assertEquals("Ziv3", users.get(2).getFirstName());
        assertEquals("Hausler", users.get(2).getLastName());
    }

    @Test
    @DisplayName("Test getAllInBoard method when board does not exist")
    public void testGetAllInBoardMethodWhenBoardDoesNotExist() {
        when(boardRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.getAllInBoard(1L));
    }
}
