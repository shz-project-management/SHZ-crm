package CRM.controller.controllers;

import CRM.controller.facades.UserFacade;
import CRM.entity.DTO.BoardDTO;
import CRM.entity.DTO.UserDTO;
import CRM.entity.DTO.UserPermissionDTO;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.entity.response.Response;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.naming.NoPermissionException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/user")
@AllArgsConstructor
@CrossOrigin
public class UserController {

    private static Logger logger = LogManager.getLogger(UserController.class.getName());


    @Autowired
    private UserFacade userFacade;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * GetMapping to retrieve a user with a given id.
     *
     * @param userId The id of the user.
     * @return A ResponseEntity with a UserDTO object and the corresponding HTTP status.
     */
    @GetMapping
    public ResponseEntity<Response<UserDTO>> get(@RequestAttribute Long userId) throws AccountNotFoundException {
        Response<UserDTO> response = userFacade.get(userId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * DeleteMapping to delete a user with a given id.
     *
     * @param id The id of the user to be deleted.
     * @return A ResponseEntity with the corresponding HTTP status.
     */
    @DeleteMapping(value = "{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable Long id) {
        Response<Void> response = userFacade.delete(id);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * GetMapping to retrieve all boards of a user with a given id.
     *
     * @param userId The id of the user.
     * @return A ResponseEntity with a map of board names and lists of BoardDTO objects and the corresponding HTTP status.
     */
    @GetMapping(value = "getAll")
    public ResponseEntity<Response<Map<String, List<BoardDTO>>>> getAllBoardsOfUser(@RequestAttribute Long userId) throws NoPermissionException, AccountNotFoundException {
        Response<Map<String, List<BoardDTO>>> response = userFacade.getAllBoardsOfUser(userId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * GetMapping to retrieve all users.
     *
     * @return A ResponseEntity with a list of UserDTO objects and the corresponding HTTP status.
     */
    @GetMapping(value = "get-all-users")
    public ResponseEntity<Response<List<UserDTO>>> getAll() {
        Response<List<UserDTO>> response = userFacade.getAll();
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * GetMapping to retrieve all users in a given board.
     *
     * @param boardId The id of the board.
     * @return A ResponseEntity with a list of UserDTO objects and the corresponding HTTP status.
     */
    @GetMapping(value = "getAll-users-in-board")
    public ResponseEntity<Response<List<UserDTO>>> getAllInBoard(@RequestAttribute Long boardId) throws AccountNotFoundException {
        Response<List<UserDTO>> response = userFacade.getAllInBoard(boardId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * GetMapping to retrieve all user permissions in a given board.
     *
     * @param boardId The id of the board.
     * @return A ResponseEntity with a list of UserPermissionDTO objects and the corresponding HTTP status.
     */
    @GetMapping(value = "getAll-users-permissions")
    public ResponseEntity<Response<List<UserPermissionDTO>>> getAllUserPermissionsInBoard(@RequestAttribute Long boardId) {
        Response<List<UserPermissionDTO>> response = userFacade.getAllUserPermissionsInBoard(boardId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * PostMapping to add a user to a given board.
     *
     * @param objectsIdsRequest The ObjectsIdsRequest object containing the user id and the board id.
     * @param boardId           The id of the board.
     * @return A ResponseEntity with no content.
     */
    @PostMapping(value = "update-user-to-board")
    public ResponseEntity<Response<List<UserPermissionDTO>>> updateUserToBoard(@RequestBody ObjectsIdsRequest objectsIdsRequest, @RequestAttribute Long boardId) throws AccountNotFoundException {
        objectsIdsRequest.setBoardId(boardId);
        Response<List<UserPermissionDTO>> response = userFacade.updateUserToBoard(objectsIdsRequest);
        messagingTemplate.convertAndSend("/userPermission/" + boardId, response);
        return ResponseEntity.noContent().build();
    }
}
