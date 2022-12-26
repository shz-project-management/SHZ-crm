package CRM.controller.facades;

import CRM.entity.*;
import CRM.entity.DTO.AttributeDTO;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.response.Response;
import CRM.service.*;
import CRM.utils.Validations;
import CRM.utils.enums.Regex;
import CRM.utils.enums.SuccessMessage;
import com.google.api.client.http.HttpStatusCodes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.NonUniqueObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AttributeFacade {
    private static Logger logger = LogManager.getLogger(AttributeFacade.class.getName());

    @Autowired
    private AttributeService attributeService;

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
     * @param clz              hold the Class type of the attributeRequest details (Status or Type)
     * @return A Response object with the status of the create operation and the created attribute object, or an error message if the operation fails.
     */
    public Response create(AttributeRequest attributeRequest, Class clz) {
        try {
            Validations.validate(attributeRequest.getName(), Regex.NAME.getRegex());

            return Response.builder()
                    .status(HttpStatus.CREATED)
                    .statusCode(HttpStatusCodes.STATUS_CODE_CREATED)
                    .data(AttributeDTO.getListOfAttributesFromDB(new HashSet<>(attributeService.create(attributeRequest, clz))))
                    .build();
        } catch (IllegalArgumentException | NonUniqueObjectException | NoSuchElementException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * Deletes an attribute(status/type) with the given ID.
     *
     * @param attributeId the ID of the attribute to delete
     * @return a response object indicating the status of the deletion operation
     * @throws NoSuchElementException if no attribute with the given ID exists
     */
    public Response delete(Long boardId, Long attributeId, Class clz) {
        try {
            Validations.validateIDs(attributeId, boardId);
            attributeService.delete(boardId, attributeId, clz);

            return Response.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .statusCode(HttpStatusCodes.STATUS_CODE_NO_CONTENT)
                    .message(SuccessMessage.DELETED.toString())
                    .build();
        } catch (NoSuchElementException | IllegalArgumentException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
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
    public Response get(Long attributeId, Long boardId, Class clz) {
        try {
            Validations.validateIDs(attributeId, boardId);

            return Response.builder()
                    .data(AttributeDTO.createAttributeDTO(attributeService.get(attributeId, boardId, clz)))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                    .build();
        } catch (NoSuchElementException | IllegalArgumentException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
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
    public Response getAllAttributesInBoard(Long boardId, Class clz) {
        try {
            Validations.validate(boardId, Regex.ID.getRegex());
            Set<Attribute> targetSet = new HashSet<>(attributeService.getAllAttributesInBoard(boardId, clz));
            return Response.builder()
                    .data(AttributeDTO.getListOfAttributesFromDB(targetSet))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                    .build();
        } catch (IllegalArgumentException | NoSuchElementException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
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
//    public Response update(UpdateObjectRequest statusRequest, Long statusId, Class clz) {
//        try {
//            Validations.validate(statusId, Regex.ID.getRegex());
//
//            return Response.builder()
//                    .data(AttributeDTO.createAttributeDTO(attributeService.update(statusRequest, statusId)))
//                    .message(SuccessMessage.FOUND.toString())
//                    .status(HttpStatus.OK)
//                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
//                    .build();
//        } catch (IllegalArgumentException | NoSuchElementException | NoSuchFieldException e) {
//            return Response.builder()
//                    .message(e.getMessage())
//                    .status(HttpStatus.BAD_REQUEST)
//                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
//                    .build();
//        } catch (NullPointerException e) {
//            return Response.builder()
//                    .message(e.getMessage())
//                    .status(HttpStatus.BAD_REQUEST)
//                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
//                    .build();
//        }
//    }
}
