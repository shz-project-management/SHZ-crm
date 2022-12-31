package CRM.controller.facades;//package CRM.controller.facades;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import CRM.entity.*;
import CRM.entity.DTO.BoardDTO;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.entity.response.Response;
import CRM.utils.enums.ExceptionMessage;
import CRM.utils.enums.SuccessMessage;
import com.google.api.client.http.HttpStatusCodes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import CRM.entity.DTO.AttributeDTO;
import CRM.entity.requests.AttributeRequest;
import CRM.service.AttributeService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

@ExtendWith(MockitoExtension.class)
public class AttributeFacadeTest {

    @Mock
    private AttributeService attributeService;

    @InjectMocks
    private AttributeFacade attributeFacade;

    //create status tests

    @Test
    @DisplayName("Create status attribute with valid parameters succeeds")
    public void createStatus_ValidParameters_Success() {
        AttributeRequest request = new AttributeRequest(1L, "Test Attribute", "Test Description");
        when(attributeService.create(request, Status.class)).thenReturn(new ArrayList<>());
        List<AttributeDTO> attributeDTOS = AttributeDTO.getListOfAttributesFromDB(new HashSet<>());
        Response<List<AttributeDTO>> response = attributeFacade.create(request, Status.class);
        verify(attributeService).create(request, Status.class);
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals(attributeDTOS, response.getData());
    }

    @Test
    @DisplayName("Create status should throw IllegalArgumentException when status name is illegal")
    public void createStatus_InvalidName_ServerErrorResponse() {
        AttributeRequest request = new AttributeRequest(1L, "!#@A", "Test Description");
        assertThrows(IllegalArgumentException.class,
                ()->attributeFacade.create(request, Status.class));
    }

    @Test
    @DisplayName("Create status should throw IllegalArgumentException when status name is not unique")
    public void createStatus_NonUniqueName_ServerErrorResponse() {
        String errorMessage = "This name already exists";
        AttributeRequest request = new AttributeRequest(1L, "Test Attribute", "Test Description");
        when(attributeService.create(request, Status.class)).thenThrow(new IllegalArgumentException(errorMessage));
        assertThrows(IllegalArgumentException.class,
                ()->attributeFacade.create(request, Status.class));
    }

    @Test
    @DisplayName("Create status should throw NullPointerException when attributeRequest has null name")
    public void createStatus_NoName_ServerErrorResponse() {
        AttributeRequest request = new AttributeRequest();
        request.setBoardId(1L);
        request.setDescription("Attribute Description");
        assertThrows(NullPointerException.class,
                ()->attributeFacade.create(request, Status.class));
    }

    @Test
    @DisplayName("Create status should throw NoSuchElementException when given a non existent board")
    public void createStatus_NonExistentBoard_ServerErrorResponse() {
        AttributeRequest request = new AttributeRequest(1L, "Test Attribute", "Test Description");
        when(attributeService.create(request, Status.class)).thenThrow(new NoSuchElementException(ExceptionMessage.NO_SUCH_ID.toString()));
        assertThrows(NoSuchElementException.class,
                ()->attributeFacade.create(request, Status.class));
    }

    @Test
    @DisplayName("Create status should throw NullPointerException when attributeRequest is null")
    public void createStatus_NullAttributeRequest_ServerErrorResponse() {
        assertThrows(NullPointerException.class,
                ()->attributeFacade.create(null, Status.class));
    }

    //create type class

    @Test
    @DisplayName("Create type attribute with valid parameters succeeds")
    public void createType_ValidParameters_Success() {
        AttributeRequest request = new AttributeRequest(1L, "Test Attribute", "Test Description");
        when(attributeService.create(request, Type.class)).thenReturn(new ArrayList<>());
        List<AttributeDTO> attributeDTOS = AttributeDTO.getListOfAttributesFromDB(new HashSet<>());
        Response<List<AttributeDTO>> response = attributeFacade.create(request, Type.class);
        verify(attributeService).create(request, Type.class);
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals(attributeDTOS, response.getData());
    }

