package CRM.service;

import CRM.entity.*;
import CRM.entity.DTO.AttributeDTO;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.repository.BoardRepository;
import CRM.repository.StatusRepository;
import CRM.utils.Validations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class StatusService {

    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private BoardRepository boardRepository;

    /**
     * This function persists a new Status to the database by calling the save function in the StatusRepository class.
     * @param status The status object to be persisted.
     * @return The persisted status object.
     */
    public Status create(Attribute status) { // also get the board ID and the type(class) of attribute I want to create
        // check if board even exists

        // board.addAttrubte(status, clz)

        // boardRepository.save(board)

        // get the new attribute from the board and return it.

        if(statusRepository.existsByBoardAndNameLike(status.getBoard(), status.getName()))
            Validations.throwAttributeAlreadyExistsForBoard(status, "Status");
        return statusRepository.save((Status.createStatus(status)));
    }

    /**
     * Deletes the given status from the repository.
     * @param statusId the status ID to delete
     */
    public boolean delete(long statusId) {
        // get the board Id and make sure it exsits

        // delete the attribute by its id using relevant method in board entity

        // return something

        Status status = Validations.doesIdExists(statusId, statusRepository);
        statusRepository.delete(status);
        return true;
    }

    /**
     * This method is used to retrieve a status with the specified id.
     * @param id The id of the status to be retrieved.
     * @return The retrieved status.
     * @throws NoSuchElementException   if the status with the specified id is not found.
     * @throws IllegalArgumentException if the specified id is invalid.
     * @throws NullPointerException     if the specified id is null.
     */
    public Status get(long id) {
        // also get the boardId, and find it in the database

        // create method in the entity and get the attribute from
        return Validations.doesIdExists(id, statusRepository);
    }


    public Status update(UpdateObjectRequest statusRequest, long statusId) throws NoSuchFieldException {
        //get in method signature the board id, get the attributeRequest and attributeId and Class clz (type,section,status)
        //find the board in db
        //check if attribute exists, if it exists get the attribute and update it
        //use update method in the board entity

        Status status = Validations.doesIdExists(statusId, statusRepository);
        Validations.setContentToFieldIfFieldExists(status, statusRequest.getFieldName(), statusRequest.getContent());

        return statusRepository.save(status);
    }

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

    /**
     * This method is used to retrieve all the statuses that belong to the board with the specified id.
     *
     * @param boardId The id of the user whose statuses are to be retrieved.
     * @return A list containing all the statuses that belong to the board with the specified id.
     * @throws NoSuchElementException   if the board with the specified id is not found.
     * @throws IllegalArgumentException if the specified board id is invalid.
     * @throws NullPointerException     if the specified board id is null.
     */
    public List<Status> getAllAttributesInBoard(long boardId) {
        //get Class clz and boardId in method signature
        //getAllAttributeInBoard method in board entity.
        Board board = Validations.doesIdExists(boardId, boardRepository);
        return statusRepository.findByBoard(board);
    }
}
