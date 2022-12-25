package CRM.controller.facades;

import CRM.entity.DTO.BoardDTO;
import CRM.entity.requests.BoardRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.entity.response.Response;
import CRM.service.BoardService;
import CRM.utils.Common;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import CRM.utils.enums.Regex;
import CRM.utils.enums.SuccessMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class BoardFacade {

    @Autowired
    private BoardService boardService;

    /**
     * This function creates a new board. It validates the board name using the NAME regex from the Regex enum,
     * finds the creator user using the creatorUserId from the BoardRequest object, creates a new Board object,
     * and calls the create function in the BoardService class to persist the board to the database.
     *
     * @param boardRequest The request body, containing the necessary information to create a new board.
     * @return A Response object with the status of the create operation and the created board object, or an error message if the operation fails.
     */
    public Response create(BoardRequest boardRequest) {
        try {
            Validations.validateNewBoard(boardRequest);
            BoardDTO boardDTO = BoardDTO.getBoardFromDB(boardService.create(boardRequest));
            return Common.buildSuccessResponse(boardDTO, HttpStatus.CREATED, SuccessMessage.CREATE.toString());
        } catch (AccountNotFoundException | NoSuchElementException e) {
            return Common.buildErrorResponse(e, HttpStatus.BAD_REQUEST);
        } catch (NullPointerException e) {
            return Common.buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a board with the given ID.
     *
     * @param id the ID of the board to delete
     * @return a response object indicating the status of the deletion operation
     * @throws NoSuchElementException if no board with the given ID exists
     */
    public Response delete(Long id) {
        try {
            Validations.validate(id, Regex.ID.getRegex());
            boardService.delete(id);
            return Common.buildSuccessResponse(id, HttpStatus.NO_CONTENT, SuccessMessage.DELETED.toString());
        } catch (IllegalArgumentException | NoSuchElementException e) {
            return Common.buildErrorResponse(e, HttpStatus.BAD_REQUEST);
        } catch (NullPointerException e) {
            return Common.buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This method is used to retrieve a board with the specified id.
     *
     * @param id The id of the board to be retrieved.
     * @return A Response object containing the retrieved board or an error message if the board is not found or the id is invalid.
     * @throws NoSuchElementException   if the board with the specified id is not found.
     * @throws IllegalArgumentException if the specified id is invalid.
     * @throws NullPointerException     if the specified id is null.
     */
    public Response get(Long id) {
        try {
            Validations.validate(id, Regex.ID.getRegex());
            BoardDTO boardDTO = BoardDTO.getBoardFromDB(boardService.get(id));
            return Common.buildSuccessResponse(boardDTO, HttpStatus.OK, SuccessMessage.FOUND.toString());
        } catch (IllegalArgumentException | NoSuchElementException e) {
            return Common.buildErrorResponse(e, HttpStatus.BAD_REQUEST);
        } catch (NullPointerException e) {
            return Common.buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This method is used to retrieve all the boards.
     *
     * @return A Response object containing all the retrieved boards.
     */
    public Response getAll() {
        List<BoardDTO> boardDTOs = BoardDTO.getListOfBoardsFromDB(boardService.getAll());
        return Common.buildSuccessResponse(boardDTOs, HttpStatus.OK, SuccessMessage.FOUND.toString());
    }

    /**
     * This method is used to retrieve all the boards created by a user with the specified id.
     *
     * @param userId The id of the user whose boards are to be retrieved.
     * @return A Response object containing all the retrieved boards or an error message if the user is not found or the id is invalid.
     * @throws IllegalArgumentException if the specified user id is invalid.
     * @throws NullPointerException     if the specified user id is null.
     * @throws NoSuchElementException   if the user with the specified id is not found.
     */
    public Response getAllBoardsOfUser(Long userId) {
        try {
            Validations.validate(userId, Regex.ID.getRegex());
            return new Response.Builder()
                    .data(BoardDTO.getListOfBoardPreviewsFromDB(boardService.getAllBoardsOfUser(userId)))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .build();
        } catch (AccountNotFoundException | IllegalArgumentException | NoSuchElementException e) {
            return new Response.Builder()
                    .statusCode(400)
                    .status(HttpStatus.BAD_REQUEST)
                    .message(ExceptionMessage.NULL_INPUT.toString())
                    .build();
        } catch (NullPointerException e) {
            return new Response.Builder()
                    .statusCode(500)
                    .status(HttpStatus.BAD_REQUEST)
                    .message(ExceptionMessage.NULL_INPUT.toString())
                    .build();
        }
    }

    /**
     * Updates a board in the database.
     *
     * @param board the board to update
     * @return a response object with a status code and message
     * @throws IllegalArgumentException if the board name or ID does not match the expected format
     * @throws NoSuchElementException   if the board to update is not found in the database
     * @throws NullPointerException     if the board object is null
     */
    public Response updateBoard(UpdateObjectRequest board, Long boardId) {
        try {
            Validations.validate(boardId, Regex.ID.getRegex());
            BoardDTO boardDTO = BoardDTO.getBoardFromDB(boardService.updateBoard(board, boardId));
            return Common.buildSuccessResponse(boardDTO, HttpStatus.OK, SuccessMessage.FOUND.toString());
        } catch (IllegalArgumentException | NoSuchElementException | NoSuchFieldException e) {
            return Common.buildErrorResponse(e, HttpStatus.BAD_REQUEST);
        } catch (NullPointerException e) {
            return Common.buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
