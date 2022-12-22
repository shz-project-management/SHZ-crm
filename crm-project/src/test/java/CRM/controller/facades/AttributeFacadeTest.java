package CRM.controller.facades;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import CRM.entity.Attribute;
import CRM.entity.Board;
import CRM.entity.DTO.AttributeDTO;
import CRM.entity.Status;
import CRM.entity.Type;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.response.Response;
import CRM.service.BoardService;
import CRM.service.StatusService;
import CRM.service.TypeService;
import CRM.utils.enums.ExceptionMessage;
import CRM.utils.enums.SuccessMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class AttributeFacadeTest {

    @Mock
    private StatusService statusService;
    @Mock
    private TypeService typeService;
    @Mock
    private BoardService boardService;

    @InjectMocks
    private AttributeFacade attributeFacade;

    /**
     * Create method tests for status
     */
    @Test
    @DisplayName("Should create status attribute and validate parameters")
    void testCreateForStatusClass() {
        // Create a test AttributeRequest object
        AttributeRequest StatusRequest = new AttributeRequest(1L, "Status Attribute", "Test Description");
        // Create test data
        Long id = 1L;
        Board board = new Board();
        board.setId(id);
        Status status = new Status();
        status.setId(id);
        status.setBoard(board);

        // Set up the mock StatusService to return the mock Attribute object when create is called
        when(statusService.create(any(Attribute.class))).thenReturn(status);

        // Call the create method and store the result in a Response object
        Response response = attributeFacade.create(StatusRequest, Status.class);

        // Assert that the response status code is 201 (CREATED)
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());

        // Cast the data to an AttributeDTO object
        AttributeDTO attributeDTO = (AttributeDTO) response.getData();

        // Assert that the AttributeDTO object has the correct name and description
        assertEquals(id, attributeDTO.getId());
    }

    @Test
    @DisplayName("Should return 400 when status name is illegal and validate message")
    void testCreateStatusWithInvalidNameInput() {
        // Create a test AttributeRequest object with an invalid name
        AttributeRequest request = new AttributeRequest(1L, "Test Attribute!", "Test Description");

        // Call the create method and store the result in a Response object
        Response response = attributeFacade.create(request, Status.class);

        // Assert that the response status code is 400 (BAD_REQUEST)
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());

        // Assert that the response message is "Invalid input: name"
        assertEquals("Could not approve the given information: Test Attribute!", response.getMessage());
    }

    @Test
    @DisplayName("Should throw 500 when board is null when trying to create a status")
    void testCreateStatusWithNullBoard() {
        // Set up test data
        AttributeRequest statusRequest = new AttributeRequest(null, "Test Attribute", "Test Description");

        // Call the create method with Status.class
        Response response = attributeFacade.create(statusRequest, Status.class);

        // Call method under test and verify exception is thrown
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode());
    }

    @Test
    @DisplayName("Should throw 500 when name is null when creating a status")
    void testCreateStatusWithNullName() {
        // Set up test data
        AttributeRequest statusRequest = new AttributeRequest(1L, null, "Test Description");

        // Call the create method with Status.class
        Response response = attributeFacade.create(statusRequest, Status.class);

        // Call method under test and verify exception is thrown
        assertEquals(500, response.getStatusCode());
    }

    @Test
    @DisplayName("Should throw 500 when name is empty")
    void testCreateStatusWithEmptyName() {
        // Set up test data
        AttributeRequest attributeRequest = new AttributeRequest(1L, "", "Test Description");
        Board board = new Board();
        board.setId(1L);
        given(boardService.get(attributeRequest.getBoardId())).willReturn(board);
        // Call the create method with Status.class
        Response response = attributeFacade.create(attributeRequest, Status.class);

        // Call method under test and verify exception is thrown
        assertEquals(500, response.getStatusCode());
    }

    /**
     * Create method tests for type
     */

    @Test
    @DisplayName("Should create status attribute and validate parameters")
    void testCreateForTypeClass() {
        // Create a test AttributeRequest object
        AttributeRequest StatusRequest = new AttributeRequest(1L, "Type Attribute", "Test Description");
        // Create test data
        Long id = 1L;
        Board board = new Board();
        board.setId(id);
        Type type = new Type();
        type.setId(id);
        type.setBoard(board);

        // Set up the mock StatusService to return the mock Attribute object when create is called
        when(typeService.create(any(Attribute.class))).thenReturn(type);

        // Call the create method and store the result in a Response object
        Response response = attributeFacade.create(StatusRequest, Type.class);

        // Assert that the response status code is 201 (CREATED)
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());

        // Cast the data to an AttributeDTO object
        AttributeDTO attributeDTO = (AttributeDTO) response.getData();

        // Assert that the AttributeDTO object has the correct name and description
        assertEquals(id, attributeDTO.getId());
    }

    @Test
    @DisplayName("Should return 400 when type name is illegal and validate message")
    void testCreateTypeWithInvalidNameInput() {
        // Create a test AttributeRequest object with an invalid name
        AttributeRequest request = new AttributeRequest(1L, "Test Attribute!", "Test Description");

        // Call the create method and store the result in a Response object
        Response response = attributeFacade.create(request, Type.class);

        // Assert that the response status code is 400 (BAD_REQUEST)
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());

        // Assert that the response message is "Invalid input: name"
        assertEquals("Could not approve the given information: Test Attribute!", response.getMessage());
    }

    @Test
    @DisplayName("Should throw 500 when board is null when trying to create a type")
    void testCreateTypeWithNullBoard() {
        // Set up test data
        AttributeRequest statusRequest = new AttributeRequest(null, "Test Attribute", "Test Description");

        // Call the create method with Status.class
        Response response = attributeFacade.create(statusRequest, Type.class);

        // Call method under test and verify exception is thrown
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode());
    }

    @Test
    @DisplayName("Should throw 500 when name is null when creating a type")
    void testCreateTypeWithNullName() {
        // Set up test data
        AttributeRequest statusRequest = new AttributeRequest(1L, null, "Test Description");

        // Call the create method with Status.class
        Response response = attributeFacade.create(statusRequest, Type.class);

        // Call method under test and verify exception is thrown
        assertEquals(500, response.getStatusCode());
    }

    @Test
    @DisplayName("Should throw 500 when name is empty")
    void testCreateTypeWithEmptyName() {
        // Set up test data
        AttributeRequest statusRequest = new AttributeRequest(1L, "", "Test Description");

        // Call the create method with Status.class
        Response response = attributeFacade.create(statusRequest, Type.class);

        // Call method under test and verify exception is thrown
        assertEquals(500, response.getStatusCode());
    }

    /**
     * Delete method for status and type
     */

    @Test
    @DisplayName("Test delete method with valid status input and successful deletion succeeds")
    public void testDeleteStatusSuccess() {
        // Set up test data
        Long id = 1L;
        AttributeRequest statusRequest = new AttributeRequest(id, "Test Attribute", "Test Description");
        attributeFacade.create(statusRequest, Status.class);

        // Call the method under test
        Response response = attributeFacade.delete(id, Status.class);

        // Verify the results
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode());
        assertEquals(SuccessMessage.DELETED.toString(), response.getMessage());
        verify(statusService).delete(1L);
    }

    @Test
    @DisplayName("Test delete method with valid status input but unsuccessful deletion")
    public void testDeleteStatusUnSuccess() {
        // Set up test data
        Long id = 123L;
        String errorMessage = "Status not found";

        // Set up mock behavior
        doThrow(new NoSuchElementException(errorMessage)).when(statusService).delete(id);

        // Call the method under test
        Response response = attributeFacade.delete(id, Status.class);
        assertEquals(400, response.getStatusCode());
        assertEquals(errorMessage, response.getMessage());

        // Verify the results
        verify(statusService).delete(id);
    }

    @Test
    @DisplayName("Test delete status method with null ID throws bad request")
    public void testDeleteStatusNullId() {
        // Call the method under test
        Response response = attributeFacade.delete(null, Status.class);

        // Verify the results
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals(ExceptionMessage.EMPTY_NOTNULL_FIELD.toString(), response.getMessage());
    }

    /**
     * type
     */

    @Test
    @DisplayName("Test delete method with valid type input and successful deletion succeeds")
    public void testDeleteTypeSuccess() {
        // Set up test data
        Long id = 1L;
        AttributeRequest statusRequest = new AttributeRequest(id, "Test Attribute", "Test Description");
        attributeFacade.create(statusRequest, Type.class);

        // Call the method under test
        Response response = attributeFacade.delete(id, Type.class);

        // Verify the results
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode());
        assertEquals(SuccessMessage.DELETED.toString(), response.getMessage());
        verify(typeService).delete(1L);
    }

    @Test
    @DisplayName("Test delete method with valid type input but unsuccessful deletion")
    public void testDeleteTypeUnSuccess() {
        // Set up test data
        Long id = 123L;
        String errorMessage = "Type not found";
        // Set up mock behavior
        doThrow(new NoSuchElementException(errorMessage)).when(typeService).delete(id);

        // Call the method under test
        Response response = attributeFacade.delete(id, Type.class);
        assertEquals(400, response.getStatusCode());
        assertEquals(errorMessage, response.getMessage());

        // Verify the results
        verify(typeService).delete(id);
    }

    @Test
    @DisplayName("Test delete type method with null ID throws bad request")
    public void testDeleteTypeNullId() {
        // Call the method under test
        Response response = attributeFacade.delete(null, Type.class);

        // Verify the results
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals(ExceptionMessage.EMPTY_NOTNULL_FIELD.toString(), response.getMessage());
    }

    @Test
    @DisplayName("Test delete method with null class type throws bad request")
    public void testDeleteNullClass() {
        // Call the method under test
        Response response = attributeFacade.delete(1L, null);

        // Verify the results
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
    }

    /**
     * Get method tests for status and type
     */

    @Test
    @DisplayName("Test get status method with valid input and successful retrieval")
    public void testGetStatusSuccess() {
        // Set up test data
        Long id = 1L;
        Status expectedStatus = new Status();
        expectedStatus.setId(id);
        Board board = new Board();
        board.setId(1L);
        expectedStatus.setBoard(board);
        expectedStatus.setName("Open");
        expectedStatus.setDescription("Task is open and ready to be worked on");

        // Set up mock behavior
        when(statusService.get(id)).thenReturn(expectedStatus);

        // Call the method under test
        Response response = attributeFacade.get(id, Status.class);

        // Verify the results
        assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    @DisplayName("Test get status method with valid input but unsuccessful retrieval")
    public void testGetStatusUnsuccess() {
        // Set up test data
        Long id = 1L;
        String errorMessage = "Status not found";

        // Set up mock behavior
        when(statusService.get(id)).thenThrow(new NoSuchElementException(errorMessage));
        Response response = attributeFacade.get(id, Status.class);

        // Call the method under test
        assertEquals(400, response.getStatusCode());
        assertEquals(errorMessage, response.getMessage());
    }

    @Test
    @DisplayName("Test get status method with null ID")
    public void testGetStatusNullId() {
        // Call the method under test
        Response response = attributeFacade.get(null, Status.class);

        // Verify the results
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals(ExceptionMessage.EMPTY_NOTNULL_FIELD.toString(), response.getMessage());
    }

    /**
     * get type
     */

    @Test
    @DisplayName("Test get type method with valid input and successful retrieval")
    public void testGetTypeSuccess() {
        // Set up test data
        Long id = 1L;
        Type expectedType = new Type();
        expectedType.setId(id);
        Board board = new Board();
        board.setId(1L);
        expectedType.setBoard(board);
        expectedType.setName("Open");
        expectedType.setDescription("Task is open and ready to be worked on");

        // Set up mock behavior
        when(typeService.get(id)).thenReturn(expectedType);

        // Call the method under test
        Response response = attributeFacade.get(id, Type.class);

        // Verify the results
        assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    @DisplayName("Test get type method with valid input but unsuccessful retrieval")
    public void testGetTypeUnSuccess() {
        // Set up test data
        Long id = 1L;
        String errorMessage = "Type not found";

        // Set up mock behavior
        when(typeService.get(id)).thenThrow(new NoSuchElementException(errorMessage));
        Response response = attributeFacade.get(id, Type.class);

        // Call the method under test
        assertEquals(400, response.getStatusCode());
        assertEquals(errorMessage, response.getMessage());
    }

    @Test
    @DisplayName("Test get method with null ID")
    public void testGetTypeNullId() {
        // Call the method under test
        Response response = attributeFacade.get(null, Type.class);

        // Verify the results
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals(ExceptionMessage.EMPTY_NOTNULL_FIELD.toString(), response.getMessage());
    }

    @Test
    @DisplayName("Test get method with invalid class type")
    public void testGetInvalidClass() {
        // Call the method under test
        Response response = attributeFacade.get(1L, null);

        // Verify the results
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
    }

    /**
     * getAll method tests for statuses and types
     */

    @Test
    @DisplayName("Test getAll statuses method with valid input and successful retrieval")
    public void testGetAllStatusesSuccess() {
        // Set up test data
        Status status1 = new Status();
        status1.setId(1L);
        status1.setName("Open");
        status1.setDescription("Task is open and ready to be worked on");
        Status status2 = new Status();
        status2.setId(2L);
        status2.setName("In Progress");
        status2.setDescription("Task is in progress");
        Board board1 = new Board();
        board1.setId(1L);
        Board board2 = new Board();
        board2.setId(2L);
        status1.setBoard(board1);
        status2.setBoard(board2);
        List<Status> expectedStatusList = Arrays.asList(status1, status2);

        // Set up mock behavior
        when(statusService.getAll()).thenReturn(expectedStatusList);

        // Call the method under test
        Response response = attributeFacade.getAll(Status.class);
        List<Status> responseStatusList = (List<Status>) response.getData();

        // Verify the results
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(expectedStatusList.size(), responseStatusList.size());
    }

    @Test
    @DisplayName("Test getAll method with invalid class type")
    public void testGetAllInvalidClass() {
        assertThrows(NullPointerException.class, ()->attributeFacade.getAll(null));
    }

    /**
     * type
     */

    @Test
    @DisplayName("Test getAll types method with valid input and successful retrieval")
    public void testGetAllTypesSuccess() {
        // Set up test data
        Type type1 = new Type();
        type1.setId(1L);
        type1.setName("Task");
        type1.setDescription("Tasks to work on");
        Type type2 = new Type();
        type2.setId(2L);
        type2.setName("Bug");
        type2.setDescription("Bugs to fix");
        Board board1 = new Board();
        board1.setId(1L);
        Board board2 = new Board();
        board2.setId(2L);
        type1.setBoard(board1);
        type2.setBoard(board2);
        List<Type> expectedTypesList = Arrays.asList(type1, type2);

        // Set up mock behavior
        when(typeService.getAll()).thenReturn(expectedTypesList);

        // Call the method under test
        Response response = attributeFacade.getAll(Type.class);
        List<Type> responseTypesList = (List<Type>) response.getData();

        // Verify the results
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(expectedTypesList.size(), responseTypesList.size());
    }

    /**
     * getAllAttributesInBoard for statuses and types
     */

    @Test
    @DisplayName("Test getAllStatusesInBoard method with valid input and successful retrieval")
    public void testGetAllStatusesInBoardSuccess() {
        // Set up test data
        Long boardId = 1L;
        Status status1 = new Status();
        status1.setId(1L);
        status1.setName("Open");
        status1.setDescription("Task is open and ready to be worked on");
        Status status2 = new Status();
        status2.setId(2L);
        status2.setName("In Progress");
        status2.setDescription("Task is in progress");
        Board board = new Board();
        board.setId(boardId);
        status1.setBoard(board);
        status2.setBoard(board);
        List<Status> expectedStatusList = Arrays.asList(status1, status2);

        // Set up mock behavior
        when(statusService.getAllInBoard(boardId)).thenReturn(expectedStatusList);

        // Call the method under test
        Response response = attributeFacade.getAllAttributesInBoard(boardId, Status.class);
        List<Status> responseStatusList = (List<Status>) response.getData();
        // Verify the results
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(expectedStatusList.size(), responseStatusList.size());
    }

    @Test
    @DisplayName("Test getAllStatusesInBoard method with valid input but unsuccessful retrieval")
    public void testGetAllInStatusesBoardUnSuccess() {
        // Set up test data
        Long boardId = 1L;
        String exceptionMessage = "Statuses not found";
        // Set up mock behavior
        when(statusService.getAllInBoard(boardId)).thenThrow(new NoSuchElementException(exceptionMessage));

        // Call the method under test
        Response response = attributeFacade.getAllAttributesInBoard(boardId, Status.class);
        assertEquals(exceptionMessage, response.getMessage());
    }

    @Test
    @DisplayName("Test getAllStatusesInBoard method with null board ID")
    public void testGetAllInStatusesBoardInvalidBoardId() {
        // Call the method under test
        Response response = attributeFacade.getAllAttributesInBoard(null, Status.class);

        // Verify the results
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals(ExceptionMessage.EMPTY_NOTNULL_FIELD.toString(), response.getMessage());
    }

    @Test
    @DisplayName("Test getAllAttributesInBoard method with null class type")
    public void testGetAllInBoardInvalidClass() {
        // Call the method under test
        Response response = attributeFacade.getAllAttributesInBoard(1L, null);

        // Verify the results
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
    }

    /**
     * type
     */

    @Test
    @DisplayName("Test getAllTypeInBoard method with valid input and successful retrieval")
    public void testGetAllTypesInBoardSuccess() {
        // Set up test data
        Long boardId = 1L;
        Type type1 = new Type();
        type1.setId(1L);
        type1.setName("Task");
        type1.setDescription("Tasks to work on");
        Type type2 = new Type();
        type2.setId(2L);
        type2.setName("Bug");
        type2.setDescription("Bugs to fix");
        Board board = new Board();
        board.setId(boardId);
        type1.setBoard(board);
        type2.setBoard(board);
        List<Type> expectedTypeList = Arrays.asList(type1, type2);

        // Set up mock behavior
        when(typeService.getAllInBoard(boardId)).thenReturn(expectedTypeList);

        // Call the method under test
        Response response = attributeFacade.getAllAttributesInBoard(boardId, Type.class);
        List<Type> responseStatusList = (List<Type>) response.getData();
        // Verify the results
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(expectedTypeList.size(), responseStatusList.size());
    }

    @Test
    @DisplayName("Test getAllTypeInBoard method with valid input but unsuccessful retrieval")
    public void testGetAllTypesInBoardUnSuccess() {
        // Set up test data
        Long boardId = 1L;
        String exceptionMessage = "Types not found";
        // Set up mock behavior
        when(typeService.getAllInBoard(boardId)).thenThrow(new NoSuchElementException(exceptionMessage));

        // Call the method under test
        Response response = attributeFacade.getAllAttributesInBoard(boardId, Type.class);
        assertEquals(exceptionMessage, response.getMessage());
    }

    @Test
    @DisplayName("Test getAllTypeInBoard method with null board ID")
    public void testGetAllTypesInBoardInvalidBoardId() {
        // Call the method under test
        Response response = attributeFacade.getAllAttributesInBoard(null, Type.class);

        // Verify the results
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals(ExceptionMessage.EMPTY_NOTNULL_FIELD.toString(), response.getMessage());
    }
}
