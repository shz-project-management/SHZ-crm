package CRM.controller.facades;

import CRM.entity.*;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.response.Response;
import CRM.service.AttributeService;
import CRM.service.StatusService;
import CRM.service.TypeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AttributeFacade {
    private static Logger logger = LogManager.getLogger(AttributeFacade.class.getName());

    @Autowired
    private StatusService statusService;
    @Autowired
    private TypeService typeService;

    public Response create(AttributeRequest statusRequest) {
        return null;
    }

    public Response delete(Long id) {
        return null;
    }

    public Response updateAttribute(Long itemId, Attribute object){
        // validate the id using Validations.validate
        return null;
    }

    private AttributeService convertFromClassToService(Class c) {
        logger.info("in AttributeFacade -> convertFromClassToService ,item of Class: " + c);

        if (c.equals(Type.class)) return typeService;
        if (c.equals(Status.class)) return statusService;

        return null;
    }
}