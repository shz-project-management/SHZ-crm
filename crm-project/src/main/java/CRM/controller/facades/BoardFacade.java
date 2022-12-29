package CRM.controller.facades;

import CRM.entity.Board;
import CRM.entity.DTO.BoardDTO;
import CRM.entity.requests.BoardRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.entity.response.Response;
import CRM.service.BoardService;
import CRM.utils.Validations;
import CRM.utils.enums.Regex;
import CRM.utils.enums.SuccessMessage;
import com.google.api.client.http.HttpStatusCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.naming.NoPermissionException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
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
    public Response<BoardDTO> create(BoardRequest boardRequest) throws AccountNotFoundException {
        Validations.validateNewBoard(boardRequest);
        return Response.<BoardDTO>builder()
                .data(BoardDTO.getBoardFromDB(boardService.create(boardRequest)))
                .message(SuccessMessage.CREATE.toString())
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatusCodes.STATUS_CODE_CREATED)
                .build();
    }

    /**
     * Deletes a board with the given ID.
     *
     * @param boardId the ID of the board to delete
     * @return a response object indicating the status of the deletion operation
     * @throws NoSuchElementException if no board with the given ID exists
     */
    public void delete(Long boardId) {
        Validations.validate(boardId, Regex.ID.getRegex());
        boardService.delete(boardId);
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
    public Response<BoardDTO> get(Long boardId, Long userId) throws NoPermissionException {
        Validations.validateIDs(boardId, userId);
        Board board = boardService.get(boardId);
        BoardDTO boardDTO = BoardDTO.getBoardFromDB(board);
        boardDTO.setUserPermission(board.getUserPermissionIntegerByUserId(userId));
        return Response.<BoardDTO>builder()
                .data(boardDTO)
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
    }

    /**
     * This method is used to retrieve all the boards.
     *
     * @return A Response object containing all the retrieved boards.
     */
    public Response<List<BoardDTO>> getAll() {
        return Response.<List<BoardDTO>>builder()
                .data(BoardDTO.getListOfBoardsFromDB(boardService.getAll()))
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
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
    public Response<BoardDTO> updateBoard(UpdateObjectRequest updateObjReq) throws NoSuchFieldException {
        Validations.validate(updateObjReq.getObjectsIdsRequest().getBoardId(), Regex.ID.getRegex());
        return Response.<BoardDTO>builder()
                .data(BoardDTO.getBoardFromDB(boardService.updateBoard(updateObjReq)))
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
    }
}
