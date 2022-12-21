package CRM.service;

import CRM.entity.*;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private StatusRepository statusRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TypeRepository typeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SectionRepository sectionRepository;

    @InjectMocks
    private ItemService itemService;


    @Test
    @DisplayName("Test create method with user not found")
    void testCreateWithUserNotFound() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setSectionId(1L);
        itemRequest.setTypeId(1L);
        itemRequest.setStatusId(1L);
        itemRequest.setUserId(1L);
        itemRequest.setTitle("Test Title");
        itemRequest.setDescription("Test Description");
        itemRequest.setImportance(1);
        given(userRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> itemService.create(itemRequest));
    }

    @Test
    @DisplayName("Test create method with parent item id not found")
    void testCreateWithParentItemNotFound() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setSectionId(1L);
        itemRequest.setTypeId(1L);
        itemRequest.setStatusId(1L);
        itemRequest.setUserId(1L);
        itemRequest.setTitle("Test Title");
        itemRequest.setDescription("Test Description");
        itemRequest.setImportance(1);
        itemRequest.setParentItemId(1L);
        assertThrows(NoSuchElementException.class, () -> itemService.create(itemRequest));
    }

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
    @DisplayName("Test delete method")
    void testDelete() {
        List<Long> ids = Arrays.asList(1L, 2L);
        Item item1 = new Item();
        item1.setId(1L);
        Item item2 = new Item();
        item2.setId(2L);
        Comment comment1 = new Comment();
        comment1.setId(3L);
        comment1.setParentItem(item1);
        Comment comment2 = new Comment();
        comment2.setId(4L);
        comment2.setParentItem(item2);
        given(itemRepository.findById(1L)).willReturn(Optional.of(item1));
        given(itemRepository.findById(2L)).willReturn(Optional.of(item2));
        doNothing().when(commentRepository).deleteAllByParentItem(item1);
        doNothing().when(commentRepository).deleteAllByParentItem(item2);
        doNothing().when(itemRepository).deleteAllById(ids);
        int result = itemService.delete(ids);
        assertEquals(2, result);
        verify(commentRepository, times(1)).deleteAllByParentItem(item1);
        verify(commentRepository, times(1)).deleteAllByParentItem(item2);
        verify(itemRepository, times(1)).deleteAllById(ids);
    }

    @Test
    @DisplayName("Test create method is null")
    void testCreateReturnsNull() throws AccountNotFoundException {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setParentItemId(1L);
        itemRequest.setUserId(1L);
        itemRequest.setSectionId(1L);
        itemRequest.setTypeId(1L);
        itemRequest.setStatusId(1L);
        itemRequest.setTitle("Test Title");
        itemRequest.setDescription("Test Description");
        itemRequest.setImportance(1);
        Item parentItem = new Item();
        parentItem.setId(1L);
        User user = new User();
        user.setId(1L);
        Section section = new Section();
        section.setId(1L);
        Type type = new Type();
        type.setId(1L);
        Status status = new Status();
        status.setId(1L);
        given(itemRepository.findById(1L)).willReturn(Optional.of(parentItem));
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(sectionRepository.findById(1L)).willReturn(Optional.of(section));
        given(typeRepository.findById(1L)).willReturn(Optional.of(type));
        given(statusRepository.findById(1L)).willReturn(Optional.of(status));
        Item result = itemService.create(itemRequest);
        assertNull(result);
    }


    @Test
    @DisplayName("Test update method with Custom object status")
    void testUpdateWithCustomObjectStatus() throws NoSuchFieldException {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        updateObject.setFieldName(UpdateField.STATUS);
        updateObject.setContent(1);
        Status status = new Status();
        Item item = new Item();
        item.setId(1L);
        item.setStatus(status);
        item.setDescription("Old description");
        given(itemRepository.findById(1L)).willReturn(Optional.of(item));
        given(statusRepository.findById(1L)).willReturn(Optional.of(status));
        given(itemRepository.save(item)).willReturn(item);
        assertNotNull(itemService.update(updateObject, 1L));
    }

    @Test
    @DisplayName("Test update method with Custom object due_date")
    void testUpdateWithCustomObjectDueDate() throws NoSuchFieldException {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        updateObject.setFieldName(UpdateField.DUE_DATE);
        updateObject.setContent(1);
        Item item = new Item();
        item.setId(1L);
        given(itemRepository.findById(1L)).willReturn(Optional.of(item));
        given(itemRepository.save(item)).willReturn(item);
        assertNotNull(itemService.update(updateObject, 1L));
    }


    @Test
    @DisplayName("Test update method with Custom object type")
    void testUpdateWithCustomObjectType() throws NoSuchFieldException {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        updateObject.setFieldName(UpdateField.TYPE);
        updateObject.setContent(1);
        Type type = new Type();
        Item item = new Item();
        item.setId(1L);
        item.setType(type);
        item.setDescription("Old description");
        given(itemRepository.findById(1L)).willReturn(Optional.of(item));
        given(typeRepository.findById(1L)).willReturn(Optional.of(type));
        given(itemRepository.save(item)).willReturn(item);
        assertNotNull(itemService.update(updateObject, 1L));
    }

    @Test
    @DisplayName("Test update method with Custom object item")
    void testUpdateWithCustomObjectParentItem() throws NoSuchFieldException {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        updateObject.setFieldName(UpdateField.PARENT_ITEM);
        updateObject.setContent(2);
        Item parentItem = new Item();
        parentItem.setId(2L);
        Item item = new Item();
        item.setId(1L);
        item.setParentItem(parentItem);
        item.setDescription("Old description");
        given(itemRepository.findById(1L)).willReturn(Optional.of(item));
        given(itemRepository.findById(2L)).willReturn(Optional.of(parentItem));
        given(itemRepository.save(item)).willReturn(item);
        assertNotNull(itemService.update(updateObject, 1L));
    }

    @Test
    @DisplayName("Test update method with Custom object section")
    void testUpdateWithCustomObjectSection() throws NoSuchFieldException {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        updateObject.setFieldName(UpdateField.SECTION);
        updateObject.setContent(1);
        Section section = new Section();
        section.setId(2L);
        Item item = new Item();
        item.setId(1L);
        item.setSection(section);
        item.setDescription("Old description");
        given(itemRepository.findById(1L)).willReturn(Optional.of(item));
        given(sectionRepository.findById(1L)).willReturn(Optional.of(section));
        given(itemRepository.save(item)).willReturn(item);
        assertNotNull(itemService.update(updateObject, 1L));
    }

    @Test
    @DisplayName("Test update method with Custom object item cant be his own parent item")
    void testUpdateWithCustomObjectItemHisOwnParentItemThrowsException() throws NoSuchFieldException {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        updateObject.setFieldName(UpdateField.PARENT_ITEM);
        updateObject.setContent(1);
        Item parentItem = new Item();
        parentItem.setId(2L);
        Item item = new Item();
        item.setId(1L);
        item.setParentItem(parentItem);
        item.setDescription("Old description");
        given(itemRepository.findById(1L)).willReturn(Optional.of(item));
        given(itemRepository.findById(1L)).willReturn(Optional.of(parentItem));
        assertThrows(IllegalArgumentException.class, () -> itemService.update(updateObject, 1L));
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


    @Test
    @DisplayName("Test getAllInBoard method")
    void testGetAllInBoard() {
        Section section = new Section();
        section.setId(1L);
        List<Item> items = new ArrayList<>();
        Item item1 = new Item();
        item1.setId(1L);
        item1.setSection(section);
        Item item2 = new Item();
        item2.setId(2L);
        item2.setSection(section);
        items.add(item1);
        items.add(item2);
        given(sectionRepository.findById(1L)).willReturn(Optional.of(section));
        given(itemRepository.findAllBySection(section)).willReturn(items);
        List<Item> result = itemService.getAllInSection(1L);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Test getAllInBoard method with non-existent board")
    void testGetAllInBoardWithNonExistentBoard() {
//        given(boardRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> itemService.getAllInSection(1L));
    }
}