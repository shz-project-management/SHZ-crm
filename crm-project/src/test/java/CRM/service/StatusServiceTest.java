package CRM.service;

import CRM.entity.Attribute;
import CRM.entity.Board;
import CRM.entity.Status;
import CRM.repository.BoardRepository;
import CRM.repository.StatusRepository;
import org.hibernate.NonUniqueObjectException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("StatusService create method tests")
class StatusServiceTest {

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private StatusService statusService;

    private Board board;

    private Status status;

    private Long statusId;

    @BeforeEach
    void setUp() {
        board = new Board();
        board.setId(1L);
        statusId = 1L;
        status = new Status();
        status.setId(statusId);
    }

    /***
     * create method tests:
     */

    @Test
    @DisplayName("Should create and return new status when name is unique for board")
    void testCreateUniqueStatus() {
        // Set up test data and mock behavior
        Attribute statusAttribute = Attribute.createAttribute(board, "Test Status", "Test description");
        Status expectedStatus = Status.createStatus(statusAttribute);
        when(statusRepository.existsByBoardAndNameLike(any(), any())).thenReturn(false);
        when(statusRepository.save(any())).thenReturn(expectedStatus);

        // Call method under test
        Status actualStatus = statusService.create(statusAttribute);

        // Verify results
        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    @DisplayName("Should throw NonUniqueObjectException when name is not unique for board")
    void testCreateNonUniqueStatus() {
        // Set up test data and mock behavior
        Attribute statusAttribute = Attribute.createAttribute(board, "Test Status", "Test description");
        when(statusRepository.existsByBoardAndNameLike(any(), any())).thenReturn(true);

        // Call method under test and verify exception is thrown
        assertThrows(NonUniqueObjectException.class, () -> statusService.create(statusAttribute));
    }

    @Test
    @DisplayName("Should throw NullPointerException when creating a null status")
    public void testCreateWithNullStatusThrowsException() {
        assertThrows(NullPointerException.class, () -> statusService.create(null));
    }

    /***
     * delete method tests:
     */

    @Test
    @DisplayName("Should delete status and return true when status exists")
    void testDeleteExistingStatus() {
        // Set up test data and mock behavior
        when(statusRepository.findById(statusId)).thenReturn(Optional.of(status));
        doNothing().when(statusRepository).delete(status);

        // Call method under test
        boolean result = statusService.delete(statusId);

        // Verify results
        assertTrue(result);
    }

    @Test
    @DisplayName("Should throw NoSuchElementException when status does not exist")
    void testDeleteNonExistentStatus() {
        // Set up test data and mock behavior
        when(statusRepository.findById(statusId)).thenReturn(Optional.empty());

        // Call method under test and verify exception is thrown
        assertThrows(NoSuchElementException.class, () -> statusService.delete(statusId));
    }

    /***
     * get method tests:
     */

    @Test
    @DisplayName("Should return status when status exists")
    void testGetExistingStatus() {
        // Set up test data and mock behavior
        Status expectedStatus = new Status();
        expectedStatus.setId(statusId);
        when(statusRepository.findById(statusId)).thenReturn(Optional.of(expectedStatus));

        // Call method under test
        Status actualStatus = statusService.get(statusId);

        // Verify results
        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    @DisplayName("Should throw NoSuchElementException when status does not exist")
    void testGetNonExistentStatus() {
        // Set up test data and mock behavior
        when(statusRepository.findById(statusId)).thenReturn(Optional.empty());

        // Call method under test and verify exception is thrown
        assertThrows(NoSuchElementException.class, () -> statusService.get(statusId));
    }

    /***
     * getAll method tests:
     */

    @Test
    @DisplayName("Should return all statuses")
    void testGetAllStatuses() {
        // Set up test data and mock behavior
        List<Status> expectedStatuses = List.of(
                new Status(),
                new Status()
        );
        when(statusRepository.findAll()).thenReturn(expectedStatuses);

        // Call method under test
        List<Status> actualStatuses = statusService.getAll();

        // Verify results
        assertEquals(expectedStatuses, actualStatuses);
    }

    /***
     * getAllInBoard method tests:
     */

    @Test
    @DisplayName("Should return all statuses in board when board exists")
    void testGetAllStatusesInExistingBoard() {
        // Set up test data and mock behavior
        List<Status> expectedStatuses = List.of(
                new Status(),
                new Status()
        );
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));
        when(statusRepository.findByBoard(board)).thenReturn(expectedStatuses);

        // Call method under test
        List<Status> actualStatuses = statusService.getAllInBoard(board.getId());

        // Verify results
        assertEquals(expectedStatuses, actualStatuses);
    }

    @Test
    @DisplayName("Should throw NoSuchElementException when board does not exist")
    void testGetAllStatusesInNonExistentBoard() {
        // Set up test data and mock behavior
        Long nonExistentBoardId = 2L;
        when(boardRepository.findById(nonExistentBoardId)).thenReturn(Optional.empty());

        // Call method under test and verify exception is thrown
        assertThrows(NoSuchElementException.class, () -> statusService.getAllInBoard(nonExistentBoardId));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when board id is invalid")
    void testGetAllStatusesWithInvalidBoardId() {
        // Set up test data and mock behavior
        Long invalidBoardId = -1L;

        // Call method under test and verify exception is thrown
        assertThrows(IllegalArgumentException.class, () -> statusService.getAllInBoard(invalidBoardId));
    }
}