    @Test
    @DisplayName("Create type should throw IllegalArgumentException when type name is illegal")
    public void createType_InvalidName_ServerErrorResponse() {
        AttributeRequest request = new AttributeRequest(1L, "!#@A", "Test Description");
        assertThrows(IllegalArgumentException.class,
                ()->attributeFacade.create(request, Type.class));
    }

    @Test
    @DisplayName("Create type should throw IllegalArgumentException when type name is not unique")
    public void createType_NonUniqueName_ServerErrorResponse() {
        String errorMessage = "This name already exists";
        AttributeRequest request = new AttributeRequest(1L, "Test Attribute", "Test Description");
        when(attributeService.create(request, Type.class)).thenThrow(new IllegalArgumentException(errorMessage));
        assertThrows(IllegalArgumentException.class,
                ()->attributeFacade.create(request, Type.class));
    }

    @Test
    @DisplayName("Create type should throw NullPointerException when attributeRequest has null name")
    public void createType_NoName_ServerErrorResponse() {
        AttributeRequest request = new AttributeRequest();
        request.setBoardId(1L);
        request.setDescription("Attribute Description");
        assertThrows(NullPointerException.class,
                ()->attributeFacade.create(request, Type.class));
    }

    @Test
    @DisplayName("Create type should throw NoSuchElementException when given a non existent board")
    public void createType_NonExistentBoard_ServerErrorResponse() {
        AttributeRequest request = new AttributeRequest(1L, "Test Attribute", "Test Description");
        when(attributeService.create(request, Type.class)).thenThrow(new NoSuchElementException(ExceptionMessage.NO_SUCH_ID.toString()));
        assertThrows(NoSuchElementException.class,
                ()->attributeFacade.create(request, Type.class));
    }

    @Test
    @DisplayName("Create type should throw NullPointerException when attributeRequest is null")
    public void createType_NullAttributeRequest_ServerErrorResponse() {
        assertThrows(NullPointerException.class,
                ()->attributeFacade.create(null, Type.class));
    }

    @Test
    @DisplayName("Create attribute should return empty data when type class is null")
    public void createAttribute_NullTypeClass_EmptyData() {
        AttributeRequest request = new AttributeRequest(1L, "Test Attribute", "Test Description");
        Response<List<AttributeDTO>> response = attributeFacade.create(request, null);
        assertEquals(response.getData(), new ArrayList<>());
    }

    //delete status tests

