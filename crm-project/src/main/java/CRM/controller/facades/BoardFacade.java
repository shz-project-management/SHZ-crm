package CRM.controller.facades;

import CRM.entity.Board;
import CRM.entity.DTO.BoardDTO;
import CRM.entity.requests.BoardRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.entity.response.Response;
import CRM.service.BoardService;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import CRM.utils.enums.Regex;
import CRM.utils.enums.SuccessMessage;
import com.google.api.client.http.HttpStatusCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.naming.NoPermissionException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.NoSuchElementException;

@Component
public class BoardFacade {

    @Autowired
    private BoardService boardService;

    /**
     * Creates a new board with the provided request object.
     *
     * @param boardRequest the request object containing the new board's name and owner ID
     * @return a response object with a status code and message indicating the success or failure of the operation, and the created board object
     * @throws AccountNotFoundException if the specified owner does not exist
     * @throws NoSuchElementException   if the specified board name is already in use
     * @throws IllegalArgumentException if the board name or owner ID is invalid or not provided
     * @throws NullPointerException     if the board request object is null
     */
    public Response create(BoardRequest boardRequest) {
        try {
            Validations.validateNewBoard(boardRequest);

            return Response.builder()
                    .data(BoardDTO.getBoardFromDB(boardService.create(boardRequest)))
                    .message(SuccessMessage.CREATE.toString())
                    .status(HttpStatus.CREATED)
                    .statusCode(HttpStatusCodes.STATUS_CODE_CREATED)
                    .build();

        } catch (IllegalArgumentException | AccountNotFoundException | NoSuchElementException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * Deletes a board with the given ID.
     *
     * @param boardId the ID of the board to delete
     * @return a response object indicating the status of the deletion operation
     * @throws NoSuchElementException if no board with the given ID exists
     */
    public Response delete(Long boardId) {
        try {
            Validations.validate(boardId, Regex.ID.getRegex());
            boardService.delete(boardId);

            return Response.builder()
                    .message(SuccessMessage.DELETED.toString())
                    .status(HttpStatus.NO_CONTENT)
                    .statusCode(HttpStatusCodes.STATUS_CODE_NO_CONTENT)
                    .build();

        } catch (IllegalArgumentException | NoSuchElementException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * Retrieves a board with the specified ID and returns its details, along with the user's permission for the board.
     *
     * @param boardId the ID of the board to retrieve
     * @param userId  the ID of the user whose permission for the board will be returned
     * @return a response object with a status code and message indicating the success or failure of the operation, and the retrieved board object with the user's permission
     * @throws NoSuchElementException   if the specified board or user does not exist
     * @throws IllegalArgumentException if the board ID or user ID is invalid
     * @throws NoPermissionException    if the user does not have permission to view the board
     * @throws NullPointerException     if any of the parameters are null
     */
    public Response get(Long boardId, Long userId) {
        try {
            Validations.validateIDs(boardId, userId);
            Board board = boardService.get(boardId);
            BoardDTO boardDTO = BoardDTO.getBoardFromDB(board);
            boardDTO.setUserPermission(board.getUserPermissionIntegerByUserId(userId));

            return Response.builder()
                    .data(boardDTO)
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                    .build();

        } catch (IllegalArgumentException | NoSuchElementException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .build();
        } catch (NoPermissionException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.FORBIDDEN)
                    .statusCode(HttpStatusCodes.STATUS_CODE_FORBIDDEN)
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * This method is used to retrieve all the boards.
     *
     * @return A Response object containing all the retrieved boards.
     */
    public Response getAll() {
        return Response.builder()
                .data(BoardDTO.getListOfBoardsFromDB(boardService.getAll()))
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
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

            return Response.builder()
                    .data(BoardDTO.getMapWithAllBoardsForUser(boardService.getAllBoardsOfUser(userId)))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                    .build();

        } catch (AccountNotFoundException | IllegalArgumentException | NoSuchElementException e) {
            return Response.builder()
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .status(HttpStatus.BAD_REQUEST)
                    .message(ExceptionMessage.NULL_INPUT.toString())
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(e.getMessage())
                    .build();
        }
    }

    /**
     * Updates a board with the provided update object request.
     *
     * @param updateObjReq the update object request containing the new board name and board ID
     * @return a response object with a status code and message indicating the success or failure of the operation, and the updated board object
     * @throws NoSuchFieldException     if the specified board name does not exist on the board object
     * @throws IllegalArgumentException if the board ID is invalid or not provided
     * @throws NoSuchElementException   if the specified board does not exist
     * @throws NullPointerException     if the update object request object is null
     */
    public Response updateBoard(UpdateObjectRequest updateObjReq) {
        try {
            Validations.validate(updateObjReq.getObjectsIdsRequest().getBoardId(), Regex.ID.getRegex());

            return Response.builder()
                    .data(BoardDTO.getBoardFromDB(boardService.updateBoard(updateObjReq)))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                    .build();

        } catch (NoSuchFieldException | IllegalArgumentException | NoSuchElementException e) {
            return Response.builder()
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .status(HttpStatus.BAD_REQUEST)
                    .message(ExceptionMessage.NULL_INPUT.toString())
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(e.getMessage())
                    .build();
        }
    }
}
