package CRM.controller.facades;

import CRM.entity.Board;
import CRM.entity.DTO.BoardDTO;
import CRM.entity.User;
import CRM.entity.requests.BoardRequest;
import CRM.entity.response.Response;
import CRM.service.AuthService;
import CRM.service.BoardService;
import CRM.utils.Validations;
import CRM.utils.enums.Regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;

@Component
public class BoardFacade {

    @Autowired
    private BoardService boardService;

    @Autowired
    private AuthService authService;


    /**
     * This function creates a new board. It validates the board name using the NAME regex from the Regex enum,
     * finds the creator user using the creatorUserId from the BoardRequest object, creates a new Board object,
     * and calls the create function in the BoardService class to persist the board to the database.
     *
     * @param boardRequest The request body, containing the necessary information to create a new board.
     * @return A Response object with the status of the create operation and the created board object, or an error message if the operation fails.
     */
    public Response create(BoardRequest boardRequest) {
        try{
            Validations.validate(boardRequest.getName(), Regex.NAME.getRegex());
            User user = authService.findById(boardRequest.getCreatorUserId());
            Board board = Board.createBoard(user, boardRequest.getName(), boardRequest.getDescription());
            Board dbBoard = boardService.create(board);
            return new Response.Builder()
                    .status(HttpStatus.CREATED)
                    .statusCode(201)
                    .data(BoardDTO.createPlainBoard(dbBoard))
                    .build();
        }catch(AccountNotFoundException | NullPointerException | IllegalArgumentException e) {
            return new Response.Builder().message(e.getMessage()).status(HttpStatus.BAD_REQUEST).statusCode(400).build();
        }
    }
}
