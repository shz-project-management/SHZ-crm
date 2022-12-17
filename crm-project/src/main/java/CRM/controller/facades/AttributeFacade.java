package CRM.controller.facades;

import CRM.entity.*;
import CRM.entity.DTO.AttributeDTO;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.response.Response;
import CRM.service.AttributeService;
import CRM.service.BoardService;
import CRM.service.StatusService;
import CRM.service.TypeService;
import CRM.utils.Validations;
import CRM.utils.enums.Regex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.NonUniqueObjectException;
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

    /**
     * This function creates a new attribute which could be status or type, both classes inherit from attribute and have no extra data.
     * It validates the attribute name using the NAME regex from the Regex enum,
     * finds the board it belongs to using the getBoardId from the attributeRequest object, creates a new Attribute object,
     * and calls the create function in the service that matches the class type that we get as a parameter,
     * with the help of convertFromClassToService function which gives us the relevant service based on clz Class which will hold Status or Type
     * the create function that we called will persist the attribute into the database.
     * @param attributeRequest The request body, containing the necessary information to create a new attribute.
     *                         for info is the same for both classes Status and Type.
     * @param clz hold the Class type of the attributeRequest details (Status or Type)
     * @return A Response object with the status of the create operation and the created attribute object, or an error message if the operation fails.
     */
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
        }catch(IllegalArgumentException | NonUniqueObjectException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(400)
                    .build();
        }catch(NullPointerException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(500)
                    .build();
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