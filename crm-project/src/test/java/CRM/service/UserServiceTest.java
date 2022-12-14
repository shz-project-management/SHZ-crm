package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.repository.BoardRepository;
//import CRM.repository.UserInBoardRepository;
import CRM.repository.NotificationSettingRepository;
import CRM.repository.UserRepository;
import CRM.utils.enums.Permission;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.security.auth.login.AccountNotFoundException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private NotificationSettingRepository notificationSettingRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Test get method when user exists")
    public void testGetMethodWhenUserExists() throws AccountNotFoundException {
        User storedUser = new User(1L, "Ziv", "not-the-same-password-for-sure", "ziv@gmail.com", null);
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
        User differentUser = new User(2L, "Ziv", "not-the-same-password-for-sure", "ziv@gmail.com", null);
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
                new User(1L, "Ziv1", "ziv123456", "ziv1@gmail.com", null),
                new User(2L, "Ziv2", "ziv123456", "ziv2@gmail.com", null),
                new User(3L, "Ziv3", "ziv123456", "ziv3@gmail.com", null)
        ));

        // Call the getAll method
        List<User> users = userService.getAll();

        // Assert that the returned list has the correct size and contains the correct users
        assertEquals(3, users.size());
        assertEquals(1L, users.get(0).getId());
        assertEquals("Ziv1", users.get(0).getFullName());
        assertEquals(2L, users.get(1).getId());
        assertEquals("Ziv2", users.get(1).getFullName());
        assertEquals(3L, users.get(2).getId());
        assertEquals("Ziv3", users.get(2).getFullName());
    }

    @Test
    @DisplayName("Test getAllInBoard method when board exists and there are multiple users in the board")
    public void testGetAllInBoardMethodWhenBoardExistsAndMultipleUsers() throws AccountNotFoundException {
        User user1 = new User(1L, "Ziv1", "ziv123456", "ziv1@gmail.com", null);
        User user2 = new User(2L, "Ziv2", "ziv123456", "ziv2@gmail.com", null);
        User user3 = new User(3L, "Ziv3", "ziv123456", "ziv3@gmail.com", null);
        Board board1 = Board.createBoard(user1, "hello1", "world1");
        when(boardRepository.findById(1L)).thenReturn(Optional.of(board1));

        UserPermission userPermission1 = new UserPermission(1L, user1, Permission.USER);
        UserPermission userPermission2 = new UserPermission(2L, user2, Permission.USER);
        UserPermission userPermission3 = new UserPermission(3L, user3, Permission.USER);
        board1.addUserPermissionToBoard(userPermission1);
        board1.addUserPermissionToBoard(userPermission2);
        board1.addUserPermissionToBoard(userPermission3);

        List<User> users = userService.getAllInBoard(1L);
        assertEquals(3, users.size());
    }

    @Test
    @DisplayName("Test getAllInBoard method when board does not exist")
    public void testGetAllInBoardMethodWhenBoardDoesNotExist() {
        when(boardRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.getAllInBoard(1L));
    }

    //
//    @Test
//    @DisplayName("Test valid user ID and board ID values add a new user to an existing board")
//    public void testAddUserToBoard() throws AccountNotFoundException {
//        User user = new User();
//        user.setId(1L);
//        given(userRepository.findById(1L)).willReturn(Optional.of(user));
//
//        Board board = new Board();
//        board.setId(1L);
//        given(boardRepository.findById(1L)).willReturn(Optional.of(board));
//
//        given(userInBoardRepository.findByBoardAndUser(user, board)).willReturn(Optional.empty());
//        given(userInBoardRepository.save(Mockito.any())).willReturn(new UserInBoard());
//
//        UserInBoard userInBoard = userService.addUserToBoard(1L, 1L);
//        assertNotNull(userInBoard);
//    }
//
    @Test
    @DisplayName("Test non-existent user ID value throws AccountNotFoundException with USER_NOT_FOUND message")
    public void testNonExistentUserId() {
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        objectsIdsRequest.setUserId(100L);
        objectsIdsRequest.setEmail("test@gmail.com");
        assertThrows(AccountNotFoundException.class, () -> userService.updateUserToBoard(objectsIdsRequest));
    }

    @Test
    @DisplayName("Test non-existent board ID value throws AccountNotFoundException with BOARD_NOT_FOUND message")
    public void testNonExistentBoardId() {
        User user1 = new User(1L, "test", "test123456", "test@gmail.com", null);
        given(userRepository.findByEmail(user1.getEmail())).willReturn(Optional.of(user1));
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        objectsIdsRequest.setUserId(1L);
        objectsIdsRequest.setEmail("test@gmail.com");
        assertThrows(NoSuchElementException.class, () -> userService.updateUserToBoard(objectsIdsRequest));
    }


