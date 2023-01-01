package CRM.controller.facades;

import CRM.entity.Item;
import CRM.entity.Section;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.service.SectionService;
import CRM.utils.enums.UpdateField;
import com.google.api.client.http.HttpStatusCodes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SectionFacadeTest {

    @Mock
    private SectionService sectionService;

    @InjectMocks
    private SectionFacade sectionFacade;

    @Test
    @DisplayName("Test create section success")
    public void create_ValidInput_Success() {
        AttributeRequest sectionRequest = new AttributeRequest();
        sectionRequest.setBoardId(1L);
        sectionRequest.setName("name");
        sectionRequest.setDescription("desc");
        Item item = (Item) Item.createSharedContentItemForTests();
        Set<Item> itemList = new HashSet<>();
        itemList.add(item);
        Set<Section> setSection = new HashSet<>();
        Section section = mock(Section.class);
        given(section.getDescription()).willReturn("desc");
        given(section.getItems()).willReturn(itemList);
        given(section.getId()).willReturn(1L);
        given(section.getName()).willReturn("name");
        setSection.add(section);

        given(sectionService.create(sectionRequest, 1L)).willReturn(setSection);

        assertEquals(HttpStatusCodes.STATUS_CODE_CREATED, sectionFacade.create(sectionRequest, 1L).getStatusCode());
    }

    @Test
    @DisplayName("Test create section with invalid name")
    public void create_InvalidName_ThrowsIllegalArgumentException() {
        AttributeRequest sectionRequest = new AttributeRequest();
        sectionRequest.setBoardId(1L);
        sectionRequest.setName("Invalid name @!@#123");

        assertThrows(IllegalArgumentException.class, () -> sectionFacade.create(sectionRequest, 1L));
    }

    @Test
    @DisplayName("Test create section with null board id")
    public void create_NullBoardId_ThrowsNullPointerException() {
        AttributeRequest sectionRequest = new AttributeRequest();

        assertThrows(NullPointerException.class, () -> sectionFacade.create(sectionRequest, null));
    }

    @Test
    @DisplayName("Test create section with board not found")
    public void create_SectionNotFoundInDB_ThrowsNoSuchElementException() {
        AttributeRequest sectionRequest = new AttributeRequest();
        sectionRequest.setBoardId(1L);
        sectionRequest.setName("name");
        sectionRequest.setDescription("desc");

        given(sectionService.create(sectionRequest, 1L)).willThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> sectionFacade.create(sectionRequest, 1L));
    }

    @Test
    @DisplayName("delete section success")
    public void delete_ValidInput_Success() {
        long boardId = 1L;
        long sectionId = 2L;

        doNothing().when(sectionService).delete(boardId, sectionId);

        assertEquals(HttpStatusCodes.STATUS_CODE_NO_CONTENT, sectionFacade.delete(boardId, sectionId).getStatusCode());
    }

    @Test
    @DisplayName("delete section with invalid input board id")
    public void delete_InvalidInput_BoardId() {
        long boardId = -1L;
        long sectionId = 2L;

        assertThrows(IllegalArgumentException.class, () -> sectionFacade.delete(boardId, sectionId));
    }

    @Test
    @DisplayName("delete section with non-existent board id")
    public void delete_SectionNotFoundInDB_ThrowsNoSuchElementException() {
        long boardId = 3L;
        long sectionId = 2L;

        doThrow(new NoSuchElementException()).when(sectionService).delete(boardId, sectionId);

        assertThrows(NoSuchElementException.class, () -> sectionFacade.delete(boardId, sectionId));
    }

    @Test
    @DisplayName("delete section with null input board id")
    public void delete_NullInput_ThrowsNullPointerException() {
        Long boardId = null;
        long sectionId = 2L;

        assertThrows(NullPointerException.class, () -> sectionFacade.delete(boardId, sectionId));
    }

    @Test
    @DisplayName("get section success")
    public void get_ValidInput_Success() {
        long boardId = 1L;
        long sectionId = 2L;
        Section section = mock(Section.class);
        given(section.getDescription()).willReturn("desc");
        Item item = (Item) Item.createSharedContentItemForTests();
        Set<Item> itemList = new HashSet<>();
        itemList.add(item);

        given(section.getItems()).willReturn(itemList);
        given(section.getId()).willReturn(1L);
        given(section.getName()).willReturn("name");
        given(sectionService.get(sectionId, boardId)).willReturn(section);

        assertEquals(HttpStatusCodes.STATUS_CODE_OK, sectionFacade.get(sectionId, boardId).getStatusCode());
    }

    @Test
    @DisplayName("get section invalid input")
    public void get_InvalidInput_ThrowsIllegalArgumentException() {
        long boardId = 1L;
        long sectionId = -2L;

        assertThrows(IllegalArgumentException.class, () -> sectionFacade.get(sectionId, boardId));
    }

    @Test
    @DisplayName("get section not found")
    public void get_SectionNotFoundInDB_ThrowsNoSuchElementException() {
        long boardId = 1L;
        long sectionId = 2L;

        given(sectionService.get(sectionId, boardId)).willThrow(new NoSuchElementException());

        assertThrows(NoSuchElementException.class, () -> sectionFacade.get(sectionId, boardId));
    }

    @Test
    @DisplayName("get section with null board id throws null pointer")
    public void get_SectionNullBoardId_ThrowsNullPointerException() {
        Long boardId = null;
        long sectionId = 2L;

        assertThrows(NullPointerException.class, () -> sectionFacade.get(sectionId, boardId));
    }

    @Test
    @DisplayName("get all sections in board success")
    public void getAllSectionsInBoard_Success() {
        long boardId = 1L;
        List<Section> sections = new ArrayList<>();
        Section section = mock(Section.class);
        given(section.getDescription()).willReturn("desc");
        Item item = (Item) Item.createSharedContentItemForTests();
        Set<Item> itemList = new HashSet<>();
        itemList.add(item);

        given(section.getItems()).willReturn(itemList);
        given(section.getId()).willReturn(1L);
        given(section.getName()).willReturn("name");
        sections.add(section);
        given(sectionService.getAllSectionsInBoard(boardId)).willReturn(sections);

        assertEquals(HttpStatusCodes.STATUS_CODE_OK, sectionFacade.getAllSectionsInBoard(boardId).getStatusCode());
    }

    @Test
    @DisplayName("get all sections in board invalid input")
    public void getAllSectionsInBoard_InvalidInput_ThrowsIllegalArgumentException() {
        long boardId = -1L;

        assertThrows(IllegalArgumentException.class, () -> sectionFacade.getAllSectionsInBoard(boardId));
    }

    @Test
    @DisplayName("get all sections in board where no section found")
    public void getAllSectionsInBoard_BoardNotFoundInDB_ThrowsNoSuchElementException() {
        long boardId = 1L;

        given(sectionService.getAllSectionsInBoard(boardId)).willThrow(new NoSuchElementException());

        assertThrows(NoSuchElementException.class, () -> sectionFacade.getAllSectionsInBoard(boardId));
    }

    @Test
    @DisplayName("get all sections in board null pointer")
    public void getAllSectionsInBoard_NullBoardId_ThrowsNullPointerException() {
        Long boardId = null;

        assertThrows(NullPointerException.class, () -> sectionFacade.getAllSectionsInBoard(boardId));
    }

    @Test
    @DisplayName("get filtered sections success")
    public void getFilteredItems_Success() {
        Map<String, List<String>> filters = new HashMap<>();
        filters.put("name", Arrays.asList("Section 1", "Section 2"));
        long boardId = 1L;
        Set<Section> sections = new HashSet<>();
        Section section = mock(Section.class);
        given(section.getDescription()).willReturn("desc");
        Item item = (Item) Item.createSharedContentItemForTests();
        Set<Item> itemList = new HashSet<>();
        itemList.add(item);

        given(section.getItems()).willReturn(itemList);
        given(section.getId()).willReturn(1L);
        given(section.getName()).willReturn("name");

        sections.add(section);

        given(sectionService.getFilteredItems(filters, boardId)).willReturn(sections);

        assertEquals(HttpStatusCodes.STATUS_CODE_OK, sectionFacade.getFilteredItems(filters, boardId).getStatusCode());
    }

    @Test
    @DisplayName("get filtered sections with invalid board id")
    public void getFilteredItems_InvalidBoardId_ThrowsIllegalArgumentException() {
        Map<String, List<String>> filters = new HashMap<>();
        filters.put("invalid", List.of("Section 3"));
        long boardId = -1L;

        assertThrows(IllegalArgumentException.class, () -> sectionFacade.getFilteredItems(filters, boardId));
    }

    @Test
    @DisplayName("get filtered sections with null board id retrieved null pointer exception")
    public void getFilteredItems_NullBoardId_ThrowsNullPointerException() {
        Long boardId = null;

        assertThrows(NullPointerException.class, () -> sectionFacade.getFilteredItems(null, boardId));
    }

    @Test
    @DisplayName("update section success")
    public void update_ValidInput_Success() throws NoSuchFieldException {
        long boardId = 1L;
        long sectionId = 2L;
        UpdateObjectRequest updateItemRequest = new UpdateObjectRequest();
        updateItemRequest.setFieldName(UpdateField.NAME);
        updateItemRequest.setContent("new name");
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardSectionIds(boardId, sectionId);
        updateItemRequest.setObjectsIdsRequest(objIds);

        Section section = mock(Section.class);
        given(section.getDescription()).willReturn("desc");
        Item item = (Item) Item.createSharedContentItemForTests();
        Set<Item> itemList = new HashSet<>();
        itemList.add(item);

        given(section.getItems()).willReturn(itemList);
        given(section.getId()).willReturn(1L);
        given(section.getName()).willReturn("name");
        given(sectionService.update(updateItemRequest)).willReturn(section);

        assertEquals(HttpStatusCodes.STATUS_CODE_OK, sectionFacade.update(updateItemRequest).getStatusCode());
    }

    @Test
    @DisplayName("update section with invalid boardId throws illegal argument")
    public void update_InvalidBoardId_ThrowsIllegalArgumentException() {
        long boardId = -1L;
        long sectionId = 2L;
        UpdateObjectRequest updateItemRequest = new UpdateObjectRequest();
        updateItemRequest.setFieldName(UpdateField.NAME);
        updateItemRequest.setContent("new name");
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardSectionIds(boardId, sectionId);
        updateItemRequest.setObjectsIdsRequest(objIds);

        assertThrows(IllegalArgumentException.class, () -> sectionFacade.update(updateItemRequest));
    }

    @Test
    @DisplayName("update section with null boardId throws null pointer exception")
    public void update_NullBoardId_ThrowsNullPointerException() {
        Long boardId = null;
        long sectionId = 2L;
        UpdateObjectRequest updateItemRequest = new UpdateObjectRequest();
        updateItemRequest.setFieldName(UpdateField.NAME);
        updateItemRequest.setContent("new name");
        ObjectsIdsRequest objIds = ObjectsIdsRequest.boardSectionIds(boardId, sectionId);
        updateItemRequest.setObjectsIdsRequest(objIds);

        assertThrows(NullPointerException.class, () -> sectionFacade.update(updateItemRequest));
    }
}