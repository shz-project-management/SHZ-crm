package CRM.service;

import CRM.controller.facades.AttributeFacade;
import CRM.entity.Attribute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class TypeService implements AttributeService{

    private static Logger logger = LogManager.getLogger(TypeService.class.getName());

    @Override
    public int update(long id, Attribute object) {
        return 0;
    }
}