    @Test
    @DisplayName("Deleting a status successfully")
    public void deleteStatus_validParameters_Success() {
        Board mockBoard = Mockito.mock(Board.class);
        when(attributeService.delete(1L, 2L, Status.class)).thenReturn(mockBoard);
        when(mockBoard.getCreatorUser()).thenReturn(new User());
        Response<BoardDTO> response = attributeFacade.delete(1L, 2L, Status.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(HttpStatusCodes.STATUS_CODE_NO_CONTENT, response.getStatusCode());
        assertEquals(SuccessMessage.DELETED.toString(), response.getMessage());
    }

    @Test
    @DisplayName("Deleting a status with an invalid ID should throw IllegalArgumentException")
    public void deleteStatus_InvalidId_ServerErrorResponse() {
        assertThrows(IllegalArgumentException.class, () -> attributeFacade.delete(-1L, -2L, Status.class));
    }

    @Test
    @DisplayName("Deleting a status with null ID should throw NullPointerException")
    public void deleteStatus_NullId_ServerErrorResponse() {
        assertThrows(NullPointerException.class, () -> attributeFacade.delete(1L, null, Status.class));
    }

    //delete type tests

    @Test
    @DisplayName("Deleting a type successfully")
    public void deleteType_validParameters_Success() {
        Board mockBoard = Mockito.mock(Board.class);
        when(attributeService.delete(1L, 2L, Type.class)).thenReturn(mockBoard);
        when(mockBoard.getCreatorUser()).thenReturn(new User());
        Response<BoardDTO> response = attributeFacade.delete(1L, 2L, Type.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(HttpStatusCodes.STATUS_CODE_NO_CONTENT, response.getStatusCode());
        assertEquals(SuccessMessage.DELETED.toString(), response.getMessage());
    }

    @Test
    @DisplayName("Deleting a type with an invalid ID should throw IllegalArgumentException")
    public void deleteType_InvalidId_ServerErrorResponse() {
        assertThrows(IllegalArgumentException.class, () -> attributeFacade.delete(-1L, -2L, Type.class));
    }

    @Test
    @DisplayName("Deleting a type with null ID should throw NullPointerException")
    public void deleteType_NullId_ServerErrorResponse() {
        assertThrows(NullPointerException.class, () -> attributeFacade.delete(1L, null, Type.class));
    }

    //get status tests

    @Test
    @DisplayName("Test getting a status successfully")
    public void getStatus_validParameters_Success() {
        Status mockStatus = Mockito.mock(Status.class);
        when(attributeService.get(1L, 2L, Status.class)).thenReturn(mockStatus);
        Response<AttributeDTO> response = attributeFacade.get(1L, 2L, Status.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.getStatusCode());
        assertEquals(SuccessMessage.FOUND.toString(), response.getMessage());
    }

    @Test
    @DisplayName("Getting a status with an invalid ID should throw IllegalArgumentException")
    public void getStatus_InvalidId_ServerErrorResponse() {
        assertThrows(IllegalArgumentException.class, () -> attributeFacade.get(-1L, -2L, Status.class));
    }

    @Test
    @DisplayName("Getting a status with null ID should throw NullPointerException")
    public void getStatus_NullId_ServerErrorResponse() {
        assertThrows(NullPointerException.class, () -> attributeFacade.get(null, 1L, Status.class));
    }

    //get types tests

    @Test
    @DisplayName("Test getting a type successfully")
    public void getType_validParameters_Success() {
        Type mockType = Mockito.mock(Type.class);
        when(attributeService.get(1L, 2L, Type.class)).thenReturn(mockType);
        Response<AttributeDTO> response = attributeFacade.get(1L, 2L, Type.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.getStatusCode());
        assertEquals(SuccessMessage.FOUND.toString(), response.getMessage());
    }

    @Test
    @DisplayName("Getting a type with an invalid ID should throw IllegalArgumentException")
    public void getType_InvalidId_ServerErrorResponse() {
        assertThrows(IllegalArgumentException.class, () -> attributeFacade.get(-1L, -2L, Type.class));
    }

    @Test
    @DisplayName("Getting a type with null ID should throw NullPointerException")
    public void getType_NullId_ServerErrorResponse() {
        assertThrows(NullPointerException.class, () -> attributeFacade.get(null, 1L, Type.class));
    }

    //getAllStatusesInBoard tests

    @Test
    @DisplayName("Getting all statuses in a board successfully")
    public void getAllStatusesInBoard_ValidParameters_Success() {
        List<Status> mockAttributes = new ArrayList<>();
        when(attributeService.getAllAttributesInBoard(1L, Status.class)).thenReturn(mockAttributes);
        Response<List<AttributeDTO>> response = attributeFacade.getAllAttributesInBoard(1L, Status.class);
        assertEquals(mockAttributes, response.getData());
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.getStatusCode());
        assertEquals(SuccessMessage.FOUND.toString(), response.getMessage());
    }

    @Test
    @DisplayName("Getting all statuses in a board with an invalid board ID should throw IllegalArgumentException")
    public void getAllStatusesInBoard_InvalidBoardID_ServerErrorResponse() {
        assertThrows(IllegalArgumentException.class, () -> attributeFacade.getAllAttributesInBoard(-1L, Status.class));
    }


    @Test
    @DisplayName("Getting all statuses in a board with a null board should throw NullPointerException")
    public void getAllStatusesInBoard_NullBoard_ServerErrorResponse() {
        assertThrows(NullPointerException.class, () -> attributeFacade.getAllAttributesInBoard(null, Status.class));
    }

    @Test
    @DisplayName("Getting all statuses in a board with multiple statuses success")
    public void getAllStatusesInBoard_MultipleAttributes_Success() {
        List<Status> mockAttributes = new ArrayList<>();
        mockAttributes.add(Mockito.mock(Status.class));
        mockAttributes.add(Mockito.mock(Status.class));
        when(attributeService.getAllAttributesInBoard(1L, Status.class)).thenReturn(mockAttributes);
        Response<List<AttributeDTO>> response = attributeFacade.getAllAttributesInBoard(1L, Status.class);
        assertEquals(mockAttributes.size(), response.getData().size());
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.getStatusCode());
        assertEquals(SuccessMessage.FOUND.toString(), response.getMessage());
    }

