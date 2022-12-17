package CRM.service;

import CRM.controller.facades.AttributeFacade;
import CRM.entity.Attribute;
import CRM.entity.Status;
import CRM.entity.Type;
import CRM.repository.TypeRepository;
import CRM.utils.enums.ExceptionMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.NonUniqueObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            throw new NonUniqueObjectException(ExceptionMessage.ATTRIBUTE_ALREADY_IN_DB.toString(), type.getId(), "Type");
        return typeRepository.save((Type) type);
    }
}
