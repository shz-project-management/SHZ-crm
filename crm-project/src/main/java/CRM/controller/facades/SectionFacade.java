package CRM.controller.facades;

import CRM.entity.requests.AttributeRequest;
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

import java.util.Arrays;
import java.util.NoSuchElementException;

@Component
public class SectionFacade {
    private static Logger logger = LogManager.getLogger(SectionFacade.class.getName());

    @Autowired
    private SectionService sectionService;

    /**
     * This function creates a new attribute which could be status or type, both classes inherit from attribute and have no extra data.
     * It validates the attribute name using the NAME regex from the Regex enum,
     * finds the board it belongs to using the getBoardId from the attributeRequest object, creates a new Attribute object,
     * and calls the create function in the service that matches the class type that we get as a parameter,
     * with the help of convertFromClassToService function which gives us the relevant service based on clz Class which will hold Status or Type
     * the create function that we called will persist the attribute into the database.
     *
     * @param attributeRequest The request body, containing the necessary information to create a new attribute.
     *                         for info is the same for both classes Status and Type.
     * @return A Response object with the status of the create operation and the created attribute object, or an error message if the operation fails.
     */
    //TODO DTO
    public Response create(AttributeRequest attributeRequest) {
        try {
            Validations.validate(attributeRequest.getName(), Regex.NAME.getRegex());

            return new Response.Builder()
                    .status(HttpStatus.CREATED)
                    .statusCode(201)
                    .data(sectionService.create(attributeRequest))
                    .build();
        } catch (IllegalArgumentException | NonUniqueObjectException | NoSuchElementException e) {
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

    //TODO documentation
    public Response delete(Long boardId, Long attributeId) {
        try {
            Validations.validateIDs(Arrays.asList(boardId, attributeId));
            sectionService.delete(boardId, attributeId);

            return new Response.Builder()
                    .status(HttpStatus.NO_CONTENT)
                    .statusCode(204)
                    .message(SuccessMessage.DELETED.toString())
                    .build();
        } catch (NoSuchElementException | IllegalArgumentException e) {
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
     * This method is used to retrieve an attribute(status/type) with the specified id.
     *
     * @param attributeId The id of the attribute to be retrieved.
     * @return A Response object containing the retrieved attribute or an error message if the attribute is not found or the id is invalid.
     * @throws NoSuchElementException   if the attribute with the specified id is not found.
     * @throws IllegalArgumentException if the specified id is invalid.
     * @throws NullPointerException     if the specified id is null.
     */
    //TODO Validation function + DTO
    public Response get(Long attributeId, Long boardId) {
        try {
            Validations.validateIDs(Arrays.asList(boardId, attributeId));
            return new Response.Builder()
                    .data(sectionService.get(attributeId, boardId))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .build();
        } catch (NoSuchElementException | IllegalArgumentException e) {
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
     * This method is used to retrieve all the attributes(statuses/types) that belongs to a board with the specified id.
     *
     * @param boardId The id of the board whose attributes are to be retrieved.
     * @return A Response object containing all the retrieved attributes or an error message if the board is not found or the id is invalid.
     * @throws IllegalArgumentException if the specified board id is invalid.
     * @throws NullPointerException     if the specified board id is null.
     * @throws NoSuchElementException   if the board with the specified id is not found.
     */
    //TODO DTO
    public Response getAllSectionsInBoard(Long boardId) {
        try {
            Validations.validate(boardId, Regex.ID.getRegex());
            return new Response.Builder()
                    .data((sectionService.getAllSectionsInBoard(boardId)))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .build();
        } catch (IllegalArgumentException | NoSuchElementException e) {
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
     * Updates an attribute in the database.
     *
     * @param statusId      the id of the attribute to update
     * @param statusRequest the data of the update(fieldName and content)
     * @return a response object with a status code and message
     * @throws IllegalArgumentException if the attribute ID does not match the expected format
     * @throws NoSuchElementException   if the attribute to update is not found in the database
     * @throws NullPointerException     if the attribute object is null
     */
    //TODO + DTO
//    public Response update(UpdateObjectRequest statusRequest, Long statusId, Class clz) {
//        try {
//            Validations.validate(statusId, Regex.ID.getRegex());
//            return new Response.Builder()
//                    .data(AttributeDTO.createAttributeDTO(attributeService.update(statusRequest, statusId)))
//                    .message(SuccessMessage.FOUND.toString())
//                    .status(HttpStatus.OK)
//                    .statusCode(200)
//                    .build();
//        } catch (IllegalArgumentException | NoSuchElementException | NoSuchFieldException e) {
//            return new Response.Builder()
//                    .message(e.getMessage())
//                    .status(HttpStatus.BAD_REQUEST)
//                    .statusCode(400)
//                    .build();
//        } catch (NullPointerException e) {
//            return new Response.Builder()
//                    .message(e.getMessage())
//                    .status(HttpStatus.BAD_REQUEST)
//                    .statusCode(500)
//                    .build();
//        }
//    }
}
