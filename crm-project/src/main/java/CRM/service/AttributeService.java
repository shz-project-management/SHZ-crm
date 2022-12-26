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
import java.util.Set;

@Service
public class AttributeService {

    @Autowired
    private BoardRepository boardRepository;


    public List<Attribute> create(AttributeRequest attributeRequest, Class clz) {
        Board board = Validations.doesIdExists(attributeRequest.getBoardId(), boardRepository);

        board.addAttributeToBoard(Attribute.createAttribute(attributeRequest.getName(), attributeRequest.getDescription()), clz);
        boardRepository.save(board);

        return board.getAllAttributeInBoard(clz);
    }


    public void delete(long boardId, long attributeId, Class clz) {
        // get the board Id and make sure it exsits
        Board board = Validations.doesIdExists(boardId, boardRepository);

        // get the lists of attributes so we can delete this specific attribute
        board.removeAttribute(attributeId, clz);

        boardRepository.save(board);
    }


    public Attribute get(long attributeId, long boardId, Class clz) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        // create method in the entity and get the attribute from
        return board.getAttributeById(attributeId, clz);
    }


    public Board update(UpdateObjectRequest updateObjReq, Class clz) throws NoSuchFieldException {
        Board board = Validations.doesIdExists(updateObjReq.getObjectsIdsRequest().getBoardId(), boardRepository);
        updateBoard(board, updateObjReq, clz);
        return boardRepository.save(board);
    }

    public List<Attribute> getAllAttributesInBoard(long boardId, Class clz) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        return board.getAllAttributeInBoard(clz);
    }

    private void updateBoard(Board board, UpdateObjectRequest updateObjReq, Class clz) throws NoSuchFieldException {
        Attribute attribute;
        if (clz == Status.class) attribute = board.getStatusById(updateObjReq.getObjectsIdsRequest().getUpdateObjId());
        else attribute = board.getTypeById(updateObjReq.getObjectsIdsRequest().getUpdateObjId());
        Common.fieldIsPrimitiveOrKnownObjectHelper(updateObjReq, attribute);
    }
}
