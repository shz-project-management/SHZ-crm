package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.CommentRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.repository.*;
import CRM.utils.enums.UpdateField;
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

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    private CommentRequest commentRequest;


    @Test
    @DisplayName("Test create method with user not found")
    void testCreateWithUserNotFound() {
        commentRequest = new CommentRequest();
        commentRequest.setParentItemId(1L);
        commentRequest.setUserId(1L);
        commentRequest.setTitle("Test Title");
        commentRequest.setDescription("Test Description");
        Item item = Item.createNewItem(null, null, null, null, "title", "desc", null, 1);
        given(userRepository.findById(1L)).willReturn(Optional.empty());
        given(itemRepository.findById(1L)).willReturn(Optional.of(item));
        assertThrows(AccountNotFoundException.class, () -> commentService.create(commentRequest));
    }

    @Test
    @DisplayName("Test create method with invalid parent item")
    void testCreateWithInvalidParentItem() {
        commentRequest = new CommentRequest();
        commentRequest.setParentItemId(1L);
        commentRequest.setUserId(1L);
        commentRequest.setTitle("Test Title");
        commentRequest.setDescription("Test Description");
        User user = new User();
        user.setId(1L);
        given(itemRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> commentService.create(commentRequest));
    }

    @Test
    @DisplayName("Test create method with null user id")
    void testCreateWithNullUserId() {
        commentRequest = new CommentRequest();
        commentRequest.setParentItemId(1L);
        commentRequest.setUserId(null);
        commentRequest.setTitle("Test Title");
        commentRequest.setDescription("Test Description");
        Item item = Item.createNewItem(null, null, null, null, "title", "desc", null, 1);
        given(itemRepository.findById(1L)).willReturn(Optional.of(item));
        assertThrows(AccountNotFoundException.class, () -> commentService.create(commentRequest));
    }

    @Test
    @DisplayName("Test create method with null parent item id")
    void testCreateWithNullParentItemId() {
        commentRequest = new CommentRequest();
        commentRequest.setParentItemId(null);
        commentRequest.setUserId(1L);
        commentRequest.setTitle("Test Title");
        commentRequest.setDescription("Test Description");
        User user = new User();
        user.setId(1L);
        assertThrows(NoSuchElementException.class, () -> commentService.create(commentRequest));
    }

    @Test
    @DisplayName("Test delete method with valid input")
    void testDeleteWithValidInput() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        User user3 = new User();
        user3.setId(3L);
        Item item = Item.createNewItem(null, null, null, user1, "title", "desc", null, 1);
        Comment comment1 = Comment.createNewComment(user1, "title", "desc", item);
        Comment comment2 = Comment.createNewComment(user2, "title", "desc", item);
        Comment comment3 = Comment.createNewComment(user3, "title", "desc", item);
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment1));
        given(commentRepository.findById(2L)).willReturn(Optional.of(comment2));
        given(commentRepository.findById(3L)).willReturn(Optional.of(comment3));
        int result = commentService.delete(ids);
        assertEquals(3, result);
    }

    @Test
    @DisplayName("Test delete method with invalid input")
    void testDeleteThreeIDsAndOnlyOneValidWithTwoInvalidInput() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        User user1 = new User();
        user1.setId(1L);
        Item item = Item.createNewItem(null, null, null, user1, "title", "desc", null, 1);
        Comment comment1 = Comment.createNewComment(user1, "title", "desc", item);
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment1));
        given(commentRepository.findById(2L)).willReturn(Optional.empty());
        given(commentRepository.findById(3L)).willReturn(Optional.empty());
        int result = commentService.delete(ids);
        assertEquals(1, result);
    }

    @Test
    @DisplayName("Test update method with valid comment id")
    void testUpdateWithValidCommentId() throws NoSuchFieldException {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        updateObject.setFieldName(UpdateField.DESCRIPTION);
        updateObject.setContent("Updated descr");
        Comment comment = new Comment();
        comment.setId(1L);
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));
        given(commentRepository.save(comment)).willReturn(comment);
        assertNotNull(commentService.update(updateObject, 1L));
    }

    @Test
    @DisplayName("Test update method with invalid comment id")
    void testUpdateWithInvalidCommentId() {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        updateObject.setFieldName(UpdateField.DESCRIPTION);
        updateObject.setContent("Updated descr");
        given(commentRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> commentService.update(updateObject, 1L));
    }

    @Test
    @DisplayName("Test update method with null input")
    void testUpdateWithNullInput() {
        UpdateObjectRequest updateObject = null;
        Comment comment = new Comment();
        comment.setId(1L);
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));
        assertThrows(NullPointerException.class, () -> commentService.update(updateObject, 1L));
    }

    @Test
    @DisplayName("Test update method with null field name")
    void testUpdateWithNullFieldName() {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        updateObject.setFieldName(null);
        updateObject.setContent("Updated");
        Comment comment = new Comment();
        comment.setId(1L);
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));
        assertThrows(NullPointerException.class, () -> commentService.update(updateObject, 1L));
    }

    @Test
    @DisplayName("Test get method with valid input")
    void testGetWithValidInput() {
        Comment comment = new Comment();
        comment.setId(1L);
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));
        Comment result = commentService.get(1L);
        assertEquals(comment, result);
    }

    @Test
    @DisplayName("Test get method with invalid input")
    void testGetWithInvalidInput() {
        given(commentRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> commentService.get(1L));
    }

    @Test
    @DisplayName("Test getAllInItem method with valid input")
    void testGetAllInItemWithValidInput() {
        Item item = new Item();
        item.setId(1L);
        List<Comment> comments = Arrays.asList(
                new Comment(),
                new Comment(),
                new Comment()
        );
        given(itemRepository.findById(1L)).willReturn(Optional.of(item));
        given(commentRepository.findAllByParentItem(item)).willReturn(comments);
        List<SharedContent> result = commentService.getAllInItem(1L);
        assertEquals(comments, result);
    }

    @Test
    @DisplayName("Test getAllInItem method with invalid input")
    void testGetAllInItemWithInvalidInput() {
        given(itemRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> commentService.getAllInItem(1L));
    }

    @Test
    @DisplayName("Test getAllInItem method with no comments in item")
    void testGetAllInItemWithNoCommentsInItem() {
        Item item = new Item();
        item.setId(1L);
        given(itemRepository.findById(1L)).willReturn(Optional.of(item));
        given(commentRepository.findAllByParentItem(item)).willReturn(new ArrayList<>());
        List<SharedContent> result = commentService.getAllInItem(1L);
        assertTrue(result.isEmpty());
    }

