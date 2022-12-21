package CRM.service;

import CRM.entity.Attribute;
import CRM.entity.Board;
import CRM.entity.Type;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.repository.BoardRepository;
import CRM.repository.TypeRepository;
import CRM.utils.Validations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TypeService implements AttributeService{

    private static Logger logger = LogManager.getLogger(TypeService.class.getName());

    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private BoardRepository boardRepository;

    /**
     * This function persists a new Type to the database by calling the save function in the TypeRepository class.
     * @param type The type object to be persisted.
     * @return The persisted type object.
     */
    @Override
    public Type create(Attribute type) {
        if(typeRepository.existsByBoardAndNameLike(type.getBoard(), type.getName()))
            Validations.throwAttributeAlreadyExistsForBoard(type, "Type");
        return typeRepository.save(Type.createType(type));
    }

    /**
     * Deletes the given type from the repository.
     * @param typeId the type ID to delete
     */
    @Override
    public boolean delete(long typeId) {
        Type type = Validations.doesIdExists(typeId, typeRepository);
        typeRepository.delete(type);
        return true;
    }

    /**
     * This method is used to retrieve a type with the specified id.
     * @param id The id of the type to be retrieved.
     * @return The retrieved type.
     * @throws NoSuchElementException   if the type with the specified id is not found.
     * @throws IllegalArgumentException if the specified id is invalid.
     * @throws NullPointerException     if the specified id is null.
     */
    @Override
    public Type get(long id) {
        return Validations.doesIdExists(id, typeRepository);
    }

    @Override
    public Type update(UpdateObjectRequest typeRequest, long typeId) throws NoSuchFieldException {
        Type type = Validations.doesIdExists(typeId, typeRepository);
        Validations.setContentToFieldIfFieldExists(type, typeRequest.getFieldName(), typeRequest.getContent());
        return typeRepository.save(type);
    }

    /**
     * This method is used to retrieve all the types.
     *
     * @return A list containing all the types.
     */
    @Override
    public List<Type> getAll() {
        return typeRepository.findAll();
    }

    /**
     * This method is used to retrieve all the types that belong to the board with the specified id.
     *
     * @param boardId The id of the user whose types are to be retrieved.
     * @return A list containing all the types that belong to the board with the specified id.
     * @throws NoSuchElementException   if the board with the specified id is not found.
     * @throws IllegalArgumentException if the specified board id is invalid.
     * @throws NullPointerException     if the specified board id is null.
     */
    @Override
    public List<Type> getAllInBoard(long boardId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        return typeRepository.findByBoard(board);
    }
}
