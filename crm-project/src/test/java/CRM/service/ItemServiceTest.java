package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.CommentRequest;
import CRM.entity.requests.ItemRequest;
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
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private StatusRepository statusRepository;
    @Mock
    private TypeRepository typeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private ItemService itemService;

    private ItemRequest itemRequest;


//    @Test
//    @DisplayName("Test create method with user not found")
//    void testCreateWithUserNotFound() {
//        ItemRequest itemRequest = new ItemRequest();
//        itemRequest.setBoardId(1L);
//        itemRequest.setTypeId(1L);
//        itemRequest.setStatusId(1L);
//        itemRequest.setUserId(1L);
//        itemRequest.setTitle("Test Title");
//        itemRequest.setDescription("Test Description");
//        itemRequest.setImportance(1);
//        given(userRepository.findById(1L)).willReturn(Optional.empty());
//        assertThrows(AccountNotFoundException.class, () -> itemService.create(itemRequest));
//    }

//    @Test
//    @DisplayName("Test create method with parent item id not found")
//    void testCreateWithParentItemNotFound() {
//        ItemRequest itemRequest = new ItemRequest();
//        itemRequest.setBoardId(1L);
//        itemRequest.setTypeId(1L);
//        itemRequest.setStatusId(1L);
//        itemRequest.setUserId(1L);
//        itemRequest.setTitle("Test Title");
//        itemRequest.setDescription("Test Description");
//        itemRequest.setImportance(1);
//        itemRequest.setParentItemId(1L);
//        assertThrows(NoSuchElementException.class, () -> itemService.create(itemRequest));
//    }

    @Test
    @DisplayName("Test create method with board id not found")
    void testCreateWithBoardNotFound() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setTypeId(1L);
        itemRequest.setStatusId(1L);
        itemRequest.setUserId(1L);
        itemRequest.setTitle("Test Title");
        itemRequest.setDescription("Test Description");
        itemRequest.setImportance(1);
        User user = new User();
        user.setId(1L);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        assertThrows(NoSuchElementException.class, () -> itemService.create(itemRequest));
    }

    @Test
    @DisplayName("Test delete method with delete all items and comments")
    void testDeleteWithDeleteAllItemsAndComments() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        Item item1 = new Item();
        item1.setId(1L);
        Comment comment1 = new Comment();
        comment1.setParentItem(item1);
        Comment comment2 = new Comment();
        comment2.setParentItem(item1);
        Item item2 = new Item();
        item2.setId(2L);
        Comment comment3 = new Comment();
        comment3.setParentItem(item2);
        Item item3 = new Item();
        item3.setId(3L);
        given(itemRepository.findById(1L)).willReturn(java.util.Optional.of(item1));
        given(itemRepository.findById(2L)).willReturn(java.util.Optional.of(item2));
        given(itemRepository.findById(3L)).willReturn(java.util.Optional.of(item3));
        int result = itemService.delete(ids);
        assertEquals(3, result);
    }

    @Test
    @DisplayName("Test update method with primitive or known object field")
    void testUpdateWithPrimitiveOrKnownObjectField() throws NoSuchFieldException {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        updateObject.setFieldName(UpdateField.DESCRIPTION);
        updateObject.setContent("New description");
        Item item = new Item();
        item.setId(1L);
        item.setDescription("Old description");
        given(itemRepository.findById(1L)).willReturn(Optional.of(item));
        given(itemRepository.save(item)).willReturn(item);
        assertNotNull(itemService.update(updateObject, 1L));
    }

    @Test
    @DisplayName("Test update method with invalid comment id")
    void testUpdateWithInvalidCommentId() {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        updateObject.setFieldName(UpdateField.DESCRIPTION);
        updateObject.setContent("Updated descr");
        given(itemRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> itemService.update(updateObject, 1L));
    }

    @Test
    @DisplayName("Test update method with null input")
    void testUpdateWithNullInput() {
        UpdateObjectRequest updateObject = null;
        Item item = new Item();
        item.setId(1L);
        given(itemRepository.findById(1L)).willReturn(Optional.of(item));
        assertThrows(NullPointerException.class, () -> itemService.update(updateObject, 1L));
    }

    @Test
    @DisplayName("Test update method with null field name")
    void testUpdateWithNullFieldName() {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        updateObject.setFieldName(null);
        updateObject.setContent("Updated");
        Item item = new Item();
        item.setId(1L);
        given(itemRepository.findById(1L)).willReturn(Optional.of(item));
        assertThrows(NullPointerException.class, () -> itemService.update(updateObject, 1L));
    }

    @Test
    @DisplayName("Test get method with valid input")
    void testGetWithValidInput() {
        Item item = new Item();
        item.setId(1L);
        given(itemRepository.findById(1L)).willReturn(Optional.of(item));
        Item result = itemService.get(1L);
        assertEquals(item, result);
    }

    @Test
    @DisplayName("Test get method with invalid input")
    void testGetWithInvalidInput() {
        given(itemRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> itemService.get(1L));
    }

    @Test
    @DisplayName("Test getAllInItem method with valid input")
    void testGetAllInItemWithValidInput() {
        Item item = new Item();
        item.setId(1L);
        Item item2 = new Item();
        item2.setId(2L);
        item2.setParentItem(item);
        List<Item> items = new ArrayList<>(Arrays.asList(item,item2));
        given(itemRepository.findById(1L)).willReturn(Optional.of(item));
        given(itemRepository.findAllByParentItem(item)).willReturn(items);
        List<SharedContent> result = itemService.getAllInItem(1L);
        assertEquals(items, result);
    }

    @Test
    @DisplayName("Test getAllInItem method with invalid input")
    void testGetAllInItemWithInvalidInput() {
        given(itemRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> itemService.getAllInItem(1L));
    }


//    @Test
//    @DisplayName("Test getAllInBoard method")
//    void testGetAllInBoard() {
//        Board board = new Board();
//        board.setId(1L);
//        List<Item> items = new ArrayList<>();
//        Item item1 = new Item();
//        item1.setId(1L);
//        item1.setBoard(board);
//        Item item2 = new Item();
//        item2.setId(2L);
//        item2.setBoard(board);
//        items.add(item1);
//        items.add(item2);
//        given(boardRepository.findById(1L)).willReturn(java.util.Optional.of(board));
//        given(itemRepository.findAllByBoard(board)).willReturn(items);
//        List<Item> result = itemService.getAllInBoard(1L);
//        assertNotNull(result);
//    }
//
//    @Test
//    @DisplayName("Test getAllInBoard method with non-existent board")
//    void testGetAllInBoardWithNonExistentBoard() {
//        given(boardRepository.findById(1L)).willReturn(Optional.empty());
//        assertThrows(NoSuchElementException.class, () -> itemService.getAllInBoard(1L));
//    }
}