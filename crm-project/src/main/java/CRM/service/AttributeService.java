package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.repository.BoardRepository;
import CRM.utils.Common;
import CRM.utils.Validations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttributeService {

    @Autowired
    private BoardRepository boardRepository;


    /**
     * Creates a new attribute and adds it to the board with the given boardId.
     *
     * @param attributeRequest the request object containing information about the attribute to be created
     * @param clz              the class of the attribute to be created, either Type or Status
     * @return a list of all attributes of the given class in the board
     */
    public <T extends Attribute> List<T> create(AttributeRequest attributeRequest, Class<T> clz) {
        Board board = Validations.doesIdExists(attributeRequest.getBoardId(), boardRepository);
        // check if is a force add to the db. if so, push the new attribute request to the db.
        board.addAttributeToBoard(Attribute.createAttribute(attributeRequest.getName(), attributeRequest.getDescription()), clz);
        boardRepository.save(board);
        return board.getAllAttributeInBoard(clz);
    }

    public <T extends Attribute> List<T> checkSimilarityWithDatabase(AttributeRequest attributeRequest, Class<T> clz) {
        Board board = Validations.doesIdExists(attributeRequest.getBoardId(), boardRepository);
        return board.checkSimilarityInDatabase(attributeRequest.getName(), clz);
    }

    /**
     * Deletes the attribute with the given attributeId from the board with the given boardId.
     *
     * @param boardId     the id of the board from which the attribute is to be deleted
     * @param attributeId the id of the attribute to be deleted
     * @param clz         the class of the attribute to be deleted, either Type or Status
     */
    public <T extends Attribute> Board delete(long boardId, long attributeId, Class<T> clz) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        board.removeAttributeFromItems(attributeId, clz);
        board.removeAttribute(attributeId, clz);
        return boardRepository.save(board);
    }

    /**
     * Retrieves the attribute with the given attributeId from the board with the given boardId.
     *
     * @param attributeId the id of the attribute to retrieve
     * @param boardId     the id of the board containing the attribute
     * @param clz         the class of the attribute to retrieve, either Type or Status
     * @return the attribute with the given attributeId
     */
    public <T extends Attribute> T get(long attributeId, long boardId, Class<T> clz) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        return board.getAttributeById(attributeId, clz);
    }

    /**
     * Updates the attribute with the given attributeId in the board with the given boardId.
     *
     * @param updateObjReq the request object containing information about the attribute to be updated
     * @param clz          the class of the attribute to be updated, either Type or Status
     * @return the updated board
     * @throws NoSuchFieldException if the field to be updated does not exist in the attribute
     */
    public <T extends Attribute> Board update(UpdateObjectRequest updateObjReq, Class<T> clz) throws NoSuchFieldException {
        Board board = Validations.doesIdExists(updateObjReq.getObjectsIdsRequest().getBoardId(), boardRepository);
        updateBoard(board, updateObjReq, clz);
        return boardRepository.save(board);
    }

    /**
     * Retrieves a list of all attributes of the given class in the board with the given boardId.
     *
     * @param boardId the id of the board whose attributes are to be retrieved
     * @param clz     the class of the attributes to retrieve, either Type or Status
     * @return a list of all attributes of the given class in the board
     */
    public <T extends Attribute> List<T> getAllAttributesInBoard(long boardId, Class<T> clz) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        return board.getAllAttributeInBoard(clz);
    }

    /**
     * Updates the specified attribute of a board object with the provided update object request.
     *
     * @param board        the board object to update
     * @param updateObjReq the update object request containing the new attribute value
     * @param clz          the class of the attribute to update (either {@link Status} or {@link Type})
     * @throws NoSuchFieldException if the specified attribute does not exist on the board object
     */
    private <T extends Attribute> void updateBoard(Board board, UpdateObjectRequest updateObjReq, Class<T> clz) throws NoSuchFieldException {
        Attribute attribute;
        if (clz == Status.class) attribute = board.getStatus(updateObjReq.getObjectsIdsRequest().getUpdateObjId());
        else attribute = board.getType(updateObjReq.getObjectsIdsRequest().getUpdateObjId());
        Common.fieldIsPrimitiveOrKnownObjectHelper(updateObjReq, attribute);
    }
}