//    @Test
//    @DisplayName("Test combination of user and board that already exists in the database throws IllegalArgumentException with USER_IN_BOARD_EXISTS message")
//    public void testUserInBoardAlreadyExists() throws AccountNotFoundException {
//        User user = new User();
//        user.setId(1L);
//
//        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
//        objectsIdsRequest.setUserId(1L);
//        objectsIdsRequest.setBoardId(1L);
//        objectsIdsRequest.setPermissionId(1L);
//        objectsIdsRequest.setEmail("test@gmail.com");
//
//        given(userRepository.findByEmail("test@gmail.com")).willReturn(Optional.of(user));
//
//        Board board = new Board();
//        board.setId(1L);
//        given(boardRepository.findById(1L)).willReturn(Optional.of(board));
//
//        UserPermission userPermission1 = new UserPermission(1L, user, Permission.USER);
//        List<UserPermission> upList = Collections.singletonList(userPermission1);
//        given(notificationSettingRepository.findAll()).willReturn(new ArrayList<>());
//        assertEquals(userPermission1, userService.updateUserToBoard(objectsIdsRequest));
//    }
//
//    @Test
//    @DisplayName("Test removal of all user dependencies")
//    public void testRemoveAllUserDependencies() {
//        User user = new User();
//        user.setId(1L);
//        assertTrue(userService.removeAllUserDependencies(user));
//    }
//
//    @Test
//    @DisplayName("Test removal of boards associated with user")
//    public void testRemoveBoardsByUser() {
//        User user = new User();
//        user.setId(1L);
//        List<Board> boardList = Arrays.asList(new Board(), new Board());
//        when(boardRepository.findAllByUser(user)).thenReturn(boardList);
//        assertTrue(userService.removeUserDependenciesFromBoardTable(user));
//    }
//
//    @Test
//    @DisplayName("Test handling of empty list of boards")
//    public void testHandleEmptyBoardList() {
//        User user = new User();
//        user.setId(1L);
//        List<Board> boardList = new ArrayList<>();
//        when(boardRepository.findAllByUser(user)).thenReturn(boardList);
//        assertTrue(userService.removeUserDependenciesFromBoardTable(user));
//    }
//
//    @Test
//    @DisplayName("Test deletion of user and dependencies")
//    public void testDeleteUserAndDependencies() throws AccountNotFoundException {
//        long userId = 1L;
//        User user = new User();
//        user.setId(userId);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        given(userService.removeDependenciesFromBoard(user)).willReturn(true);
////        given(userRepository.delete(user)).
//        doNothing().when(userRepository).delete(user);
//        assertTrue(userService.delete(userId));
//    }
//
    @Test
    @DisplayName("Test handling of non-existent user")
    public void testHandleNonExistentUser() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> userService.delete(userId));
    }


//    @Test
//    @DisplayName("Test deletion of user with no dependencies")
//    public void testDeleteUserWithNoDependencies() throws AccountNotFoundException {
//        long userId = 1L;
//        User user = new User();
//        user.setId(userId);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        assertTrue(userService.delete(userId));
//    }

//    @Test
//    @DisplayName("Test deletion of user with multiple dependencies")
//    public void testDeleteUserWithMultipleDependencies() throws AccountNotFoundException {
//        long userId = 1L;
//        User user = new User();
//        user.setId(userId);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        UserInBoard userInBoard = new UserInBoard();
//        Comment comment = new Comment();
//        Attribute attribute = new Attribute();
//        Board board = new Board();
//        when(userInBoardRepository.findAllByUserId(userId)).thenReturn(Arrays.asList(userInBoard, userInBoard));
//        when(commentRepository.findAllByUserId(userId)).thenReturn(Arrays.asList(comment, comment));
//        when(attributeRepository.findAllByUserId(userId)).thenReturn(Arrays.asList(attribute, attribute));
//        when(boardRepository.findAllByUserId(userId)).thenReturn(Arrays.asList(board, board));
//        assertTrue(delete(userId));
//        verify(userInBoardRepository, times(2)).delete(userInBoard);
//        verify(commentRepository, times(2)).delete(comment);
//        verify(attributeRepository, times(2)).delete(attribute);
//        verify(boardRepository, times(2)).delete(board);
//        verify(userRepository, times(1)).delete(user);
//    }
}
