package CRM.service;

import CRM.entity.Attribute;
import CRM.entity.Board;
import CRM.entity.Status;
import CRM.repository.StatusRepository;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import org.hibernate.NonUniqueObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.NoSuchElementException;

@Service
public class StatusService implements AttributeService {

    @Autowired
    private StatusRepository statusRepository;

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
    public boolean delete(Long statusId) throws AccountNotFoundException {
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
    public Status get(Long id) throws AccountNotFoundException {
        return Validations.doesIdExists(id, statusRepository);
    }
}
