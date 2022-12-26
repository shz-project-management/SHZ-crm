package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.BoardRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.repository.BoardRepository;
import CRM.repository.NotificationSettingRepository;
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


    //TODO documentation
    @Transactional
    public Board create(BoardRequest boardRequest) throws AccountNotFoundException {
        User user;
        try {
            user = Validations.doesIdExists(boardRequest.getCreatorUserId(), userRepository);
        } catch (NoSuchElementException e) {
            throw new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
        }
        Board board = Board.createBoard(user, boardRequest.getName(), boardRequest.getDescription());
        Common.createDefaultSettingForNewUserInBoard(user,board,notificationSettingRepository, entityManager);
        return boardRepository.save(board);
    }

    /**
     * Deletes the given board from the repository.
     *
     * @param boardId the board ID to delete
     */
    public boolean delete(long boardId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
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
     * This method is used to retrieve all the boards created by a user with the specified id.
     *
     * @param userId The id of the user whose boards are to be retrieved.
     * @return A list containing all the boards created by the user with the specified id.
     * @throws NoSuchElementException   if the user with the specified id is not found.
     * @throws IllegalArgumentException if the specified user id is invalid.
     * @throws NullPointerException     if the specified user id is null.
     */
    //TODO
    public Map<String, List<Board>> getAllBoardsOfUser(long userId) throws AccountNotFoundException {
        User user;
        try {
            user = Validations.doesIdExists(userId, userRepository);
        } catch (NoSuchElementException e) {
            throw new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
        }

        //get all the boards of the creator user
        Map<String,List<Board>> userBoards = new HashMap<>();
        userBoards.put(myBoards, boardRepository.findByCreatorUser(user));
        userBoards.put(SharedBoards, getSharedBoardsOfUser(user));
        return userBoards;
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

    private void createDefaultSettingForNewUserInBoard(User user, Board board) {
        List<NotificationSetting> notificationSettingList = notificationSettingRepository.findAll();
        for (NotificationSetting notificationSetting : notificationSettingList) {
            UserSetting userSetting = UserSetting.createUserSetting(user, notificationSetting);
            board.addUserSettingToBoard(userSetting);
        }
    }

    private List<Board> getSharedBoardsOfUser(User user){
        //get all the boards of the user he is shared with
        List<Board> allBoards = boardRepository.findAll();
        List<Board> sharedBoards = new ArrayList<>();
        for (Board board: allBoards) {
            if(board.getAllUsersInBoard().contains(user)){
                sharedBoards.add(board);
            }
        }
        return sharedBoards;
    }
}
