package CRM.controller.facades;

import CRM.entity.*;
import CRM.entity.DTO.AttributeDTO;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.response.Response;
import CRM.repository.StatusRepository;
import CRM.service.AttributeService;
import CRM.service.BoardService;
import CRM.service.StatusService;
import CRM.service.TypeService;
import CRM.utils.Validations;
import CRM.utils.enums.Regex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AttributeFacade {
    private static Logger logger = LogManager.getLogger(AttributeFacade.class.getName());

    @Autowired
    private StatusService statusService;
    @Autowired
    private TypeService typeService;

    @Autowired
    private BoardService boardService;
    @Autowired
    private StatusRepository statusRepository;

    public Response create(AttributeRequest attributeRequest, Class clz) {
        try{
            Validations.validate(attributeRequest.getName(), Regex.NAME.getRegex());
            Board board = boardService.get(attributeRequest.getBoardId());
            Attribute attribute = Attribute.createAttribute(board, attributeRequest.getName(), attributeRequest.getDescription());
            Attribute savedAttribute = convertFromClassToService(clz).create(attribute);
            return new Response.Builder()
                    .status(HttpStatus.CREATED)
                    .statusCode(201)
                    .data(AttributeDTO.createAttributeDTO(savedAttribute))
                    .build();
        }catch(NullPointerException | IllegalArgumentException e) {
            return new Response.Builder().message(e.getMessage()).status(HttpStatus.BAD_REQUEST).statusCode(400).build();
        }
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