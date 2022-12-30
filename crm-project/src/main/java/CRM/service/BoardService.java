package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.BoardRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.repository.BoardRepository;
import CRM.repository.NotificationRepository;
import CRM.repository.NotificationSettingRepository;
import CRM.repository.UserRepository;
import CRM.utils.Common;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import CRM.utils.enums.Permission;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.security.auth.login.AccountNotFoundException;
import java.util.*;

import static CRM.utils.Util.SharedBoards;
import static CRM.utils.Util.myBoards;

@Service
public class BoardService {

    private static Logger logger = LogManager.getLogger(BoardService.class.getName());

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationSettingRepository notificationSettingRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private NotificationRepository notificationRepository;


    /**
     * Creates a new board with the given request parameters.
     *
     * @param boardRequest a request object containing the creator user id and the name and description of the board
     * @return the created board
     * @throws AccountNotFoundException if the creator user id does not correspond to a user in the system
     */
    @Transactional
    public Board create(BoardRequest boardRequest) throws AccountNotFoundException {
        User user;
        try {
            user = Validations.doesIdExists(boardRequest.getCreatorUserId(), userRepository);
        } catch (NoSuchElementException e) {
            throw new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
        }
        if(!user.getId().equals(boardRequest.getCreatorUserId())){
            throw new IllegalArgumentException(ExceptionMessage.USER_REQUEST_DOSENT_MATCH.toString());
        }
        Board board = Board.createBoard(user, boardRequest.getName(), boardRequest.getDescription());
        Common.createDefaultSettingForNewUserInBoard(user, board, notificationSettingRepository, entityManager);
        boardRepository.save(board);
        board.setStatuses(Status.defaultStatuses());
        board.setTypes(Type.defaultTypes());
        board.getUsersPermissions().add(UserPermission.newUserPermission(board.getCreatorUser(), Permission.ADMIN));
        return boardRepository.save(board);
    }

    /**
     * Deletes the given board from the repository.
     *
     * @param boardId the board ID to delete
     */
    public boolean delete(long boardId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        notificationRepository.deleteByBoard(board);
        boardRepository.delete(board);
        return true;
    }

    /**
     * This method is used to retrieve a board with the specified id.
     *
     * @param boardId The id of the board to be retrieved.
     * @return The retrieved board.
     * @throws NoSuchElementException   if the board with the specified id is not found.
     * @throws IllegalArgumentException if the specified id is invalid.
     * @throws NullPointerException     if the specified id is null.
     */
    public Board get(long boardId) {
        return Validations.doesIdExists(boardId, boardRepository);
    }

    /**
     * This method is used to retrieve all the boards.
     *
     * @return A list containing all the boards.
     */
    public List<Board> getAll() {
        return boardRepository.findAll();
    }

    /**
     * Updates the specified board with the provided update object request.
     *
     * @param updateObjReq the update object request containing the field and value to update
     * @return the updated board
     * @throws NoSuchFieldException if the field to update does not exist in the board object
     */
    public Board updateBoard(UpdateObjectRequest updateObjReq) throws NoSuchFieldException {
        Board board = Validations.doesIdExists(updateObjReq.getObjectsIdsRequest().getBoardId(), boardRepository);
        Common.fieldIsPrimitiveOrKnownObjectHelper(updateObjReq, board);
        return boardRepository.save(board);
    }
}
