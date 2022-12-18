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

    @Override
    public boolean delete(Long typeId) throws AccountNotFoundException {
        Type type = Validations.doesIdExists(typeId, typeRepository);
        typeRepository.delete(type);
        return true;
    }
}
