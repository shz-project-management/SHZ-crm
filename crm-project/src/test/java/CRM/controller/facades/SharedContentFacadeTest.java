package CRM.controller.facades;

import CRM.entity.*;
import CRM.entity.requests.*;
import CRM.service.*;
import CRM.utils.NotificationSender;
import CRM.utils.enums.UpdateField;
import com.google.api.client.http.HttpStatusCodes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class SharedContentFacadeTest {

    @Mock
    private ItemService itemService;
    @Mock
    private CommentService commentService;
    @Mock
    private BoardService boardService;
    @Mock
    private UserService userService;
    @Mock
    private SettingsService settingsService;

    @Mock
    private NotificationSender notificationSender;

    @InjectMocks
    private SharedContentFacade sharedContentFacade;


    @Test
    @DisplayName("Test create item method with valid input")
    void create_ItemWithValidInput_Success() throws AccountNotFoundException {
        ItemRequest testItem = new ItemRequest(1L, 1L, 1L, 1L, LocalDateTime.now());
        testItem.setName("name");
        testItem.setSectionId(1L);

        given(itemService.create(testItem)).willReturn(Section.createSection(new AttributeRequest(1L, "name", "desc")));

        assertEquals(HttpStatusCodes.STATUS_CODE_CREATED, sharedContentFacade.create(testItem).getStatusCode());
    }

    @Test
    @DisplayName("Test create item method with invalid board input throws illegal argument")
    void create_ItemWithInValidBoardInput_ThrowsIllegalArgumentException() {
        ItemRequest testItem = new ItemRequest(1L, 1L, -1L, 1L, LocalDateTime.now());
        testItem.setName("name");
        testItem.setSectionId(1L);

        assertThrows(IllegalArgumentException.class, () -> sharedContentFacade.create(testItem));
    }

    @Test
    @DisplayName("Test create item method with invalid user input throws illegal argument")
    void create_ItemWithInValidUserInput_ThrowsIllegalArgumentException() {
        ItemRequest testItem = new ItemRequest(1L, 1L, 1L, -1L, LocalDateTime.now());
        testItem.setName("name");
        testItem.setSectionId(1L);

        assertThrows(IllegalArgumentException.class, () -> sharedContentFacade.create(testItem));
    }

    @Test
    @DisplayName("Test create item method with invalid user input throws null pointer")
    void create_ItemWithNullUserInput_ThrowsNullPointerException() {
        ItemRequest testItem = new ItemRequest(1L, 1L, 1L, null, LocalDateTime.now());
        testItem.setName("name");
        testItem.setSectionId(1L);

        assertThrows(NullPointerException.class, () -> sharedContentFacade.create(testItem));
    }

//    @Test
//    @DisplayName("Test create comment method with valid input")
//    public void create_CommentWithValidInput_Success() throws AccountNotFoundException {
//        long userId = 1;
//        long boardId = 2;
//        long parentItemId = 3;
//        String description = "Test comment";
//        long sectionId = 4;
//        String name = "name";
//
//        CommentRequest comment = mock(CommentRequest.class);
//        given(comment.getParentItemId()).willReturn(parentItemId);
//        given(comment.getDescription()).willReturn(description);
//        given(comment.getSectionId()).willReturn(sectionId);
//        given(comment.getName()).willReturn(name);
//
//
//        Set<Comment> comments = new HashSet<>();
//        given(commentService.create(comment, userId, boardId)).willReturn(comments);
//
//        User user = mock(User.class);
//        given(userService.get(userId)).willReturn(user);
//
//        Board board = mock(Board.class);
//        given(boardService.get(boardId)).willReturn(board);
//        given(board.getBoardUsersSet()).willReturn(new HashSet<>());
//
//        assertEquals(HttpStatusCodes.STATUS_CODE_CREATED, sharedContentFacade.create(comment, userId, boardId).getStatusCode());
//    }

    @Test
    @DisplayName("Test create comment method with null input throws null pointer")
    public void create_CommentWithNullInput_ThrowsNulPointerException() {
        long userId = 1;
        long boardId = 2;
        CommentRequest comment = mock(CommentRequest.class);

        assertThrows(NullPointerException.class, () -> sharedContentFacade.create(comment, userId, boardId));
    }

    @Test
    @DisplayName("Test create comment method with invalid input throws illegal argument")
    public void create_CommentWithInvalidInput_ThrowsIllegalArgumentException() {
        long userId = -1;
        long boardId = 2;
        long parentItemId = 3;
        String description = "Test comment";
        long sectionId = 4;
        String name = "name";

        CommentRequest comment = new CommentRequest();
        comment.setParentItemId(parentItemId);
        comment.setDescription(description);
        comment.setSectionId(sectionId);
        comment.setName(name);

        assertThrows(IllegalArgumentException.class, () -> sharedContentFacade.create(comment, userId, boardId));
    }


    @Test
    @DisplayName("Test that delete returns correct response when input is valid")
    void delete_testValidInput_Success() throws AccountNotFoundException {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        given(commentService.delete(ids, 1L)).willReturn(3);
        given(boardService.get(1L)).willReturn(new Board());

        assertEquals(HttpStatusCodes.STATUS_CODE_NO_CONTENT, sharedContentFacade.delete(ids, 1L, Comment.class).getStatusCode());
    }

    @Test
    @DisplayName("Test validate update comment")
    void updateItem_testValidateId_Success() throws NoSuchFieldException, AccountNotFoundException {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        ObjectsIdsRequest objectRequest = ObjectsIdsRequest.boardSectionItemIds(1L, 1L, 1L);
        objectRequest.setUpdateObjId(1L);
        updateObject.setObjectsIdsRequest(objectRequest);

        given(commentService.update(updateObject)).willReturn(Section.createSection(new AttributeRequest(1L, "name", "desc")));

        assertEquals(HttpStatusCodes.STATUS_CODE_OK, sharedContentFacade.update(updateObject, Comment.class).getStatusCode());
    }

    @Test
    @DisplayName("Test invalid updateObj ID throws illegal argument")
    void updateComment_InvalidUpdateObjId_ThrowsIllegalArgumentException() {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        ObjectsIdsRequest objectRequest = ObjectsIdsRequest.boardSectionItemIds(1L, 1L, -1L);
        objectRequest.setUpdateObjId(-1L);
        updateObject.setObjectsIdsRequest(objectRequest);

        assertThrows(IllegalArgumentException.class, () -> sharedContentFacade.update(updateObject, Item.class));
    }

    @Test
    @DisplayName("Test invalid updateObj ID throws Account Not Found Exception")
    void updateComment_InvalidFieldInput_ThrowsAccountNotFoundException() throws NoSuchFieldException, AccountNotFoundException {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        ObjectsIdsRequest objectRequest = ObjectsIdsRequest.boardSectionItemIds(1L, 1L, -1L);
        objectRequest.setUserId(1L);
        objectRequest.setUpdateObjId(1L);
        updateObject.setObjectsIdsRequest(objectRequest);
        updateObject.setFieldName(UpdateField.STATUS);
        Section section = mock(Section.class);
        given(section.getDescription()).willReturn("desc");
        Item item = (Item) Item.createSharedContentItemForTests();
        Set<Item> itemList = new HashSet<>();
        itemList.add(item);

        given(section.getItems()).willReturn(itemList);
        given(section.getId()).willReturn(1L);
        given(section.getName()).willReturn("name");
        given(itemService.update(updateObject)).willReturn(section);
        given(userService.get(updateObject.getObjectsIdsRequest().getUserId())).willThrow(AccountNotFoundException.class);

        assertThrows(AccountNotFoundException.class, () -> sharedContentFacade.update(updateObject, Item.class));
    }


    @Test
    @DisplayName("Test null update obj Id throws null pointer")
    void updateComment_NullObjId_NullPointerException() {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        ObjectsIdsRequest objectRequest = ObjectsIdsRequest.boardSectionItemIds(1L, 1L, 1L);
        objectRequest.setUpdateObjId(null);

        assertThrows(NullPointerException.class, () -> sharedContentFacade.update(updateObject, Item.class));
    }

    @Test
    @DisplayName("bad request, field not found to update")
    void updateComment_FieldInputNotExists_ThrowsNoSuchField() throws NoSuchFieldException {
        UpdateObjectRequest updateObject = new UpdateObjectRequest();
        ObjectsIdsRequest objectRequest = ObjectsIdsRequest.boardSectionItemIds(1L, 1L, 1L);
        objectRequest.setUpdateObjId(1L);
        updateObject.setObjectsIdsRequest(objectRequest);

        given(itemService.update(updateObject)).willThrow(NoSuchFieldException.class);

        assertThrows(NoSuchFieldException.class, () -> sharedContentFacade.update(updateObject, Item.class));
    }

    @Test
    @DisplayName("Test update item status method with valid input")
    public void update_ItemStatusValidInput_Success() throws AccountNotFoundException, NoSuchFieldException {
        long userId = 1;
        long boardId = 2;
        long updateObjId = 3;
        String content = "done";
        long sectionId = 4;

        ObjectsIdsRequest objectsIdsRequest = mock(ObjectsIdsRequest.class);
        given(objectsIdsRequest.getUserId()).willReturn(userId);
        given(objectsIdsRequest.getBoardId()).willReturn(boardId);
        given(objectsIdsRequest.getUpdateObjId()).willReturn(updateObjId);
        given(objectsIdsRequest.getSectionId()).willReturn(sectionId);

        UpdateObjectRequest updateObject = mock(UpdateObjectRequest.class);
        given(updateObject.getObjectsIdsRequest()).willReturn(objectsIdsRequest);
        given(updateObject.getFieldName()).willReturn(UpdateField.STATUS);
        given(updateObject.getContent()).willReturn(content);

        User user = mock(User.class);
        given(userService.get(userId)).willReturn(user);

        Board board = mock(Board.class);
        given(boardService.get(boardId)).willReturn(board);
        given(board.getBoardUsersSet()).willReturn(new HashSet<>());

        Item item = (Item) Item.createSharedContentItemForTests();
        Set<Item> itemList = new HashSet<>();
        itemList.add(item);

        Section section = mock(Section.class);
        given(section.getDescription()).willReturn("desc");
        given(section.getItems()).willReturn(itemList);
        given(section.getId()).willReturn(1L);
        given(section.getName()).willReturn("name");

        given(itemService.update(updateObject)).willReturn(section);

        assertEquals(HttpStatusCodes.STATUS_CODE_OK, sharedContentFacade.update(updateObject, Item.class).getStatusCode());
    }

    @Test
    @DisplayName("Test update item type method with valid input")
    public void update_ItemTypeValidInput_Success() throws AccountNotFoundException, NoSuchFieldException {
        long userId = 1;
        long boardId = 2;
        long updateObjId = 3;
        String content = "bug";
        long sectionId = 4;

        ObjectsIdsRequest objectsIdsRequest = mock(ObjectsIdsRequest.class);
        given(objectsIdsRequest.getUserId()).willReturn(userId);
        given(objectsIdsRequest.getBoardId()).willReturn(boardId);
        given(objectsIdsRequest.getUpdateObjId()).willReturn(updateObjId);
        given(objectsIdsRequest.getSectionId()).willReturn(sectionId);

        UpdateObjectRequest updateObject = mock(UpdateObjectRequest.class);
        given(updateObject.getObjectsIdsRequest()).willReturn(objectsIdsRequest);
        given(updateObject.getFieldName()).willReturn(UpdateField.TYPE);
        given(updateObject.getContent()).willReturn(content);

        User user = mock(User.class);
        given(userService.get(userId)).willReturn(user);

        Board board = mock(Board.class);
        given(boardService.get(boardId)).willReturn(board);
        given(board.getBoardUsersSet()).willReturn(new HashSet<>());

        Item item = (Item) Item.createSharedContentItemForTests();
        Set<Item> itemList = new HashSet<>();
        itemList.add(item);

        Section section = mock(Section.class);
        given(section.getDescription()).willReturn("desc");
        given(section.getItems()).willReturn(itemList);
        given(section.getId()).willReturn(1L);
        given(section.getName()).willReturn("name");

        given(itemService.update(updateObject)).willReturn(section);

        assertEquals(HttpStatusCodes.STATUS_CODE_OK, sharedContentFacade.update(updateObject, Item.class).getStatusCode());
    }

    @Test
    @DisplayName("Test update item name method with valid input")
    public void update_ItemNameValidInput_Success() throws AccountNotFoundException, NoSuchFieldException {
        long userId = 1;
        long boardId = 2;
        long updateObjId = 3;
        String content = "new name";
        long sectionId = 4;

        ObjectsIdsRequest objectsIdsRequest = mock(ObjectsIdsRequest.class);
        given(objectsIdsRequest.getUserId()).willReturn(userId);
        given(objectsIdsRequest.getBoardId()).willReturn(boardId);
        given(objectsIdsRequest.getUpdateObjId()).willReturn(updateObjId);
        given(objectsIdsRequest.getSectionId()).willReturn(sectionId);

        UpdateObjectRequest updateObject = mock(UpdateObjectRequest.class);
        given(updateObject.getObjectsIdsRequest()).willReturn(objectsIdsRequest);
        given(updateObject.getFieldName()).willReturn(UpdateField.NAME);
        given(updateObject.getContent()).willReturn(content);

        User user = mock(User.class);
        given(userService.get(userId)).willReturn(user);

        Board board = mock(Board.class);
        given(boardService.get(boardId)).willReturn(board);
        given(board.getBoardUsersSet()).willReturn(new HashSet<>());

        Item item = (Item) Item.createSharedContentItemForTests();
        Set<Item> itemList = new HashSet<>();
        itemList.add(item);

        Section section = mock(Section.class);
        given(section.getDescription()).willReturn("desc");
        given(section.getItems()).willReturn(itemList);
        given(section.getId()).willReturn(1L);
        given(section.getName()).willReturn("name");

        given(itemService.update(updateObject)).willReturn(section);

        assertEquals(HttpStatusCodes.STATUS_CODE_OK, sharedContentFacade.update(updateObject, Item.class).getStatusCode());
    }

    @Test
    @DisplayName("valid input given to test the get function")
    void getItem_ValidInput_Success() {
        ObjectsIdsRequest objectRequest = ObjectsIdsRequest.boardSectionItemIds(1L, 1L, 1L);
        objectRequest.setUpdateObjId(1L);

        given(itemService.get(objectRequest)).willReturn(SharedContent.createSharedContentItemForTests());

        assertEquals(HttpStatusCodes.STATUS_CODE_OK, sharedContentFacade.get(objectRequest, Item.class).getStatusCode());
    }

    @Test
    @DisplayName("illegal id given to test the get function")
    void getItem_InvalidInputSectionId_ThrowsIllegalArgumentException() {
        ObjectsIdsRequest objectRequest = ObjectsIdsRequest.boardSectionItemIds(1L, -1L, 1L);
        objectRequest.setUpdateObjId(1L);

        assertThrows(IllegalArgumentException.class, () -> sharedContentFacade.get(objectRequest, Item.class));
    }

    @Test
    @DisplayName("null given to test the get function throws null pointer")
    void getItem_NullBoardId_ThrowsNullPointerException() {
        ObjectsIdsRequest objectRequest = ObjectsIdsRequest.boardSectionItemIds(null, 1L, 1L);
        objectRequest.setUpdateObjId(1L);

        assertThrows(NullPointerException.class, () -> sharedContentFacade.get(objectRequest, Item.class));
    }

    @Test
    @DisplayName("get all items in section Valid input")
    void getAllItemInSection_ValidInput_Success() {
        ObjectsIdsRequest objectRequest = ObjectsIdsRequest.boardSectionItemIds(1L, 1L, 1L);
        objectRequest.setUpdateObjId(1L);
        Set<Item> items = new HashSet<>();
        items.add((Item) Item.createSharedContentItemForTests());
        items.add((Item) Item.createSharedContentItemForTests());

        given(itemService.getAllInSection(objectRequest)).willReturn(items);

        assertEquals(HttpStatusCodes.STATUS_CODE_OK, sharedContentFacade.getAllItemsInSection(objectRequest).getStatusCode());
    }

    @Test
    @DisplayName("get all item in section - invalid section id input")
    void getAllItemInSection_InvalidInputSectionId_ThrowsIllegalArgumentException() {
        ObjectsIdsRequest objectRequest = ObjectsIdsRequest.boardSectionItemIds(1L, -1L, 1L);
        objectRequest.setUpdateObjId(1L);

        assertThrows(IllegalArgumentException.class, () ->  sharedContentFacade.getAllItemsInSection(objectRequest));
    }

    @Test
    @DisplayName("get all item in section - null board input throws null pointer")
    void getAllItemInSection_NullBoardInput_ThrowsNullPointerException() {
        ObjectsIdsRequest objectRequest = ObjectsIdsRequest.boardSectionItemIds(null, 1L, 1L);
        objectRequest.setUpdateObjId(1L);

        assertThrows(NullPointerException.class, () ->  sharedContentFacade.getAllItemsInSection(objectRequest));
    }


    @Test
    @DisplayName("Test get all in item method with invalid item ID throws illegal argument")
    public void getAllInItem_InvalidItemId_ThrowsIllegalArgumentException() throws NoSuchElementException {
        long itemId = -1;
        long sectionId = 2;
        long boardId = 3;
        ObjectsIdsRequest objectsIdsRequest = ObjectsIdsRequest.boardSectionItemIds(boardId, sectionId, itemId);

        assertThrows(IllegalArgumentException.class, () -> sharedContentFacade.getAllInItem(objectsIdsRequest, Comment.class));
    }

    @Test
    @DisplayName("Test get all in item method with invalid section ID throws illegal argument")
    public void getAllInItem_InvalidSectionId_BadRequestResponse() throws NoSuchElementException {
        long itemId = 1;
        long sectionId = -1;
        long boardId = 3;
        ObjectsIdsRequest objectsIdsRequest = ObjectsIdsRequest.boardSectionItemIds(boardId, sectionId, itemId);

        assertThrows(IllegalArgumentException.class, () -> sharedContentFacade.getAllInItem(objectsIdsRequest, Comment.class));
    }

    @Test
    @DisplayName("Test get all in item method with null board ID throws null pointer")
    public void getAllInItem_NullBoardId_ThrowsNullPointerException() throws NoSuchElementException {
        long itemId = 1;
        long sectionId = 1;
        Long boardId = null;
        ObjectsIdsRequest objectsIdsRequest = ObjectsIdsRequest.boardSectionItemIds(boardId, sectionId, itemId);

        assertThrows(NullPointerException.class, () -> sharedContentFacade.getAllInItem(objectsIdsRequest, Comment.class));
    }

    @Test
    @DisplayName("Test assignToUser method with valid input and successful assignment")
    public void assignToUser_ValidInput_Success() throws AccountNotFoundException {
        long boardId = 1;
        long sectionId = 2;
        long updateObjId = 3;
        long assignedUserId = 3;

        ObjectsIdsRequest objIds = mock(ObjectsIdsRequest.class);
        given(objIds.getBoardId()).willReturn(boardId);
        given(objIds.getSectionId()).willReturn(sectionId);
        given(objIds.getUpdateObjId()).willReturn(updateObjId);
        given(objIds.getAssignedUserId()).willReturn(assignedUserId);


        Item item = (Item) Item.createSharedContentItemForTests();
        Set<Item> itemList = new HashSet<>();
        itemList.add(item);

        Section section = mock(Section.class);
        given(section.getDescription()).willReturn("desc");
        given(section.getItems()).willReturn(itemList);
        given(section.getId()).willReturn(1L);
        given(section.getName()).willReturn("name");
        given(itemService.assignToUser(objIds)).willReturn(section);

        assertEquals(HttpStatusCodes.STATUS_CODE_OK, sharedContentFacade.assignToUser(objIds, Item.class).getStatusCode());
    }

    @Test
    @DisplayName("Test assignToUser method with invalid email input throws illegal argument")
    public void assignToUser_InvalidEmailInput_ThrowsIllegalArgument() {
        long boardId = 1;
        long sectionId = 2;
        long updateObjId = 3;
        long assignedUserId = -3;

        ObjectsIdsRequest objIds = mock(ObjectsIdsRequest.class);
        given(objIds.getBoardId()).willReturn(boardId);
        given(objIds.getSectionId()).willReturn(sectionId);
        given(objIds.getUpdateObjId()).willReturn(updateObjId);
        given(objIds.getAssignedUserId()).willReturn(assignedUserId);

        assertThrows(IllegalArgumentException.class, () -> sharedContentFacade.assignToUser(objIds, Item.class));
    }

    @Test
    @DisplayName("Test assignToUser method with null input throws null pointer exception")
    public void assignToUser_NullUpdateObjIdInput_ThrowsNullPointerException() {
        long boardId = 1;
        long sectionId = 2;
        Long updateObjId = null;
        ObjectsIdsRequest objectsIdsRequest = ObjectsIdsRequest.boardSectionIds(boardId, sectionId);
        objectsIdsRequest.setUpdateObjId(updateObjId);

        assertThrows(NullPointerException.class, () -> sharedContentFacade.assignToUser(objectsIdsRequest, Item.class));
    }
}
