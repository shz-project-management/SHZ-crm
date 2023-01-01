package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.ItemRequest;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.requests.RegisterUserRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.repository.BoardRepository;
import CRM.repository.UserRepository;
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
    private BoardRepository boardRepository;
    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private ItemService itemService;


    @Test
    @DisplayName("Test create method with valid input")
    void create_ValidInput_Success() throws AccountNotFoundException {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setBoardId(1L);
        itemRequest.setTypeId(1L);
        itemRequest.setStatusId(1L);
        itemRequest.setUserId(1L);
        itemRequest.setName("Test Title");
        itemRequest.setDescription("Test Description");
        itemRequest.setSectionId(1L);
        itemRequest.setUserId(1L);
        itemRequest.setBoardId(1L);
        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);

        given(boardRepository.findById(1L)).willReturn(Optional.of(expectedBoard));
        given(userRepository.findById(1L)).willReturn(Optional.of(expectedUser));

        Set<Item> itemList = new HashSet<>();
        Item item = (Item) Item.createSharedContentItemForTests();
        item.setId(1L);
        itemList.add(item);

        Set<Section> setSection = new HashSet<>();
        Section section = new Section();
        section.setDescription("desc");
        section.setItems(itemList);
        section.setId(1L);
        section.setName("name");
        setSection.add(section);

        expectedBoard.setSections(setSection);

        given(boardRepository.save(expectedBoard)).willReturn(expectedBoard);

        assertEquals(section, itemService.create(itemRequest));
    }


    @Test
    @DisplayName("Test create method with user not found")
    void create_UserNotFound_ThrowsAccountNotFoundException() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setBoardId(1L);
        itemRequest.setTypeId(1L);
        itemRequest.setStatusId(1L);
        itemRequest.setUserId(1L);
        itemRequest.setName("Test Title");
        itemRequest.setDescription("Test Description");
        itemRequest.setSectionId(1L);
        itemRequest.setUserId(1L);
        itemRequest.setBoardId(1L);

        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);

        given(boardRepository.findById(1L)).willReturn(Optional.of(expectedBoard));
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> itemService.create(itemRequest));
    }

    @Test
    @DisplayName("Test create method with board id not found")
    void create_BoardNotFound_ThrowsNoSuchElementException() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setBoardId(1L);
        itemRequest.setTypeId(1L);
        itemRequest.setStatusId(1L);
        itemRequest.setUserId(1L);
        itemRequest.setName("Test Title");
        itemRequest.setDescription("Test Description");
        itemRequest.setSectionId(1L);
        itemRequest.setUserId(1L);
        itemRequest.setBoardId(1L);

        given(boardRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> itemService.create(itemRequest));
    }

    @Test
    @DisplayName("Test delete method with delete all items and comments")
    void delete_ValidInput_Success() {
        List<Long> ids = List.of(1L);
        Item item1 = (Item) Item.createSharedContentItemForTests();
        item1.setId(1L);
        Set<Comment> comments = new HashSet<>();
        Comment comment1 = (Comment) Comment.createSharedContentCommentForTests();
        comment1.setParentItem(item1);
        comments.add(comment1);

        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);

        given(boardRepository.findById(1L)).willReturn(Optional.of(expectedBoard));

        Set<Item> itemList = new HashSet<>();
        Item item = (Item) Item.createSharedContentItemForTests();
        item.setId(1L);
        item.setComments(comments);
        itemList.add(item);

        Set<Section> setSection = new HashSet<>();
        Section section = new Section();
        section.setDescription("desc");
        section.setItems(itemList);
        section.setId(1L);
        section.setName("name");
        setSection.add(section);

        expectedBoard.setSections(setSection);

        int result = itemService.delete(ids, 1L);
        assertEquals(1, result);
    }

    @Test
    @DisplayName("Test delete method with invalid board id")
    void delete_BoardNotFound_ThrowsNoSuchElementException() {
        List<Long> ids = List.of(1L);

        given(boardRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> itemService.delete(ids, 1L));
    }

    @Test
    @DisplayName("Test update method with primitive or known object field")
    void update_ValidObjectField_Success() throws NoSuchFieldException {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        updateObject.setFieldName(UpdateField.SECTION);
        updateObject.setContent(1);
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardSectionIds(1L, 1L);
        objIds.setUpdateObjId(1L);
        updateObject.setObjectsIdsRequest(objIds);

        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);

        given(boardRepository.findById(1L)).willReturn(Optional.of(expectedBoard));

        Set<Item> itemList = new HashSet<>();
        Item item = (Item) Item.createSharedContentItemForTests();
        item.setId(1L);
        itemList.add(item);

        Set<Section> setSection = new HashSet<>();
        Section section = new Section();
        section.setDescription("desc");
        section.setItems(itemList);
        section.setId(1L);
        section.setName("name");
        setSection.add(section);

        expectedBoard.setSections(setSection);

        assertNotNull(itemService.update(updateObject));
    }

    @Test
    @DisplayName("Test update method with primitive or known object field")
    void update_ValidPrimitiveOrKnownObjectField_Success() throws NoSuchFieldException {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        updateObject.setFieldName(UpdateField.DESCRIPTION);
        updateObject.setContent("New description");
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardSectionIds(1L, 1L);
        objIds.setUpdateObjId(1L);
        updateObject.setObjectsIdsRequest(objIds);

        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);

        given(boardRepository.findById(1L)).willReturn(Optional.of(expectedBoard));

        Set<Item> itemList = new HashSet<>();
        Item item = (Item) Item.createSharedContentItemForTests();
        item.setId(1L);
        itemList.add(item);

        Set<Section> setSection = new HashSet<>();
        Section section = new Section();
        section.setDescription("desc");
        section.setItems(itemList);
        section.setId(1L);
        section.setName("name");
        setSection.add(section);

        expectedBoard.setSections(setSection);

        assertNotNull(itemService.update(updateObject));
    }

    @Test
    @DisplayName("Test update method with invalid board id")
    void update_BoardNotFound_NoSuchElementException() {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        updateObject.setFieldName(UpdateField.DESCRIPTION);
        updateObject.setContent("New description");
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardSectionIds(1L, 1L);
        objIds.setUpdateObjId(1L);
        updateObject.setObjectsIdsRequest(objIds);

        given(boardRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> itemService.update(updateObject));
    }

    @Test
    @DisplayName("Test get method with valid input")
    void get_ValidInput_Success() {
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardSectionIds(1L, 1L);
        objIds.setSearchId(1L);

        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);

        Set<Item> itemList = new HashSet<>();
        Item item = (Item) Item.createSharedContentItemForTests();
        item.setId(1L);
        itemList.add(item);

        Set<Section> setSection = new HashSet<>();
        Section section = new Section();
        section.setDescription("desc");
        section.setItems(itemList);
        section.setId(1L);
        section.setName("name");
        setSection.add(section);

        expectedBoard.setSections(setSection);

        given(boardRepository.findById(1L)).willReturn(Optional.of(expectedBoard));

        Item result = (Item) itemService.get(objIds);
        assertEquals(item, result);
    }

    @Test
    @DisplayName("Test get method with invalid board id input")
    void get_InvalidBoardInput_ThrowsNoSuchElementException() {
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardSectionIds(1L, 1L);
        objIds.setSearchId(1L);

        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);

        given(boardRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> itemService.get(objIds));
    }

    @Test
    @DisplayName("Test getAllInItem method with valid input")
    void getAllInItem_ValidInput_Success() {
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardSectionIds(1L, 1L);
        objIds.setSearchId(1L);

        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);

        Set<Item> itemList = new HashSet<>();
        Item item = (Item) Item.createSharedContentItemForTests();
        item.setId(1L);
        itemList.add(item);

        Set<Section> setSection = new HashSet<>();
        Section section = new Section();
        section.setDescription("desc");
        section.setItems(itemList);
        section.setId(1L);
        section.setName("name");
        setSection.add(section);

        expectedBoard.setSections(setSection);

        given(boardRepository.findById(1L)).willReturn(Optional.of(expectedBoard));

        List<Item> result = itemService.getAllInItem(objIds);
        assertEquals(itemList, new HashSet<>(result));
    }

    @Test
    @DisplayName("Test getAllInItem method with invalid board input")
    void getAllInItem_InvalidBoardInput_ThrowsNoSuchElementException() {
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardSectionIds(1L, 1L);
        objIds.setSearchId(1L);

        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);

        given(boardRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> itemService.getAllInItem(objIds));
    }


    @Test
    @DisplayName("Test getAllInSection method with valid input")
    void getAllInSection_ValidInput_Success() {
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardSectionIds(1L, 1L);
        objIds.setSearchId(1L);

        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);

        Set<Item> itemList = new HashSet<>();
        Item item = (Item) Item.createSharedContentItemForTests();
        item.setId(1L);
        itemList.add(item);

        Set<Section> setSection = new HashSet<>();
        Section section = new Section();
        section.setDescription("desc");
        section.setItems(itemList);
        section.setId(1L);
        section.setName("name");
        setSection.add(section);

        expectedBoard.setSections(setSection);

        given(boardRepository.findById(1L)).willReturn(Optional.of(expectedBoard));

        Set<Item> result = itemService.getAllInSection(objIds);
        assertEquals(itemList, result);
    }

    @Test
    @DisplayName("Test getAllInSection method with invalid board input")
    void getAllInSection_InvalidBoardInput_ThrowsNoSuchElementException() {
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardSectionIds(1L, 1L);
        objIds.setSearchId(1L);

        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);

        given(boardRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> itemService.getAllInSection(objIds));
    }

    @Test
    @DisplayName("Test assignToUser method with valid input")
    void assignToUser_ValidInput_Success() {
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardSectionIds(1L, 1L);
        objIds.setSearchId(1L);
        objIds.setAssignedUserId(1L);
        objIds.setUpdateObjId(1L);

        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);

        Set<Item> itemList = new HashSet<>();
        Item item = (Item) Item.createSharedContentItemForTests();
        item.setId(1L);
        itemList.add(item);

        Set<Section> setSection = new HashSet<>();
        Section section = new Section();
        section.setDescription("desc");
        section.setItems(itemList);
        section.setId(1L);
        section.setName("name");
        setSection.add(section);

        expectedBoard.setSections(setSection);

        given(boardRepository.findById(1L)).willReturn(Optional.of(expectedBoard));
        given(userRepository.findById(1L)).willReturn(Optional.of(expectedUser));

        Section result = itemService.assignToUser(objIds);
        assertEquals(section, result);
    }
}