package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.AttributeRequest;
import CRM.repository.BoardRepository;
import CRM.utils.Validations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttributeService {

    @Autowired
    private BoardRepository boardRepository;


    public Attribute create(AttributeRequest attributeRequest, Class clz) {
        Board board = Validations.doesIdExists(attributeRequest.getBoardId(), boardRepository);
        Attribute attribute = Attribute.createAttribute(attributeRequest.getName(), attributeRequest.getDescription());

        board.addAttributeToBoard(attribute, clz);

        boardRepository.save(board);

        return attribute;
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


//    public Status update(UpdateObjectRequest statusRequest, long statusId) throws NoSuchFieldException {
//        //get in method signature the board id, get the attributeRequest and attributeId and Class clz (type,section,status)
//        //find the board in db
//        //check if attribute exists, if it exists get the attribute and update it
//        //use update method in the board entity
//    }

//    /**
//     * This method is used to retrieve all the statuses.
//     *
//     * @return A list containing all the statuses.
//     */
//    @Override
//    public List<Status> getAll() {
//        get Class clz and boardId in method signature
//        findAll method in board entity.
//
//        return statusRepository.findAll();
//    }

    public List<Attribute> getAllAttributesInBoard(long boardId, Class clz) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        return board.getAllAttributeInBoard(clz);
    }
}
