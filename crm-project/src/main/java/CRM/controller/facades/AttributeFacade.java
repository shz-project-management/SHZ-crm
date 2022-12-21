package CRM.controller.facades;

import CRM.entity.*;
import CRM.entity.DTO.AttributeDTO;
import CRM.entity.DTO.BoardDTO;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.entity.response.Response;
import CRM.service.*;
import CRM.utils.Validations;
import CRM.utils.enums.Regex;
import CRM.utils.enums.SuccessMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.NonUniqueObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import java.util.NoSuchElementException;

@Component
public class AttributeFacade {
    private static Logger logger = LogManager.getLogger(AttributeFacade.class.getName());

    @Autowired
    private StatusService statusService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private SectionService sectionService;
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

    /**
     * Deletes an attribute(status/type) with the given ID.
     * @param id the ID of the attribute to delete
     * @return a response object indicating the status of the deletion operation
     * @throws NoSuchElementException if no attribute with the given ID exists
     */
    public Response delete(Long id, Class clz) {
        try{
            Validations.validate(id, Regex.ID.getRegex());
            convertFromClassToService(clz).delete(id);
            return new Response.Builder()
                    .status(HttpStatus.NO_CONTENT)
                    .statusCode(204)
                    .message(SuccessMessage.DELETED.toString())
                    .build();
        }catch(NoSuchElementException | IllegalArgumentException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(400)
                    .build();
        }catch (NullPointerException e){
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(500)
                    .build();
        }
    }

    /**
     This method is used to retrieve an attribute(status/type) with the specified id.
     @param id The id of the attribute to be retrieved.
     @return A Response object containing the retrieved attribute or an error message if the attribute is not found or the id is invalid.
     @throws NoSuchElementException if the attribute with the specified id is not found.
     @throws IllegalArgumentException if the specified id is invalid.
     @throws NullPointerException if the specified id is null.
     */
    public Response get(Long id, Class clz) {
        try {
            Validations.validate(id, Regex.ID.getRegex());
            return new Response.Builder()
                    .data(AttributeDTO.createAttributeDTO(convertFromClassToService(clz).get(id)))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .build();
        } catch(NoSuchElementException | IllegalArgumentException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(400)
                    .build();
        } catch (NullPointerException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(500)
                    .build();
        }
    }

    /**
     This method is used to retrieve all the attributes(statuses/types).
     @return A Response object containing all the retrieved attributes.
     */
    public Response getAll(Class clz) {
        return new Response.Builder()
                .data(AttributeDTO.createListOfAttributesDTO(convertFromClassToService(clz).getAll()))
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.OK)
                .statusCode(200)
                .build();
    }

    /**
     This method is used to retrieve all the attributes(statuses/types) that belongs to a board with the specified id.
     @param boardId The id of the board whose attributes are to be retrieved.
     @return A Response object containing all the retrieved attributes or an error message if the board is not found or the id is invalid.
     @throws IllegalArgumentException if the specified board id is invalid.
     @throws NullPointerException if the specified board id is null.
     @throws NoSuchElementException if the board with the specified id is not found.
     */
    public Response getAllAttributesInBoard(Long boardId, Class clz) {
        try {
            Validations.validate(boardId, Regex.ID.getRegex());
            return new Response.Builder()
                    .data(AttributeDTO.createListOfAttributesDTO(convertFromClassToService(clz).getAllInBoard(boardId)))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .build();
        } catch (IllegalArgumentException | NoSuchElementException e){
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(400)
                    .build();
        }catch (NullPointerException e){
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(500)
                    .build();
        }
    }

    /**
     * Updates an attribute in the database.
     *
     * @param statusId the id of the attribute to update
     * @param statusRequest the data of the update(fieldName and content)
     * @return a response object with a status code and message
     * @throws IllegalArgumentException if the attribute ID does not match the expected format
     * @throws NoSuchElementException   if the attribute to update is not found in the database
     * @throws NullPointerException     if the attribute object is null
     */
    public Response update(UpdateObjectRequest statusRequest, Long statusId, Class clz) {
        try {
            Validations.validate(statusId, Regex.ID.getRegex());
            return new Response.Builder()
                    .data(AttributeDTO.createAttributeDTO(convertFromClassToService(clz).update(statusRequest, statusId)))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .build();
        } catch (IllegalArgumentException | NoSuchElementException | NoSuchFieldException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(400)
                    .build();
        } catch (NullPointerException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(500)
                    .build();
        }
    }

    /**
     * Converts a given Class object to the corresponding AttributeService object.
     *
     * @param c the Class object to be converted
     * @return the corresponding AttributeService object, or null if no corresponding AttributeService object is found
     */
    private AttributeService convertFromClassToService(Class c) {
        logger.info("in AttributeFacade -> convertFromClassToService ,item of Class: " + c);

        if(c.equals(Section.class)) return sectionService;
        if (c.equals(Type.class)) return typeService;
        if (c.equals(Status.class)) return statusService;

        return null;
    }
}
