package CRM.service;

import CRM.entity.Attribute;
import CRM.entity.Board;
import CRM.entity.Type;
import CRM.repository.BoardRepository;
import CRM.repository.TypeRepository;
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
@DisplayName("TypeService tests")
class TypeServiceTest {

    @Mock
    private TypeRepository typeRepository;

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private TypeService typeService;

    private Board board;

    private Type type;

    private Long typeId;

    @BeforeEach
    void setUp() {
        board = new Board();
        board.setId(1L);
        typeId = 1L;
        type = new Type();
        type.setId(typeId);
    }

    /***
     * create method tests:
     */

    @Test
    @DisplayName("Should create and return new type when name is unique for board")
    void testCreateUniqueType() {
        // Set up test data and mock behavior
        Attribute typeAttribute = Attribute.createAttribute(board, "Test Type", "Test description");
        Type expectedType = Type.createType(typeAttribute);
        when(typeRepository.existsByBoardAndNameLike(any(), any())).thenReturn(false);
        when(typeRepository.save(any())).thenReturn(expectedType);

        // Call method under test
        Type actualType = typeService.create(typeAttribute);

        // Verify results
        assertEquals(expectedType, actualType);
    }

    @Test
    @DisplayName("Should throw NonUniqueObjectException when name is not unique for board when creating a type")
    void testCreateNonUniqueType() {
        // Set up test data and mock behavior
        Attribute typeAttribute = Attribute.createAttribute(board, "Test type", "Test description");
        when(typeRepository.existsByBoardAndNameLike(any(), any())).thenReturn(true);

        // Call method under test and verify exception is thrown
        assertThrows(NonUniqueObjectException.class, () -> typeService.create(typeAttribute));
    }

    @Test
    @DisplayName("Should throw NullPointerException when creating a null type")
    public void testCreateWithNullTypeThrowsException() {
        assertThrows(NullPointerException.class, () -> typeService.create(null));
    }

    /***
     * delete method tests:
     */

    @Test
    @DisplayName("Should delete type and return true when type exists")
    void testDeleteExistingType() {
        // Set up test data and mock behavior
        when(typeRepository.findById(typeId)).thenReturn(Optional.of(type));
        doNothing().when(typeRepository).delete(type);

        // Call method under test
        boolean result = typeService.delete(typeId);

        // Verify results
        assertTrue(result);
    }

    @Test
    @DisplayName("Should throw NoSuchElementException when type does not exist")
    void testDeleteNonExistentType() {
        // Set up test data and mock behavior
        when(typeRepository.findById(typeId)).thenReturn(Optional.empty());

        // Call method under test and verify exception is thrown
        assertThrows(NoSuchElementException.class, () -> typeService.delete(typeId));
    }

    /***
     * get method tests:
     */

    @Test
    @DisplayName("Should return type when type exists")
    void testGetExistingType() {
        // Set up test data and mock behavior
        Type expectedType = new Type();
        expectedType.setId(typeId);
        when(typeRepository.findById(typeId)).thenReturn(Optional.of(expectedType));

        // Call method under test
        Type actualType = typeService.get(typeId);

        // Verify results
        assertEquals(expectedType, actualType);
    }

    @Test
    @DisplayName("Should throw NoSuchElementException when type does not exist")
    void testGetNonExistentType() {
        // Set up test data and mock behavior
        when(typeRepository.findById(typeId)).thenReturn(Optional.empty());

        // Call method under test and verify exception is thrown
        assertThrows(NoSuchElementException.class, () -> typeService.get(typeId));
    }

    /***
     * getAll method tests:
     */

    @Test
    @DisplayName("Should return all types")
    void testGetAllTypes() {
        // Set up test data and mock behavior
        List<Type> expectedTypes = List.of(
                new Type(),
                new Type()
        );
        when(typeRepository.findAll()).thenReturn(expectedTypes);

        // Call method under test
        List<Type> actualTypes = typeService.getAll();

        // Verify results
        assertEquals(expectedTypes, actualTypes);
    }

    /***
     * getAllInBoard method tests:
     */

    @Test
    @DisplayName("Should return all types in board when board exists")
    void testGetAllTypesInExistingBoard() {
        // Set up test data and mock behavior
        List<Type> expectedTypes = List.of(
                new Type(),
                new Type()
        );
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));
        when(typeRepository.findByBoard(board)).thenReturn(expectedTypes);

        // Call method under test
        List<Type> actualTypes = typeService.getAllInBoard(board.getId());

        // Verify results
        assertEquals(expectedTypes, actualTypes);
    }

    @Test
    @DisplayName("Should throw NoSuchElementException when board does not exist when getting all types by board")
    void testGetAllTypesInNonExistentBoard() {
        // Set up test data and mock behavior
        Long nonExistentBoardId = 2L;
        when(boardRepository.findById(nonExistentBoardId)).thenReturn(Optional.empty());

        // Call method under test and verify exception is thrown
        assertThrows(NoSuchElementException.class, () -> typeService.getAllInBoard(nonExistentBoardId));
    }

    @Test
    @DisplayName("Should throw NoSuchElementException when board id is invalid when getting all types by board")
    void testGetAllTypesWithInvalidBoardId() {
        // Set up test data and mock behavior
        Long invalidBoardId = -1L;

        // Call method under test and verify exception is thrown
        assertThrows(NoSuchElementException.class, () -> typeService.getAllInBoard(invalidBoardId));
    }
}