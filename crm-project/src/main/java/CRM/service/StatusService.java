package CRM.service;

import CRM.entity.*;
import CRM.entity.DTO.AttributeDTO;
import CRM.repository.BoardRepository;
import CRM.repository.StatusRepository;
import CRM.utils.Validations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class StatusService implements AttributeService {

    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private BoardRepository boardRepository;

    @Override
    public int update(long id, Attribute object) {
        return 0;
    }

    /**
     * This function persists a new Status to the database by calling the save function in the StatusRepository class.
     * @param status The status object to be persisted.
     * @return The persisted status object.
     */
    @Override
    public Status create(Attribute status) {
        if(statusRepository.existsByBoardAndNameLike(status.getBoard(), status.getName()))
            Validations.throwAttributeAlreadyExistsForBoard(status, "Status");
        return statusRepository.save((Status.createStatus(status)));
    }

    /**
     * Deletes the given status from the repository.
     * @param statusId the status ID to delete
     */
    @Override
    public boolean delete(Long statusId) {
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
    @Override
    public Status get(Long id) {
        return Validations.doesIdExists(id, statusRepository);
    }

    /**
     * This method is used to retrieve all the statuses.
     *
     * @return A list containing all the statuses.
     */
    @Override
    public List<Status> getAll() {
        return statusRepository.findAll();
    }

    /**
     * This method is used to retrieve all the statuses that belong to the board with the specified id.
     *
     * @param boardId The id of the user whose statuses are to be retrieved.
     * @return A list containing all the statuses that belong to the board with the specified id.
     * @throws NoSuchElementException   if the board with the specified id is not found.
     * @throws IllegalArgumentException if the specified board id is invalid.
     * @throws NullPointerException     if the specified board id is null.
     */
    @Override
    public List<Status> getAllInBoard(Long boardId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        return statusRepository.findByBoard(board);
    }
}
