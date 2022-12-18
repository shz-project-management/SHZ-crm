package CRM.service;

import CRM.entity.Attribute;
import CRM.entity.Type;
import CRM.repository.TypeRepository;
import CRM.utils.Validations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.NoSuchElementException;

@Service
public class TypeService implements AttributeService{

    private static Logger logger = LogManager.getLogger(TypeService.class.getName());

    @Autowired
    private TypeRepository typeRepository;

    @Override
    public int update(long id, Attribute object) {
        return 0;
    }

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
    public boolean delete(Long typeId) throws AccountNotFoundException {
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
    public Type get(Long id) throws AccountNotFoundException {
        return Validations.doesIdExists(id, typeRepository);
    }
}
