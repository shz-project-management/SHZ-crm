package CRM.service;

import CRM.entity.Board;
import CRM.entity.User;
import CRM.repository.BoardRepository;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BoardService {

    private static Logger logger = LogManager.getLogger(BoardService.class.getName());

    @Autowired
    private BoardRepository boardRepository;

    /**
     * This function persists a new board to the database by calling the save function in the BoardRepository class.
     * @param board The board object to be persisted.
     * @return The persisted board object.
     */
    public Board create(Board board) {
        return boardRepository.save(board);
    }

    /**
     * Deletes the given board from the repository.
     * @param board the board to delete
     */
    public void delete(Board board) {
        boardRepository.delete(board);
    }

    /**
     * Finds and returns a board with the given ID.
     * @param boardID the ID of the board to find
     * @return the board with the given ID
     * @throws NoSuchElementException if no item with the specified ID exists in the repository
     */
    public Board findById(long boardID){
        return Validations.doesIdExists(boardID, boardRepository);
    }
}