package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.CommentRequest;
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
class CommentServiceTest {

    @Mock
    private BoardRepository boardRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService;


    @Test
    @DisplayName("Test create method with user not found")
    void create_ValidInput_Success() throws AccountNotFoundException {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setParentItemId(1L);
        commentRequest.setSectionId(1L);
        commentRequest.setName("Test Title");
        commentRequest.setDescription("Test Description");
        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Set<Comment> comments = new LinkedHashSet<>();
        Comment expectedComment = new Comment();
        expectedComment.setAssignedToUserId(null);
        comments.add(expectedComment);
        commentRequest.setParentItemId(1L);

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

        Set<Comment> actualComments = commentService.create(commentRequest, 1L, 1L);
        assertEquals(actualComments, comments);
    }

    @Test
    @DisplayName("Test create method with user not found")
    void create_UserNotFound_ThrowsAccountNotFoundException() {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setParentItemId(1L);
        commentRequest.setSectionId(1L);
        commentRequest.setName("Test Title");
        commentRequest.setDescription("Test Description");
        commentRequest.setParentItemId(1L);
        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);

        given(boardRepository.findById(1L)).willReturn(Optional.of(expectedBoard));
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> commentService.create(commentRequest, 1L, 1L));
    }

    @Test
    @DisplayName("Test create method with invalid board id")
    void create_InvalidBoardId_ThrowsNoSuchElementException() {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setParentItemId(1L);
        commentRequest.setSectionId(1L);
        commentRequest.setName("Test Title");
        commentRequest.setDescription("Test Description");
        commentRequest.setParentItemId(1L);

        given(boardRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> commentService.create(commentRequest, 1L, 1L));
    }


    @Test
    @DisplayName("Test create method with null parent item id")
    void create_NullParentItemId_ThrowsNoSuchElementException() {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setParentItemId(null);
        commentRequest.setSectionId(1L);
        commentRequest.setName("Test Title");
        commentRequest.setDescription("Test Description");
        User user = new User();
        user.setId(1L);

        assertThrows(NoSuchElementException.class, () -> commentService.create(commentRequest, 1L, 1L));
    }

    @Test
    @DisplayName("Test delete method with valid input")
    void delete_ValidInput_Success() {
        List<Long> ids = List.of(1L);
        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);

        Set<Item> itemList = new HashSet<>();
        Set<Comment> commentsList = new HashSet<>();
        Comment comment1 = (Comment) Comment.createSharedContentCommentForTests();
        commentsList.add(comment1);
        Item item = (Item) Item.createSharedContentItemForTests();
        item.setId(1L);
        item.setComments(commentsList);
        itemList.add(item);

        given(boardRepository.findById(1L)).willReturn(Optional.of(expectedBoard));

        Set<Section> setSection = new HashSet<>();
        Section section = new Section();
        section.setDescription("desc");
        section.setItems(itemList);
        section.setId(1L);
        section.setName("name");
        setSection.add(section);

        expectedBoard.setSections(setSection);

        int result = commentService.delete(ids, 1L);
        assertEquals(1, result);
    }


    @Test
    @DisplayName("Test update method with valid comment id")
    void update_ValidCommentId_Success() throws NoSuchFieldException {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        updateObject.setFieldName(UpdateField.DESCRIPTION);
        updateObject.setContent("Updated desc");
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardSectionItemIds(1L, 1L, 1L);
        objIds.setUpdateObjId(1L);
        updateObject.setObjectsIdsRequest(objIds);

        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);

        Set<Item> itemList = new HashSet<>();
        Set<Comment> commentsList = new HashSet<>();
        Comment comment1 = (Comment) Comment.createSharedContentCommentForTests();
        commentsList.add(comment1);
        Item item = (Item) Item.createSharedContentItemForTests();
        item.setId(1L);
        item.setComments(commentsList);
        itemList.add(item);

        given(boardRepository.findById(1L)).willReturn(Optional.of(expectedBoard));

        Set<Section> setSection = new HashSet<>();
        Section section = new Section();
        section.setDescription("desc");
        section.setItems(itemList);
        section.setId(1L);
        section.setName("name");
        setSection.add(section);

        expectedBoard.setSections(setSection);

        assertNotNull(commentService.update(updateObject));
    }

    @Test
    @DisplayName("Test update method with invalid board id")
    void update_InvalidBoardId_ThrowsNoSuchElementException() {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        updateObject.setFieldName(UpdateField.DESCRIPTION);
        updateObject.setContent("Updated desc");
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardSectionItemIds(1L, 1L, 1L);
        objIds.setUpdateObjId(1L);
        updateObject.setObjectsIdsRequest(objIds);

        assertThrows(NoSuchElementException.class, () -> commentService.update(updateObject));
    }

    @Test
    @DisplayName("Test update method with invalid comment id")
    void update_InvalidCommentId_ThrowsNullPointerException() {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        updateObject.setFieldName(UpdateField.DESCRIPTION);
        updateObject.setContent("Updated desc");
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardSectionItemIds(1L, 1L, 1L);
        objIds.setUpdateObjId(null);
        updateObject.setObjectsIdsRequest(objIds);

        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);

        given(boardRepository.findById(1L)).willReturn(Optional.of(expectedBoard));

        assertThrows(NullPointerException.class, () -> commentService.update(updateObject));
    }

    @Test
    @DisplayName("Test update method with null field name")
    void update_NullFieldName_ThrowsIllegalArgumentException() {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        updateObject.setFieldName(null);
        updateObject.setContent("Updated desc");
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardSectionItemIds(1L, 1L, 1L);
        objIds.setUpdateObjId(1L);
        updateObject.setObjectsIdsRequest(objIds);

        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);

        given(boardRepository.findById(1L)).willReturn(Optional.of(expectedBoard));

        assertThrows(IllegalArgumentException.class, () -> commentService.update(updateObject));
    }

    @Test
    @DisplayName("Test get method with valid input")
    void get_ValidInput_Success() {
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardSectionItemIds(1L, 1L, 1L);
        objIds.setParentId(1L);
        objIds.setUpdateObjId(1L);
        objIds.setSearchId(1L);

        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);

        Set<Item> itemList = new HashSet<>();
        Set<Comment> commentsList = new HashSet<>();
        Item item = (Item) Item.createSharedContentItemForTests();
        Comment comment1 = (Comment) Comment.createSharedContentCommentForTests();
        comment1.setId(1L);
        comment1.setParentItem(item);
        commentsList.add(comment1);
        item.setId(1L);
        item.setComments(commentsList);
        itemList.add(item);

        given(boardRepository.findById(1L)).willReturn(Optional.of(expectedBoard));

        Set<Section> setSection = new HashSet<>();
        Section section = new Section();
        section.setDescription("desc");
        section.setItems(itemList);
        section.setId(1L);
        section.setName("name");
        setSection.add(section);

        expectedBoard.setSections(setSection);

        Comment result = (Comment) commentService.get(objIds);
        assertEquals(comment1, result);
    }

    @Test
    @DisplayName("Test get method with invalid input")
    void get_InvalidInput_ThrowsNoSuchElementException() {
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardSectionItemIds(1L, 1L, 1L);
        objIds.setParentId(1L);
        objIds.setUpdateObjId(1L);
        objIds.setSearchId(1L);

        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);

        given(boardRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> commentService.get(objIds));
    }

    @Test
    @DisplayName("Test getAllInItem method with valid input")
    void getAllInItem_ValidInput_Success() {
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardSectionItemIds(1L, 1L, 1L);

        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);

        Set<Item> itemList = new HashSet<>();
        Set<Comment> commentsList = new HashSet<>();
        Item item = (Item) Item.createSharedContentItemForTests();
        Comment comment1 = (Comment) Comment.createSharedContentCommentForTests();
        comment1.setId(1L);
        comment1.setParentItem(item);
        commentsList.add(comment1);
        item.setId(1L);
        item.setComments(commentsList);
        itemList.add(item);

        given(boardRepository.findById(1L)).willReturn(Optional.of(expectedBoard));

        Set<Section> setSection = new HashSet<>();
        Section section = new Section();
        section.setDescription("desc");
        section.setItems(itemList);
        section.setId(1L);
        section.setName("name");
        setSection.add(section);

        expectedBoard.setSections(setSection);

        assertEquals(commentsList, new HashSet<>(commentService.getAllInItem(objIds)));
    }

    @Test
    @DisplayName("Test getAllInItem method with invalid input")
    void getAllInItem_InvalidInput_Success() {
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardSectionItemIds(1L, 1L, 1L);

        given(boardRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> commentService.getAllInItem(objIds));
    }

    @Test
    @DisplayName("Test getAllInItem method with invalid input")
    void assignToUser_ValidInput_Success() {
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardSectionItemIds(1L, 1L, 1L);

        assertNull(commentService.assignToUser(objIds));
    }
}