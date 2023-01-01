package CRM.controller.facades;

import CRM.entity.DTO.SectionDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SectionFacade {
    private static Logger logger = LogManager.getLogger(SectionFacade.class.getName());

    @Autowired
    private SectionService sectionService;

    /**
     * Creates a new attribute in the board with the given attribute request.
     *
     * @param sectionRequest the request containing the attribute information
     * @return a response object with the created attribute and relevant status information
     * @throws IllegalArgumentException if the attribute name is invalid
     * @throws NoSuchElementException   if the board with the given id does not exist
     * @throws NullPointerException     if an error occurs while creating the attribute
     */
    public Response<List<SectionDTO>> create(AttributeRequest sectionRequest, Long boardId) {
        Validations.validate(sectionRequest.getName(), Regex.NAME.getRegex());
        Validations.validate(boardId, Regex.ID.getRegex());
        return Response.<List<SectionDTO>>builder()
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatusCodes.STATUS_CODE_CREATED)
                .data(SectionDTO.getSectionsDTOList(sectionService.create(sectionRequest, boardId)))
                .build();

    }

    /**
     * Deletes the attribute with the given id from the board with the given id.
     *
     * @param boardId   the id of the board that the attribute belongs to
     * @param sectionId the id of the attribute to delete
     * @return a response object with relevant status information
     * @throws NoSuchElementException   if the attribute or the board with the given ids do not exist
     * @throws IllegalArgumentException if the attribute or board ids are invalid
     * @throws NullPointerException     if an error occurs while deleting the attribute
     */
    public Response<Void> delete(Long boardId, Long sectionId) {
        Validations.validateIDs(boardId, sectionId);
        sectionService.delete(boardId, sectionId);
        return Response.<Void>builder()
                .status(HttpStatus.NO_CONTENT)
                .statusCode(HttpStatusCodes.STATUS_CODE_NO_CONTENT)
                .message(SuccessMessage.DELETED.toString())
                .build();

    }

    /**
     * This method retrieves an attribute by its ID and the ID of the board it belongs to.
     *
     * @param attributeId the ID of the attribute to be retrieved
     * @param boardId     the ID of the board the attribute belongs to
     * @return a Response object containing the retrieved attribute, a status message and the corresponding HTTP status code
     * @throws NoSuchElementException   if the attribute or board with the provided IDs do not exist
     * @throws IllegalArgumentException if the provided attribute or board IDs are invalid
     * @throws NullPointerException     if any of the provided IDs is null
     */
    public Response<SectionDTO> get(Long attributeId, Long boardId) {
        Validations.validateIDs(boardId, attributeId);
        return Response.<SectionDTO>builder()
                .data(SectionDTO.createSectionDTO(sectionService.get(attributeId, boardId)))
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
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
    public Response<List<SectionDTO>> getAllSectionsInBoard(Long boardId) {
        Validations.validate(boardId, Regex.ID.getRegex());
        return Response.<List<SectionDTO>>builder()
                .data(SectionDTO.getSectionsDTOList(new HashSet<>(sectionService.getAllSectionsInBoard(boardId))))
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
    }

    /**
     * Returns a response with a list of sections that match the given filters for a specific board.
     *
     * @param filters a map of filters to be applied to the sections
     * @param boardId the ID of the board to which the sections belong
     * @return a response with a list of sections that match the given filters
     */

    public Response getFilteredItems(Map<String, List<String>> filters, Long boardId) {
        Validations.validate(boardId, Regex.ID.getRegex());
        return Response.builder()
                .data(SectionDTO.getSectionsDTOList(sectionService.getFilteredItems(filters, boardId)))
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
    }


    /**
     * Updates a section with new data.
     *
     * @param updateItemRequest the request object containing the new data for the section
     * @return a response with the updated section
     * @throws IllegalArgumentException if the input is invalid
     * @throws NoSuchElementException   if the section to be updated does not exist
     * @throws NoSuchFieldException     if the field to be updated does not exist
     * @throws NullPointerException     if an unexpected null value is encountered
     */
    public Response<SectionDTO> update(UpdateObjectRequest updateItemRequest) throws NoSuchFieldException {
        Validations.validateIDs(updateItemRequest.getObjectsIdsRequest().getBoardId(),
                updateItemRequest.getObjectsIdsRequest().getSectionId());
        return Response.<SectionDTO>builder()
                .data(SectionDTO.createSectionDTO(sectionService.update(updateItemRequest)))
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
    }
}