    //getAllTypesInBoard tests

    @Test
    @DisplayName("Getting all types in a board successfully")
    public void getAllTypesInBoard_ValidParameters_Success() {
        List<Type> mockAttributes = new ArrayList<>();
        when(attributeService.getAllAttributesInBoard(1L, Type.class)).thenReturn(mockAttributes);
        Response<List<AttributeDTO>> response = attributeFacade.getAllAttributesInBoard(1L, Type.class);
        assertEquals(mockAttributes, response.getData());
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.getStatusCode());
        assertEquals(SuccessMessage.FOUND.toString(), response.getMessage());
    }

    @Test
    @DisplayName("Getting all types in a board with an invalid board ID should throw IllegalArgumentException")
    public void getAllTypesInBoard_InvalidBoardID_ServerErrorResponse() {
        assertThrows(IllegalArgumentException.class, () -> attributeFacade.getAllAttributesInBoard(-1L, Type.class));
    }


    @Test
    @DisplayName("Getting all types in a board with a null board should throw NullPointerException")
    public void getAllTypesInBoard_NullBoard_ServerErrorResponse() {
        assertThrows(NullPointerException.class, () -> attributeFacade.getAllAttributesInBoard(null, Type.class));
    }

    @Test
    @DisplayName("Getting all types in a board with multiple types success")
    public void getAllTypesInBoard_MultipleAttributes_Success() {
        List<Type> mockAttributes = new ArrayList<>();
        mockAttributes.add(Mockito.mock(Type.class));
        mockAttributes.add(Mockito.mock(Type.class));
        when(attributeService.getAllAttributesInBoard(1L, Type.class)).thenReturn(mockAttributes);
        Response<List<AttributeDTO>> response = attributeFacade.getAllAttributesInBoard(1L, Type.class);
        assertEquals(mockAttributes.size(), response.getData().size());
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.getStatusCode());
        assertEquals(SuccessMessage.FOUND.toString(), response.getMessage());
    }

    //update status tests

    @Test
    @DisplayName("Updating a status with invalid IDs should throw IllegalArgumentException")
    public void updateStatus_InvalidIDs_ServerErrorResponse() {
        UpdateObjectRequest updateObjectRequest = new UpdateObjectRequest();
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        objectsIdsRequest.setBoardId(-1L);
        objectsIdsRequest.setUpdateObjId(-1L);
        updateObjectRequest.setObjectsIdsRequest(objectsIdsRequest);
        assertThrows(IllegalArgumentException.class,
                () -> attributeFacade.update(updateObjectRequest, Status.class));
    }

    @Test
    @DisplayName("Updating a status with null IDs should throw NullPointerException")
    public void updateStatus_NullIDs_ServerErrorResponse() {
        assertThrows(NullPointerException.class,
                () -> attributeFacade.update(new UpdateObjectRequest(), Status.class));
    }

    //update type tests

    @Test
    @DisplayName("Updating a type with invalid IDs should throw IllegalArgumentException")
    public void updateType_InvalidIDs_ServerErrorResponse() {
        UpdateObjectRequest updateObjectRequest = new UpdateObjectRequest();
        ObjectsIdsRequest objectsIdsRequest = new ObjectsIdsRequest();
        objectsIdsRequest.setBoardId(-1L);
        objectsIdsRequest.setUpdateObjId(-1L);
        updateObjectRequest.setObjectsIdsRequest(objectsIdsRequest);
        assertThrows(IllegalArgumentException.class,
                () -> attributeFacade.update(updateObjectRequest, Type.class));
    }

    @Test
    @DisplayName("Updating a type with null IDs should throw NullPointerException")
    public void updateType_NullIDs_ServerErrorResponse() {
        assertThrows(NullPointerException.class,
                () -> attributeFacade.update(new UpdateObjectRequest(), Type.class));
    }
}

