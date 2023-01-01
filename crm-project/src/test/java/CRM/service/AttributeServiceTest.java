package CRM.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import CRM.entity.Attribute;
import CRM.entity.Board;
import CRM.entity.Status;
import CRM.entity.Type;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.repository.BoardRepository;
import CRM.utils.enums.ExceptionMessage;
import CRM.utils.enums.UpdateField;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@ExtendWith(MockitoExtension.class)
public class AttributeServiceTest {

    @Mock
    private BoardRepository boardRepository;
    @InjectMocks
    private AttributeService attributeService;

    @Test
    @DisplayName("create type with valid parameters successfully")
    public void createType_ValidParameters_Successfully() {
        AttributeRequest attributeRequest = new AttributeRequest();
        attributeRequest.setBoardId(1L);
        attributeRequest.setName("Test Type");
        attributeRequest.setDescription("Test Description");
        Board board = new Board();
        board.setId(1L);
        board.addAttributeToBoard(Type.createTypeAttribute("Test Type", "Test Description"), Type.class);
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        List<Type> result = attributeService.create(attributeRequest, Type.class);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("create type with non existent board should throw NoSuchElementException")
    public void createType_NonExistentBoard_ThrowsNoSuchElementException() {
        AttributeRequest attributeRequest = new AttributeRequest();
        attributeRequest.setBoardId(123L);
        attributeRequest.setName("Test Type");
        attributeRequest.setDescription("Test Description");
        assertThrows(NoSuchElementException.class, () -> attributeService.create(attributeRequest, Type.class));
    }

    @Test
    @DisplayName("create type with null name should throw NullPointerException")
    public void createType_NullName_ThrowsNullPointerException() {
        AttributeRequest attributeRequest = new AttributeRequest();
        attributeRequest.setBoardId(1L);
        attributeRequest.setName(null);
        attributeRequest.setDescription("Test Description");
        Board board = new Board();
        board.setId(1L);
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        assertThrows(NullPointerException.class, () -> {
            attributeService.create(attributeRequest, Type.class);
        });
    }

    @Test
    @DisplayName("create type with null board should throw NullPointerException")
    public void createType_NullBoard_ThrowsNullPointerException() {
        AttributeRequest attributeRequest = new AttributeRequest();
        attributeRequest.setBoardId(null);
        attributeRequest.setName("Name");
        attributeRequest.setDescription("Test Description");
        assertThrows(NoSuchElementException.class, () -> {
            attributeService.create(attributeRequest, Type.class);
        });
    }

    @Test
    @DisplayName("create type with null description should not throw any exception")
    public void createType_NullDescription_DoesNotThrow() {
        AttributeRequest attributeRequest = new AttributeRequest();
        attributeRequest.setBoardId(1L);
        attributeRequest.setName("Test Name");
        attributeRequest.setDescription(null);
        Board board = new Board();
        board.setId(1L);
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        assertDoesNotThrow(() -> {
            attributeService.create(attributeRequest, Type.class);
        });
    }

    @Test
    @DisplayName("delete type with valid parameters successfully")
    public void deleteType_ValidParameters_Successfully() {
        Board board = new Board();
        board.setId(1L);
        board.setName("Test Board");
        board.setDescription("This is a test board.");
        Type type = new Type();
        type.setId(1L);
        type.setName("Test Type");
        type.setDescription("This is a test type.");
        board.addAttributeToBoard(type, Type.class);
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        attributeService.delete(1L, 1L, Type.class);
        assertEquals(0, board.getAllAttributeInBoard(Type.class).size());
    }

    @Test
    @DisplayName("delete type with non existent attribute should throw NoSuchElementException")
    public void deleteType_AttributeDoesNotExist_ThrowsNoSuchElementException() {
        Board board = new Board();
        board.setId(1L);
        board.setName("Test Board");
        board.setDescription("This is a test board.");
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        assertThrows(NoSuchElementException.class,
                () -> attributeService.delete(1L, 1L, Type.class));

    }

    @Test
    @DisplayName("delete type with non existent board should throw NoSuchElementException")
    public void deleteType_BoardDoesNotExist_ThrowsNoSuchElementException() {
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        assertThrows(NoSuchElementException.class,
                () -> attributeService.delete(1L, 1L, Type.class));
    }

    @Test
    @DisplayName("create type with negative attribute id should throw NoSuchElementException")
    public void deleteType_AttributeIdIsNegative_ThrowsNoSuchElementException() {
        Board board = new Board();
        board.setId(1L);
        board.setName("Test Board");
        board.setDescription("This is a test board.");
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        assertThrows(NoSuchElementException.class, () ->
                attributeService.delete(1L, -1L, Type.class));
    }

    @Test
    @DisplayName("get type with valid parameters successfully")
    public void getType_ValidParameters_Successfully() {
        Board board = new Board();
        board.setId(1L);
        board.setName("Test Board");
        board.setDescription("This is a test board.");
        Type type = new Type();
        type.setId(1L);
        type.setName("Test Type");
        type.setDescription("This is a test type.");
        board.addAttributeToBoard(type, Type.class);
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        Type retrievedType = attributeService.get(1L, 1L, Type.class);
        assertEquals(type, retrievedType);
    }

    @Test
    @DisplayName("get type with non existent attribute should throw NoSuchElementException")
    public void getType_AttributeDoesNotExist_ThrowsNoSuchElementException() {
        Board board = new Board();
        board.setId(1L);
        board.setName("Test Board");
        board.setDescription("This is a test board.");
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        assertThrows(NoSuchElementException.class,
                () -> attributeService.get(1L, 1L, Type.class));
    }

    @Test
    @DisplayName("get type with non existent board should throw NoSuchElementException")
    public void getType_BoardDoesNotExist_ThrowsNoSuchElementException() {
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        assertThrows(NoSuchElementException.class,
                () -> attributeService.get(1L, 1L, Type.class));
    }

    @Test
    @DisplayName("get all types in board with valid parameters successfully")
    public void getAllTypesInBoard_ValidParameters_Successfully() {
        Board board = new Board();
        board.setId(1L);
        board.setName("Test Board");
        board.setDescription("This is a test board.");
        Type type1 = new Type();
        type1.setId(1L);
        type1.setName("Test Type 1");
        type1.setDescription("This is a test type.");
        board.addAttributeToBoard(type1, Type.class);
        Type type2 = new Type();
        type2.setId(2L);
        type2.setName("Test Type 2");
        type2.setDescription("This is another test type.");
        board.addAttributeToBoard(type2, Type.class);
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        List<Type> types = attributeService.getAllAttributesInBoard(1L, Type.class);
        assertEquals(Arrays.asList(type1, type2).toString(), types.toString());
    }

    @Test
    @DisplayName("get all types in board with non existent board should return empty list")
    public void getAllTypesInBoard_NonExistentBoard_ReturnsEmptyList() {
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        assertThrows(NoSuchElementException.class, () ->
                attributeService.getAllAttributesInBoard(1L, Type.class));
    }

    @Test
    @DisplayName("get all types in board when board does not contain types should return empty list")
    public void getAllTypesInBoard_BoardDoesNotContainTypes_ReturnsEmptyList() {
        Board board = new Board();
        board.setId(1L);
        board.setName("Test Board");
        board.setDescription("This is a test board.");
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        List<Type> types = attributeService.getAllAttributesInBoard(1L, Type.class);
        assertTrue(types.isEmpty());
    }

    @Test
    @DisplayName("get all attributes in board with unsupported attribute type should throw IllegalArgumentException")
    public void getAllAttributesInBoard_UnsupportedAttributeType_ThrowsIllegalArgumentException() {
        Board board = new Board();
        board.setId(1L);
        board.setName("Test Board");
        board.setDescription("This is a test board.");
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> attributeService.getAllAttributesInBoard(1L, Attribute.class));
        assertTrue(exception.getMessage().contains("Invalid Attribute class"));
    }

