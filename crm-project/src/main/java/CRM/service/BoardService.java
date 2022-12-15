package CRM.service;

import CRM.entity.Board;
import CRM.entity.User;
import CRM.repository.BoardRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    private static Logger logger = LogManager.getLogger(BoardService.class.getName());

    @Autowired
    private BoardRepository boardRepository;

    /**
     * This function persists a new board to the database by calling the save function in the BoardRepository class.
     *
     * @param board The board object to be persisted.
     * @return The persisted board object.
     */
    public Board create(Board board) {
        return boardRepository.save(board);
    }
}