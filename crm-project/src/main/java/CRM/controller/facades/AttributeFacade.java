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

@Component
public class AttributeFacade {
    private static Logger logger = LogManager.getLogger(AttributeFacade.class.getName());

    @Autowired
    private AttributeService attributeService;

    /**
     * Creates a new attribute for a board object.
     *
     * @param attributeRequest the request object containing the attribute name and board ID
     * @param clz              the class of the attribute to create (either {@link Status} or {@link Type})
     * @return a response object with a status code and message indicating the success or failure of the operation
     * @throws NonUniqueObjectException if an attribute with the same name already exists for the specified board
     * @throws NoSuchElementException   if the specified board does not exist
     * @throws IllegalArgumentException if the attribute name is invalid or the board ID is not provided
     * @throws NullPointerException     if the attribute request object is null
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
     * Deletes an attribute from a board object.
     *
     * @param boardId     the ID of the board containing the attribute
     * @param attributeId the ID of the attribute to delete
     * @param clz         the class of the attribute to delete (either {@link Status} or {@link Type})
     * @return a response object with a status code and message indicating the success or failure of the operation
     * @throws NoSuchElementException   if the specified attribute or board does not exist
     * @throws IllegalArgumentException if the attribute or board ID is invalid
     * @throws NullPointerException     if one of the ID parameters is null
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
     * Retrieves an attribute from a board object.
     *
     * @param attributeId the ID of the attribute to retrieve
     * @param boardId     the ID of the board containing the attribute
     * @param clz         the class of the attribute to retrieve (either {@link Status} or {@link Type})
     * @return a response object with a status code and message indicating the success or failure of the operation, and the retrieved attribute
     * @throws NoSuchElementException   if the specified attribute or board does not exist
     * @throws IllegalArgumentException if the attribute or board ID is invalid
     * @throws NullPointerException     if one of the ID parameters is null
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
     * Retrieves all attributes of a given class (either {@link Status} or {@link Type}) from a board object.
     *
     * @param boardId the ID of the board to retrieve attributes from
     * @param clz     the class of the attributes to retrieve (either {@link Status} or {@link Type})
     * @return a response object with a status code and message indicating the success or failure of the operation, and a list of retrieved attributes
     * @throws NoSuchElementException   if the specified board does not exist
     * @throws IllegalArgumentException if the board ID is invalid
     * @throws NullPointerException     if the board ID is null
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
     * Updates an attribute of a board object with the provided update object request.
     *
     * @param updateObjectRequest the update object request containing the required id's
     * @param clz                 the class of the attribute to update (either {@link Status} or {@link Type})
     * @return a response object with a status code and message indicating the success or failure of the operation, and the updated board object
     * @throws NoSuchElementException   if the specified attribute or board does not exist
     * @throws IllegalArgumentException if the attribute ID or board ID is invalid
     * @throws NoSuchFieldException     if the specified attribute does not exist on the board object
     * @throws NullPointerException     if the update object request object is null
     */
    public Response update(UpdateObjectRequest updateObjectRequest, Class clz) {
        try {
            Validations.validateIDs(updateObjectRequest.getObjectsIdsRequest().getUpdateObjId(),
                    updateObjectRequest.getObjectsIdsRequest().getBoardId());

            return Response.builder()
                    .data(BoardDTO.getBoardFromDB(attributeService.update(updateObjectRequest, clz)))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                    .build();
        } catch (IllegalArgumentException | NoSuchElementException | NoSuchFieldException e) {
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
}