    @Test
    @DisplayName("update type with non existent attribute should throw IllegalArgumentException")
    public void updateType_AttributeDoesNotExist_ThrowsIllegalArgumentException() {
        Board board = new Board();
        board.setId(1L);
        board.setName("Test Board");
        board.setDescription("This is a test board.");
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        UpdateObjectRequest request = new UpdateObjectRequest();
        request.setObjectsIdsRequest(new ObjectsIdsRequest());
        request.getObjectsIdsRequest().setBoardId(1L);
        request.getObjectsIdsRequest().setUpdateObjId(1L);
        request.setFieldName(UpdateField.NAME);
        request.setContent("Test Type 1");
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> attributeService.update(request, Type.class));
        assertEquals(ExceptionMessage.FIELD_OBJECT_NOT_EXISTS.toString(), exception.getMessage());
    }

    @Test
    @DisplayName("update type with valid request should update successfully")
    public void updateType_ValidRequest_Success() throws NoSuchFieldException {
        Board board = new Board();
        board.setId(1L);
        board.setName("Test Board");
        board.setDescription("This is a test board.");
        Type type = new Type();
        type.setId(1L);
        type.setName("Test Type 1");
        type.setDescription("This is a test type.");
        board.addAttributeToBoard(type, Type.class);
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        UpdateObjectRequest request = new UpdateObjectRequest();
        request.setObjectsIdsRequest(new ObjectsIdsRequest());
        request.getObjectsIdsRequest().setBoardId(1L);
        request.getObjectsIdsRequest().setUpdateObjId(1L);
        request.setFieldName(UpdateField.NAME);
        request.setContent("Updated Test Type 1");
        attributeService.update(request, Type.class);
        Type updatedType = board.getAttributeById(1L, Type.class);
        assertEquals("Updated Test Type 1", updatedType.getName());
    }

    @Test
    @DisplayName("check similarity with existent types names should return list of matching types")
    public void checkSimilarityWithDatabase_ValidRequestAndTypeClass_ReturnsListOfMatchingTypes() {
        Board board = new Board();
        board.setId(1L);
        board.setName("Test Board");
        board.setDescription("This is a test board.");
        Type type1 = new Type();
        type1.setId(1L);
        type1.setName("Test Type 1");
        type1.setDescription("This is a test type.");
        Type type2 = new Type();
        type2.setId(2L);
        type2.setName("Test Type 2");
        type2.setDescription("This is another test type.");
        Type type3 = new Type();
        type3.setId(3L);
        type3.setName("Test Type 3");
        type3.setDescription("This is yet another test type.");
        board.addAttributeToBoard(type1, Type.class);
        board.addAttributeToBoard(type2, Type.class);
        board.addAttributeToBoard(type3, Type.class);
        AttributeRequest request = new AttributeRequest();
        request.setBoardId(1L);
        request.setName("Test Type 1");
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        List<Type> matchingAttributes = attributeService.checkSimilarityWithDatabase(request, Type.class);
        assertEquals(1, matchingAttributes.size());
        assertEquals("Test Type 1", matchingAttributes.get(0).getName());
    }

    //status attribute tests:

    @Test
    @DisplayName("create status with valid parameters successfully")
    public void createStatus_ValidParameters_Successfully() {
        AttributeRequest attributeRequest = new AttributeRequest();
        attributeRequest.setBoardId(1L);
        attributeRequest.setName("Test Status");
        attributeRequest.setDescription("Test Description");
        Board board = new Board();
        board.setId(1L);
        board.addAttributeToBoard(Status.createStatusAttribute("Test Status", "Test Description"), Status.class);
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        List<Status> result = attributeService.create(attributeRequest, Status.class);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("create status with non existent board should throw NoSuchElementException")
    public void createStatus_NonExistentBoard_ThrowsNoSuchElementException() {
        AttributeRequest attributeRequest = new AttributeRequest();
        attributeRequest.setBoardId(123L);
        attributeRequest.setName("Test Status");
        attributeRequest.setDescription("Test Description");
        assertThrows(NoSuchElementException.class, () -> attributeService.create(attributeRequest, Status.class));
    }

    @Test
    @DisplayName("create status with null name should throw NullPointerException")
    public void createStatus_NullName_ThrowsNullPointerException() {
        AttributeRequest attributeRequest = new AttributeRequest();
        attributeRequest.setBoardId(1L);
        attributeRequest.setName(null);
        attributeRequest.setDescription("Test Description");
        Board board = new Board();
        board.setId(1L);
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        assertThrows(NullPointerException.class, () -> {
            attributeService.create(attributeRequest, Status.class);
        });
    }

    @Test
    @DisplayName("create status with null board should throw NullPointerException")
    public void createStatus_NullBoard_ThrowsNullPointerException() {
        AttributeRequest attributeRequest = new AttributeRequest();
        attributeRequest.setBoardId(null);
        attributeRequest.setName("Name");
        attributeRequest.setDescription("Test Description");
        assertThrows(NoSuchElementException.class, () -> {
            attributeService.create(attributeRequest, Status.class);
        });
    }

    @Test
    @DisplayName("create status with null description should not throw any exception")
    public void createStatus_NullDescription_DoesNotThrow() {
        AttributeRequest attributeRequest = new AttributeRequest();
        attributeRequest.setBoardId(1L);
        attributeRequest.setName("Test Name");
        attributeRequest.setDescription(null);
        Board board = new Board();
        board.setId(1L);
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        assertDoesNotThrow(() -> {
            attributeService.create(attributeRequest, Status.class);
        });
    }

    @Test
    @DisplayName("delete status with valid parameters successfully")
    public void deleteStatus_ValidParameters_Successfully() {
        Board board = new Board();
        board.setId(1L);
        board.setName("Test Board");
        board.setDescription("This is a test board.");
        Status status = new Status();
        status.setId(1L);
        status.setName("Test Status");
        status.setDescription("This is a test status.");
        board.addAttributeToBoard(status, Status.class);
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        attributeService.delete(1L, 1L, Status.class);
        assertEquals(0, board.getAllAttributeInBoard(Status.class).size());
    }

    @Test
    @DisplayName("delete status with non existent attribute should throw NoSuchElementException")
    public void deleteStatus_AttributeDoesNotExist_ThrowsNoSuchElementException() {
        Board board = new Board();
        board.setId(1L);
        board.setName("Test Board");
        board.setDescription("This is a test board.");
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        assertThrows(NoSuchElementException.class,
                () -> attributeService.delete(1L, 1L, Status.class));

    }

    @Test
    @DisplayName("delete status with non existent board should throw NoSuchElementException")
    public void deleteStatus_BoardDoesNotExist_ThrowsNoSuchElementException() {
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        assertThrows(NoSuchElementException.class,
                () -> attributeService.delete(1L, 1L, Status.class));
    }

    @Test
    @DisplayName("create status with negative attribute id should throw NoSuchElementException")
    public void deleteStatus_AttributeIdIsNegative_ThrowsNoSuchElementException() {
        Board board = new Board();
        board.setId(1L);
        board.setName("Test Board");
        board.setDescription("This is a test board.");
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        assertThrows(NoSuchElementException.class, () ->
                attributeService.delete(1L, -1L, Status.class));
    }

    @Test
    @DisplayName("get status with valid parameters successfully")
    public void getStatus_ValidParameters_Successfully() {
        Board board = new Board();
        board.setId(1L);
        board.setName("Test Board");
        board.setDescription("This is a test board.");
        Status status = new Status();
        status.setId(1L);
        status.setName("Test Status");
        status.setDescription("This is a test status.");
        board.addAttributeToBoard(status, Status.class);
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        Status retrievedStatus = attributeService.get(1L, 1L, Status.class);
        assertEquals(status, retrievedStatus);
    }

    @Test
    @DisplayName("get status with non existent attribute should throw NoSuchElementException")
    public void getStatus_AttributeDoesNotExist_ThrowsNoSuchElementException() {
        Board board = new Board();
        board.setId(1L);
        board.setName("Test Board");
        board.setDescription("This is a test board.");
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        assertThrows(NoSuchElementException.class,
                () -> attributeService.get(1L, 1L, Status.class));
    }

    @Test
    @DisplayName("get status with non existent board should throw NoSuchElementException")
    public void getStatus_BoardDoesNotExist_ThrowsNoSuchElementException() {
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        assertThrows(NoSuchElementException.class,
                () -> attributeService.get(1L, 1L, Status.class));
    }

    @Test
    @DisplayName("get all statuss in board with valid parameters successfully")
    public void getAllStatussInBoard_ValidParameters_Successfully() {
        Board board = new Board();
        board.setId(1L);
        board.setName("Test Board");
        board.setDescription("This is a test board.");
        Status status1 = new Status();
        status1.setId(1L);
        status1.setName("Test Status 1");
        status1.setDescription("This is a test status.");
        board.addAttributeToBoard(status1, Status.class);
        Status status2 = new Status();
        status2.setId(2L);
        status2.setName("Test Status 2");
        status2.setDescription("This is another test status.");
        board.addAttributeToBoard(status2, Status.class);
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        List<Status> statuss = attributeService.getAllAttributesInBoard(1L, Status.class);
        assertEquals(Arrays.asList(status1, status2).toString(), statuss.toString());
    }

    @Test
    @DisplayName("get all statuss in board with non existent board should return empty list")
    public void getAllStatussInBoard_NonExistentBoard_ReturnsEmptyList() {
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        assertThrows(NoSuchElementException.class, () ->
                attributeService.getAllAttributesInBoard(1L, Status.class));
    }

    @Test
    @DisplayName("get all statuss in board when board does not contain statuss should return empty list")
    public void getAllStatussInBoard_BoardDoesNotContainStatuss_ReturnsEmptyList() {
        Board board = new Board();
        board.setId(1L);
        board.setName("Test Board");
        board.setDescription("This is a test board.");
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        List<Status> statuss = attributeService.getAllAttributesInBoard(1L, Status.class);
        assertTrue(statuss.isEmpty());
    }

    @Test
    @DisplayName("update status with non existent attribute should throw IllegalArgumentException")
    public void updateStatus_AttributeDoesNotExist_ThrowsIllegalArgumentException() {
        Board board = new Board();
        board.setId(1L);
        board.setName("Test Board");
        board.setDescription("This is a test board.");
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        UpdateObjectRequest request = new UpdateObjectRequest();
        request.setObjectsIdsRequest(new ObjectsIdsRequest());
        request.getObjectsIdsRequest().setBoardId(1L);
        request.getObjectsIdsRequest().setUpdateObjId(1L);
        request.setFieldName(UpdateField.NAME);
        request.setContent("Test Status 1");
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> attributeService.update(request, Status.class));
        assertEquals(ExceptionMessage.FIELD_OBJECT_NOT_EXISTS.toString(), exception.getMessage());
    }

    @Test
    @DisplayName("update status with valid request should update successfully")
    public void updateStatus_ValidRequest_Success() throws NoSuchFieldException {
        Board board = new Board();
        board.setId(1L);
        board.setName("Test Board");
        board.setDescription("This is a test board.");
        Status status = new Status();
        status.setId(1L);
        status.setName("Test Status 1");
        status.setDescription("This is a test status.");
        board.addAttributeToBoard(status, Status.class);
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        UpdateObjectRequest request = new UpdateObjectRequest();
        request.setObjectsIdsRequest(new ObjectsIdsRequest());
        request.getObjectsIdsRequest().setBoardId(1L);
        request.getObjectsIdsRequest().setUpdateObjId(1L);
        request.setFieldName(UpdateField.NAME);
        request.setContent("Updated Test Status 1");
        attributeService.update(request, Status.class);
        Status updatedStatus = board.getAttributeById(1L, Status.class);
        assertEquals("Updated Test Status 1", updatedStatus.getName());
    }

    @Test
    @DisplayName("check similarity with existent statuss names should return list of matching statuss")
    public void checkSimilarityWithDatabase_ValidRequestAndStatusClass_ReturnsListOfMatchingStatuss() {
        Board board = new Board();
        board.setId(1L);
        board.setName("Test Board");
        board.setDescription("This is a test board.");
        Status status1 = new Status();
        status1.setId(1L);
        status1.setName("Test Status 1");
        status1.setDescription("This is a test status.");
        Status status2 = new Status();
        status2.setId(2L);
        status2.setName("Test Status 2");
        status2.setDescription("This is another test status.");
        Status status3 = new Status();
        status3.setId(3L);
        status3.setName("Test Status 3");
        status3.setDescription("This is yet another test status.");
        board.addAttributeToBoard(status1, Status.class);
        board.addAttributeToBoard(status2, Status.class);
        board.addAttributeToBoard(status3, Status.class);
        AttributeRequest request = new AttributeRequest();
        request.setBoardId(1L);
        request.setName("Test Status 1");
        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(board));
        List<Status> matchingAttributes = attributeService.checkSimilarityWithDatabase(request, Status.class);
        assertEquals(1, matchingAttributes.size());
        assertEquals("Test Status 1", matchingAttributes.get(0).getName());
    }
}
