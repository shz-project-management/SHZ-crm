//package CRM.controller.facades;
//
//import CRM.entity.*;
//import CRM.entity.requests.CommentRequest;
//import CRM.entity.requests.ItemRequest;
//import CRM.entity.requests.UpdateObjectRequest;
//import CRM.entity.response.Response;
//import CRM.service.CommentService;
//import CRM.service.ItemService;
//import CRM.utils.Validations;
//import CRM.utils.enums.UpdateField;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//
//import javax.security.auth.login.AccountNotFoundException;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.NoSuchElementException;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class SharedContentFacadeTest {
//
//    @Mock
//    private ItemService itemService;
//    @Mock
//    private CommentService commentService;
//
//    @InjectMocks
//    private SharedContentFacade sharedContentFacade;
//
////    @Test
////    @DisplayName("Test that create returns correct response when input is valid")
////    void createItem_testValidInput() throws AccountNotFoundException {
////        ItemRequest item = new ItemRequest(1L, 1L, 1L, "", LocalDateTime.now(), 1);
////        item.setTitle("Test");
////        item.setParentItemId(1L);
////        item.setUserId(1L);
////
////        User user = new User(1L, "test", "testa", "test123456", "test@gmail.com", null, null);
////        Board board = new Board(1L, user, "title", "description", null, null, null);
////
////        Type type = new Type();
////        type.setBoard(board);
////        type.setName("type");
////        type.setDescription("description");
////
////        Status status = new Status();
////        status.setBoard(board);
////        status.setName("status");
////        status.setDescription("description");
////
////        Item expectedItem = new Item(board, status, type, "section", null, LocalDateTime.now(), 1, null, null);
////        expectedItem.setUser(user);
////
////        when(itemService.create(item)).thenReturn(expectedItem);
////
////        Response response = sharedContentFacade.create(item);
////
////        assertEquals(201, response.getStatusCode());
////    }
//
//    @Test
//    @DisplayName("Test that create returns bad response response when input is invalid")
//    void createItem_testInvalidInput() {
//        ItemRequest item = new ItemRequest();
//        item.setUserId(-1L);
//
//        Response response = sharedContentFacade.create(item);
//
//        assertEquals(400, response.getStatusCode());
//    }
//
//    @Test
//    @DisplayName("Test that create returns correct response when NullPointerException is thrown")
//    void createItem_testNullPointerException() throws AccountNotFoundException {
//        ItemRequest item = new ItemRequest();
//
//        Response response = sharedContentFacade.create(item);
//
//        assertEquals(500, response.getStatusCode());
//    }
//
////    @Test
////    @DisplayName("Test that create returns correct response when input is valid")
////    void createComment_testValidInput() throws AccountNotFoundException {
////        CommentRequest comment = new CommentRequest();
////        comment.setParentItemId(1L);
////        comment.setUserId(1l);
////        comment.setTitle("test");
////
////        User user = new User(1L, "test", "testa", "test123456", "test@gmail.com", null, null);
////        Board board = new Board(1L, user, "title", "description", null, null, null);
////
////        Type type = new Type();
////        type.setBoard(board);
////        type.setName("type");
////        type.setDescription("description");
////
////        Status status = new Status();
////        status.setBoard(board);
////        status.setName("status");
////        status.setDescription("description");
////
////        Item item = new Item(board, status, type, "section", null, LocalDateTime.now(), 1, null, null);
////        item.setUser(user);
////
////        Comment expectedComment = Comment.createNewComment(user, "title", "description", item);
////
////        when(commentService.create(comment)).thenReturn(expectedComment);
////
////        Response response = sharedContentFacade.create(comment);
////
////        assertEquals(201, response.getStatusCode());
////    }
//
//    @Test
//    @DisplayName("Test that create returns correct response when input is invalid")
//    void createComment_testInvalidInput() {
//        CommentRequest comment = new CommentRequest();
//        comment.setParentItemId(-1L);
//
//        Response response = sharedContentFacade.create(comment);
//
//        assertEquals(400, response.getStatusCode());
//    }
//
//    @Test
//    @DisplayName("Test that create returns correct response when NullPointerException is thrown")
//    void createComment_testNullPointerException() {
//        CommentRequest comment = new CommentRequest();
//
//        Response response = sharedContentFacade.create(comment);
//
//        assertEquals(500, response.getStatusCode());
//    }
//
//    @Test
//    @DisplayName("Test validate IDs")
//    void deleteItems_testValidateIds() {
//        List<Long> validList = Arrays.asList(1L, 456L);
//        given(itemService.delete(validList)).willReturn(1);
//        // Expect an IllegalArgumentException to be thrown
//        assertEquals(204, sharedContentFacade.delete(validList, Item.class)
//                .getStatusCode());
//    }
//
//    @Test
//    @DisplayName("Test validate IDs")
//    void deleteComments_testValidateIds() {
//        List<Long> validList = Arrays.asList(1L, 456L);
//        given(commentService.delete(validList)).willReturn(1);
//        // Expect an IllegalArgumentException to be thrown
//        assertEquals(204, sharedContentFacade.delete(validList, Comment.class)
//                .getStatusCode());
//    }
//
//    @Test
//    @DisplayName("Test invalidate IDs and show it still tries to delete")
//    void delete_testInvalidateIds() {
//        List<Long> validList = Arrays.asList(-1L, 456L);
//        // Expect an IllegalArgumentException to be thrown
//        assertEquals(204, sharedContentFacade.delete(validList, Item.class)
//                .getStatusCode());
//    }
//
//    @Test
//    @DisplayName("Test invalidate IDs and show it still tries to delete")
//    void delete_testOnlyInvalidateIds() {
//        List<Long> validList = Arrays.asList(-1L, -2L);
//        // Expect an IllegalArgumentException to be thrown
//        assertEquals(0, sharedContentFacade.delete(validList, Item.class).getData());
//    }
//
////    @Test
////    @DisplayName("Test validate ID")
////    void updateItem_testValidateId() {
////        UpdateObjectRequest updateObject = new UpdateObjectRequest();
////        Long invalidId = -123L;
////
////        assertEquals(400, sharedContentFacade.update(updateObject, invalidId, Item.class).getStatusCode());
////    }
//
//    @Test
//    @DisplayName("Test validate ID")
//    void updateComment_testValidateId() {
//        UpdateObjectRequest updateObject = new UpdateObjectRequest();
//        Long invalidId = -123L;
//
//        assertEquals(400, sharedContentFacade.update(updateObject, invalidId, Comment.class).getStatusCode());
//    }
//
//    @Test
//    @DisplayName("Test on update item when it does not exist in the db")
//    void updateItem_itemDoesntExist() throws NoSuchFieldException {
//        UpdateObjectRequest updateObject = new UpdateObjectRequest(UpdateField.NAME, "test");
//        Long id = 1L;
//
//        given(itemService.update(updateObject, id)).willThrow(NoSuchElementException.class);
//
//        assertEquals(400, sharedContentFacade.update(updateObject, id, Item.class).getStatusCode());
//    }
//
//    @Test
//    @DisplayName("Test on update comment when it does not exist in the db")
//    void updateComment_itemDoesntExist() throws NoSuchFieldException {
//        UpdateObjectRequest updateObject = new UpdateObjectRequest(UpdateField.NAME, "test");
//        Long id = 1L;
//
//        given(commentService.update(updateObject, id)).willThrow(NoSuchElementException.class);
//
//        assertEquals(400, sharedContentFacade.update(updateObject, id, Comment.class).getStatusCode());
//    }
//
////    @Test
////    @DisplayName("Test on update item when it does not exist in the db")
////    void updateItem_Success() throws NoSuchFieldException {
////        UpdateObjectRequest updateObject = new UpdateObjectRequest(UpdateField.NAME, "test");
////        Long id = 1L;
////
////        User user = new User(1L, "test", "testa", "test123456", "test@gmail.com", null, null);
////        Board board = new Board(1L, user, "title", "description", null, null, null);
////
////        Type type = new Type();
////        type.setBoard(board);
////        type.setName("type");
////        type.setDescription("description");
////
////        Status status = new Status();
////        status.setBoard(board);
////        status.setName("status");
////        status.setDescription("description");
////
////        Item item = new Item(board, status, type, "section", null, LocalDateTime.now(), 1, null, null);
////        item.setUser(user);
////
////        given(itemService.update(updateObject, id)).willReturn(item);
////
////        assertEquals(200, sharedContentFacade.update(updateObject, id, Item.class).getStatusCode());
////    }
//
////    @Test
////    @DisplayName("Test on update comment when it does not exist in the db")
////    void updateComment_Success() throws NoSuchFieldException {
////        UpdateObjectRequest updateObject = new UpdateObjectRequest(UpdateField.NAME, "test");
////        Long id = 1L;
////
////        User user = new User(1L, "test", "testa", "test123456", "test@gmail.com", null, null);
////        Board board = new Board(1L, user, "title", "description", null, null, null);
////
////        Type type = new Type();
////        type.setBoard(board);
////        type.setName("type");
////        type.setDescription("description");
////
////        Status status = new Status();
////        status.setBoard(board);
////        status.setName("status");
////        status.setDescription("description");
////
////        Item item = new Item(board, status, type, "section", null, LocalDateTime.now(), 1, null, null);
////        item.setUser(user);
////
////        Comment comment = new Comment();
////        comment.setUser(user);
////        comment.setParentItem(item);
////
////        given(commentService.update(updateObject, id)).willReturn(comment);
////
////        assertEquals(200, sharedContentFacade.update(updateObject, id, Comment.class).getStatusCode());
////    }
//
//    @Test
//    @DisplayName("Test on update comment when it does not exist in the db")
//    void updateComment_idNull() throws NoSuchFieldException {
//        UpdateObjectRequest updateObject = new UpdateObjectRequest(UpdateField.NAME, "test");
//        Long id = 1L;
//
//        given(commentService.update(updateObject, id)).willThrow(NullPointerException.class);
//
//        assertEquals(500, sharedContentFacade.update(updateObject, id, Comment.class).getStatusCode());
//    }
//
//    @Test
//    @DisplayName("Test on update comment when it does not exist in the db")
//    void updateItem_idNull() throws NoSuchFieldException {
//        UpdateObjectRequest updateObject = new UpdateObjectRequest(UpdateField.NAME, "test");
//        Long id = 1L;
//
//        given(itemService.update(updateObject, id)).willThrow(NullPointerException.class);
//
//        assertEquals(500, sharedContentFacade.update(updateObject, id, Item.class).getStatusCode());
//    }
//
////    @Test
////    @DisplayName("Test validate ID")
////    void getComment_testValidateId() {
////        User user = new User(1L, "test", "testa", "test123456", "test@gmail.com", null, null);
////        Board board = new Board(1L, user, "title", "description", null, null, null);
////
////        Type type = new Type();
////        type.setBoard(board);
////        type.setName("type");
////        type.setDescription("description");
////
////        Status status = new Status();
////        status.setBoard(board);
////        status.setName("status");
////        status.setDescription("description");
////
////        Item item = new Item(board, status, type, "section", null, LocalDateTime.now(), 1, null, null);
////        item.setUser(user);
////
////        Comment comment = new Comment();
////        comment.setUser(user);
////        comment.setParentItem(item);
////
////        given(commentService.get(1L)).willReturn(comment);
////        assertEquals(200, sharedContentFacade.get(1L, Comment.class).getStatusCode());
////    }
//
////    @Test
////    @DisplayName("Test validate ID")
////    void getItem_testValidateId() {
////        User user = new User(1L, "test", "testa", "test123456", "test@gmail.com", null, null);
////        Board board = new Board(1L, user, "title", "description", null, null, null);
////
////        Type type = new Type();
////        type.setBoard(board);
////        type.setName("type");
////        type.setDescription("description");
////
////        Status status = new Status();
////        status.setBoard(board);
////        status.setName("status");
////        status.setDescription("description");
////
////        Item item = new Item(board, status, type, "section", null, LocalDateTime.now(), 1, null, null);
////        item.setUser(user);
////
////        given(itemService.get(1L)).willReturn(item);
////        assertEquals(200, sharedContentFacade.get(1L, Item.class).getStatusCode());
////    }
//
//    @Test
//    @DisplayName("illegal id given to test the get function")
//    void getItem_IllegalArgumentBeThrown() {
//        given(itemService.get(1L)).willThrow(IllegalArgumentException.class);
//        assertEquals(400, sharedContentFacade.get(1L, Item.class).getStatusCode());
//    }
//
//    @Test
//    @DisplayName("illegal id given to test the get function")
//    void getComment_IllegalArgumentBeThrown() {
//        given(commentService.get(1L)).willThrow(IllegalArgumentException.class);
//        assertEquals(400, sharedContentFacade.get(1L, Comment.class).getStatusCode());
//    }
//
//    @Test
//    @DisplayName("null id given to test the get function")
//    void getItem_NullExceptionBeThrown() {
//        given(itemService.get(1L)).willThrow(NullPointerException.class);
//        assertEquals(500, sharedContentFacade.get(1L, Item.class).getStatusCode());
//    }
//
//    @Test
//    @DisplayName("null id given to test the get function")
//    void getComment_NullExceptionBeThrown() {
//        given(commentService.get(1L)).willThrow(NullPointerException.class);
//        assertEquals(500, sharedContentFacade.get(1L, Comment.class).getStatusCode());
//    }
//}
