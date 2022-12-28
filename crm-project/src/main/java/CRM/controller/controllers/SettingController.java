package CRM.controller.controllers;

import CRM.controller.facades.SettingsFacade;
import CRM.entity.DTO.SettingsDTO;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.requests.SettingUpdateRequest;
import CRM.entity.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@Controller
@RequestMapping(value = "/settings")
@AllArgsConstructor
@CrossOrigin
public class SettingController {

    @Autowired
    private SettingsFacade settingsFacade;

    /**
     * GetMapping to retrieve all user settings in a given board.
     *
     * @param userId  The id of the user.
     * @param boardId The id of the board.
     * @return A ResponseEntity with a list of SettingsDTO objects and the corresponding HTTP status.
     */
    @GetMapping(value = "get-user-settings-in-board")
    public ResponseEntity<Response<List<SettingsDTO>>> getAllUserSettingsInBoard(@RequestAttribute Long userId, @RequestAttribute Long boardId) throws AccountNotFoundException {
        ObjectsIdsRequest objectsIdsRequest = ObjectsIdsRequest.boardUserIds(boardId, userId);
        Response<List<SettingsDTO>> response = settingsFacade.getAllUserSettingsInBoard(objectsIdsRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * PatchMapping to change user settings in a given board.
     *
     * @param settingUpdateRequest The SettingUpdateRequest object containing the updated settings and the board id.
     * @param boardId              The id of the board.
     * @return A ResponseEntity with a list of SettingsDTO objects and the corresponding HTTP status.
     */
    @PatchMapping(consumes = "application/json")
    public ResponseEntity<Response<List<SettingsDTO>>> changeUserSettingsInBoard(@RequestBody SettingUpdateRequest settingUpdateRequest, @RequestAttribute Long boardId) {
        settingUpdateRequest.setBoardId(boardId);
        Response<List<SettingsDTO>> response = settingsFacade.changeUserSettingsInBoard(settingUpdateRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
