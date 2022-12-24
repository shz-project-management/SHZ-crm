package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.repository.BoardRepository;
import CRM.repository.SettingRepository;
import CRM.repository.UserRepository;
import CRM.utils.Common;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.security.auth.login.AccountNotFoundException;
import java.util.*;

@Service
public class BoardService {

    private static Logger logger = LogManager.getLogger(BoardService.class.getName());

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SettingRepository settingRepository;
    @Autowired
    private EntityManager entityManager;


    /**
     * This function persists a new board to the database by calling the save function in the BoardRepository class.
     *
     * @param board The board object to be persisted.
     * @return The persisted board object.
     */
    // FIXME: board should be created within the service, and not in the facade
    @Transactional
    public Board create(Board board) {
        User user = board.getCreatorUser();
        //FIXME notificationSetting shouldn't be created this way.
        NotificationSetting notificationSetting = Validations.doesIdExists(2L, settingRepository);
        UserSetting userSetting = UserSetting.createUserSetting(user, board, notificationSetting);
        //FIXME maybe take Entity manager outside the service will be a better practise.
        userSetting = entityManager.merge(userSetting);
        board.addUserSettingToBoard(userSetting);
        return boardRepository.save(board);
    }

    /**
     * Deletes the given board from the repository.
     *
     * @param boardId the board ID to delete
     */
    public boolean delete(long boardId) {
        Board board = Common.getBoard(boardId, boardRepository);
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
        return Common.getBoard(boardId, boardRepository);
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
     * This method is used to retrieve all the boards created by a user with the specified id.
     *
     * @param userId The id of the user whose boards are to be retrieved.
     * @return A list containing all the boards created by the user with the specified id.
     * @throws NoSuchElementException   if the user with the specified id is not found.
     * @throws IllegalArgumentException if the specified user id is invalid.
     * @throws NullPointerException     if the specified user id is null.
     */
    //TODO
    public List<Board> getAllBoardsOfUser(long userId) throws AccountNotFoundException {
        try {
            User user = Validations.doesIdExists(userId, userRepository);
            return null;
//            return user.getBoards().stream().collect(Collectors.toList());
        } catch (NoSuchElementException e) {
            throw new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
        }
    }

    /**
     * Updates a board with the given information.
     *
     * @param boardReq the request object containing the update      information for the board
     * @return the updated board
     * @throws NoSuchFieldException if the boardReq with the given field does not exist
     */
    //TODO
    public Board updateBoard(UpdateObjectRequest boardReq, long boardId) throws NoSuchFieldException {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        Validations.setContentToFieldIfFieldExists(board, boardReq.getFieldName(), boardReq.getContent());
        return boardRepository.save(board);
    }
}
