package CRM.service;

import CRM.entity.Board;
import CRM.entity.User;
import CRM.entity.UserInBoard;
import CRM.entity.requests.BoardRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.repository.BoardRepository;
import CRM.repository.UserInBoardRepository;
import CRM.repository.UserRepository;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class BoardService {

    private static Logger logger = LogManager.getLogger(BoardService.class.getName());

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInBoardRepository userInBoardRepository;

    /**
     * This function persists a new board to the database by calling the save function in the BoardRepository class.
     *
     * @param board The board object to be persisted.
     * @return The persisted board object.
     */
    public Board create(Board board) {
        Board dbBoard = boardRepository.save(board);
        userInBoardRepository.save(UserInBoard.adminUserInBoard(dbBoard.getCreatorUser(), dbBoard));
        return dbBoard;
    }

    /**
     * Deletes the given board from the repository.
     *
     * @param boardId the board ID to delete
     */
    public boolean delete(long boardId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        userInBoardRepository.deleteAllByBoard(board);
        boardRepository.delete(board);
        return true;
    }

    /**
     * This method is used to retrieve a board with the specified id.
     *
     * @param id The id of the board to be retrieved.
     * @return The retrieved board.
     * @throws NoSuchElementException   if the board with the specified id is not found.
     * @throws IllegalArgumentException if the specified id is invalid.
     * @throws NullPointerException     if the specified id is null.
     */
    public Board get(long id) {
        return Validations.doesIdExists(id, boardRepository);
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
    public List<Board> getAllBoardsOfUser(long userId) throws AccountNotFoundException {
        try {
            User user = Validations.doesIdExists(userId, userRepository);
            List<UserInBoard> userInBoard = userInBoardRepository.findAllBoardByUser(user);
            return userInBoard.stream().map(UserInBoard::getBoard).collect(Collectors.toList());
        } catch (NoSuchElementException e) {
            throw new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
        }
    }

    /**
     * Updates a board with the given information.
     *
     * @param boardReq the request object containing the update information for the board
     * @return the updated board
     * @throws AccountNotFoundException if the board with the given id does not exist
     */
    public Board updateBoard(UpdateObjectRequest boardReq, long boardId) throws NoSuchFieldException {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        Validations.setContentToFieldIfFieldExists(board, boardReq.getFieldName(), boardReq.getContent());
        return boardRepository.save(board);
    }
}