//    @Test
//    @DisplayName("Test getAllCommentsInBoard method with valid input")
//    void testGetAllCommentsInBoardWithValidInput() {
//        Board board = new Board();
//        board.setId(1L);
//        List<Item> items = Arrays.asList(
//                new Item(),
//                new Item()
//        );
//        items.get(0).setId(1L);
//        items.get(1).setId(2L);
//        board.setSections(new HashSet<>());
//        List<Comment> comments1 = Arrays.asList(
//                new Comment(),
//                new Comment()
//        );
//        List<Comment> comments2 = Arrays.asList(
//                new Comment(),
//                new Comment()
//        );
//        given(boardRepository.findById(1L)).willReturn(Optional.of(board));
//        given(commentRepository.findAllByParentItem(items.get(0))).willReturn(comments1);
//        given(commentRepository.findAllByParentItem(items.get(1))).willReturn(comments2);
//        List<Comment> result = commentService.getAllCommentsInBoard(1L);
//        assertEquals(4, result.size());
//    }

    @Test
    @DisplayName("Test getAllCommentsInBoard method with invalid input")
    void testGetAllCommentsInBoardWithInvalidInput() {
        given(boardRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> commentService.getAllCommentsInBoard(1L));
    }

    @Test
    @DisplayName("Test getAllCommentsInBoard method with no items in board")
    void testGetAllCommentsInBoardWithNoItemsInBoard() {
        Board board = new Board();
        board.setId(1L);
        board.setSections(new HashSet<>());
        given(boardRepository.findById(1L)).willReturn(Optional.of(board));
        List<Comment> result = commentService.getAllCommentsInBoard(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test getAllCommentsInStatus method with valid input")
    void testGetAllCommentsInStatusWithValidInput() {
        Status status = new Status();
        status.setId(1L);
        List<Item> items = Arrays.asList(
                new Item(),
                new Item()
        );
        Set<Item> itemsSet = new HashSet<>(items);
        List<Comment> comments1 = Arrays.asList(
                new Comment(),
                new Comment()
        );
        List<Comment> comments2 = Arrays.asList(
                new Comment()
        );
        given(statusRepository.findById(1L)).willReturn(Optional.of(status));
        given(itemRepository.findAllByStatus(status)).willReturn(itemsSet);
        given(commentRepository.findAllByParentItem(items.get(0))).willReturn(comments1);
        given(commentRepository.findAllByParentItem(items.get(1))).willReturn(comments2);
        List<Comment> result = commentService.getAllCommentsInStatus(1L);
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("Test getAllCommentsInStatus method with invalid input")
    void testGetAllCommentsInStatusWithInvalidInput() {
        given(statusRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> commentService.getAllCommentsInStatus(1L));
    }

}