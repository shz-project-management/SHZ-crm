package CRM.service;

import CRM.controller.facades.AttributeFacade;
import CRM.entity.Attribute;
import CRM.entity.Status;
import CRM.entity.Type;
import CRM.repository.TypeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    @Override
    public Type create(Attribute type) {
        return typeRepository.save((Type) type);
    }
}
