package CRM.service;

import CRM.entity.Board;
import CRM.entity.Item;
import CRM.entity.Section;
import CRM.entity.User;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.requests.RegisterUserRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.repository.BoardRepository;
import CRM.utils.enums.UpdateField;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @Mock
    private BoardRepository boardRepository;
    @Mock
    private EntityManagerFactory entityManagerFactory;
    @Mock
    private EntityManager entityManager;


    @InjectMocks
    private SectionService sectionService;

    @Test
    @DisplayName("Test create method with valid input")
    void create_ValidInput_Success() {
        AttributeRequest sectionRequest = new AttributeRequest();
        sectionRequest.setBoardId(1L);
        sectionRequest.setName("Test Title");
        sectionRequest.setDescription("Test Description");
        sectionRequest.setBoardId(1L);

        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);

        given(boardRepository.findById(1L)).willReturn(Optional.of(expectedBoard));

        Set<Section> setSection = new HashSet<>();
        Section section = new Section();
        section.setDescription("desc");
        section.setId(1L);
        section.setName("name");
        setSection.add(section);

        expectedBoard.setSections(setSection);

        given(boardRepository.save(expectedBoard)).willReturn(expectedBoard);

        assertNotNull(sectionService.create(sectionRequest, 1L));
    }

    @Test
    @DisplayName("Test create method with invalid board input")
    void create_InvalidBoardInput_NoSuchElementException() {
        AttributeRequest sectionRequest = new AttributeRequest();
        sectionRequest.setBoardId(1L);
        sectionRequest.setName("Test Title");
        sectionRequest.setDescription("Test Description");
        sectionRequest.setBoardId(1L);

        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Shai Levi", "shai123456", "shai@gmail.com");
        User expectedUser = User.newUser(correctRegisterUserRequest);
        Board expectedBoard = Board.createBoard(expectedUser, "name", "desc");
        expectedBoard.setId(1L);

        given(boardRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> sectionService.create(sectionRequest, 1L));
    }

    @Test
    @DisplayName("Test delete method with valid input")
    void delete_ValidInput_Success() {
        Board board = new Board();
        board.setId(1L);
        Section section = new Section();
        section.setId(1L);
        Set<Section> sections = new HashSet<>();
        sections.add(section);
        board.setSections(sections);

        given(boardRepository.findById(1L)).willReturn(Optional.of(board));
        given(boardRepository.save(board)).willReturn(board);
        sectionService.delete(1L, 1L);

        assertTrue(board.getSections().isEmpty());
    }

    @Test
    @DisplayName("Test delete method with invalid board ID")
    void delete_InvalidBoardId_ThrowsNoSuchElementException() {
        long invalidBoardId = -1L;

        given(boardRepository.findById(invalidBoardId)).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> sectionService.delete(invalidBoardId, 1L));
    }

    @Test
    @DisplayName("Test get method with valid input")
    void get_ValidInput_ReturnsSection() {
        Board board = new Board();
        board.setId(1L);
        Section expectedSection = new Section();
        expectedSection.setId(1L);
        Set<Section> sections = new HashSet<>();
        sections.add(expectedSection);
        board.setSections(sections);

        given(boardRepository.findById(1L)).willReturn(Optional.of(board));

        assertEquals(expectedSection, sectionService.get(1L, 1L));
    }


    @Test
    @DisplayName("Test get method with invalid board ID")
    void get_InvalidBoardId_ThrowsNoSuchElementException() {
        long invalidBoardId = -1L;

        given(boardRepository.findById(invalidBoardId)).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> sectionService.get(1L, invalidBoardId));
    }

    @Test
    @DisplayName("Test getAllSectionsInBoard method with valid input")
    void getAllSectionsInBoard_ValidInput_ReturnsListOfSections() {
        Board board = new Board();
        board.setId(1L);
        Section section1 = new Section();
        section1.setId(1L);
        Section section2 = new Section();
        section2.setId(2L);
        Set<Section> sections = new HashSet<>();
        sections.add(section1);
        sections.add(section2);
        board.setSections(sections);

        given(boardRepository.findById(1L)).willReturn(Optional.of(board));

        List<Section> expectedSections = Arrays.asList(section1, section2);
        assertEquals(expectedSections.size(), sectionService.getAllSectionsInBoard(1L).size());
    }

    @Test
    @DisplayName("Test getAllSectionsInBoard method with invalid board ID")
    void getAllSectionsInBoard_InvalidBoardId_ThrowsNoSuchElementException() {
        long invalidBoardId = -1L;

        given(boardRepository.findById(invalidBoardId)).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> sectionService.getAllSectionsInBoard(invalidBoardId));
    }

    @Test
    @DisplayName("Test update method with valid input")
    void update_ValidInput_ReturnsUpdatedSection() throws NoSuchFieldException {
        Board board = new Board();
        board.setId(1L);
        Section section = new Section();
        section.setId(1L);
        section.setName("Old name");
        section.setDescription("Old description");
        Set<Section> sections = new HashSet<>();
        sections.add(section);
        board.setSections(sections);

        UpdateObjectRequest updateObjectRequest = new UpdateObjectRequest();
        updateObjectRequest.setFieldName(UpdateField.NAME);
        updateObjectRequest.setContent("New name");
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        objectsIdsRequest.setBoardId(1L);
        objectsIdsRequest.setSectionId(1L);
        updateObjectRequest.setObjectsIdsRequest(objectsIdsRequest);

        given(boardRepository.findById(1L)).willReturn(Optional.of(board));
        given(boardRepository.save(board)).willReturn(board);

        Section updatedSection = sectionService.update(updateObjectRequest);
        assertEquals("New name", updatedSection.getName());
        assertEquals("Old description", updatedSection.getDescription());
    }

    @Test
    @DisplayName("Test update method with invalid board ID")
    void update_InvalidBoardId_ThrowsNoSuchElementException() {
        long invalidBoardId = -1L;

        UpdateObjectRequest updateObjectRequest = new UpdateObjectRequest();
        updateObjectRequest.setFieldName(UpdateField.NAME);
        updateObjectRequest.setContent("New name");
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        objectsIdsRequest.setBoardId(invalidBoardId);
        objectsIdsRequest.setSectionId(1L);
        updateObjectRequest.setObjectsIdsRequest(objectsIdsRequest);

        given(boardRepository.findById(invalidBoardId)).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> sectionService.update(updateObjectRequest));
    }
}