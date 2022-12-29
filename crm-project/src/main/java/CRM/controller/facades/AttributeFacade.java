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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class AttributeFacade {
    private static Logger logger = LogManager.getLogger(AttributeFacade.class.getName());

    @Autowired
    private AttributeService attributeService;

    /**
     * Method to create a new attribute.
     *
     * @param attributeRequest The AttributeRequest object containing the attribute details.
     * @param clz              The class type of the attribute.
     * @return A Response object with the created attribute as a list of AttributeDTO objects and the corresponding HTTP status.
     */
    public Response<List<AttributeDTO>> create(AttributeRequest attributeRequest, Class<? extends Attribute> clz) {
        Validations.validate(attributeRequest.getName(), Regex.NAME.getRegex());
        return Response.<List<AttributeDTO>>builder()
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatusCodes.STATUS_CODE_CREATED)
                .data(AttributeDTO.getListOfAttributesFromDB(new HashSet<>(attributeService.create(attributeRequest, clz))))
                .build();
    }

    /**
     * Method to delete an attribute with a given id.
     *
     * @param boardId     The id of the board to which the attribute belongs.
     * @param attributeId The id of the attribute to be deleted.
     * @param clz         The class type of the attribute.
     * @return A Response object with the updated board as a BoardDTO object and the corresponding HTTP status.
     */
    public Response<BoardDTO> delete(Long boardId, Long attributeId, Class<? extends Attribute> clz) {
        Validations.validateIDs(attributeId, boardId);
        return Response.<BoardDTO>builder()
                .data(BoardDTO.getBoardFromDB(attributeService.delete(boardId, attributeId, clz)))
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_NO_CONTENT)
                .message(SuccessMessage.DELETED.toString())
                .build();
    }

    /**
     * Method to retrieve an attribute with a given id.
     *
     * @param attributeId The id of the attribute.
     * @param boardId     The id of the board to which the attribute belongs.
     * @param clz         The class type of the attribute.
     * @return A Response object with the attribute as an AttributeDTO object and the corresponding HTTP status.
     */
    public Response<AttributeDTO> get(Long attributeId, Long boardId, Class<? extends Attribute> clz) {
        Validations.validateIDs(attributeId, boardId);
        return Response.<AttributeDTO>builder()
                .data(AttributeDTO.createAttributeDTO(attributeService.get(attributeId, boardId, clz)))
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
    }

    /**
     * Method to retrieve all attributes belonging to a given board.
     *
     * @param boardId The id of the board.
     * @param clz     The class type of the attribute.
     * @return A Response object with the list of attributes as a list of AttributeDTO objects and the corresponding HTTP status.
     */
    public Response<List<AttributeDTO>> getAllAttributesInBoard(Long boardId, Class<? extends Attribute> clz) {
        Validations.validate(boardId, Regex.ID.getRegex());
        Set<Attribute> targetSet = new HashSet<>(attributeService.getAllAttributesInBoard(boardId, clz));
        return Response.<List<AttributeDTO>>builder()
                .data(AttributeDTO.getListOfAttributesFromDB(targetSet))
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
    }

    /**
     * Method to update an attribute with a given id.
     *
     * @param updateObjectRequest The request object containing the updated attribute object and the id of the attribute to be updated.
     * @param clz                 The class type of the attribute.
     * @return A Response object with the updated attribute as an AttributeDTO object and the corresponding HTTP status.
     */
    public Response<BoardDTO> update(UpdateObjectRequest updateObjectRequest, Class<? extends Attribute> clz) throws NoSuchFieldException {
        Validations.validateIDs(updateObjectRequest.getObjectsIdsRequest().getUpdateObjId(),
                updateObjectRequest.getObjectsIdsRequest().getBoardId());
        return Response.<BoardDTO>builder()
                .data(BoardDTO.getBoardFromDB(attributeService.update(updateObjectRequest, clz)))
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
    }
}
