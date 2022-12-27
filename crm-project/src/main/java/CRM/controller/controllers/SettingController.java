package CRM.controller.controllers;

import CRM.controller.facades.SettingsFacade;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.requests.SettingUpdateRequest;
import CRM.entity.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/settings")
@AllArgsConstructor
@CrossOrigin
public class SettingController {

    @Autowired
    private SettingsFacade settingsFacade;

    /**
     * This endpoint is used to retrieve all user settings in a specific board.
     *
     * @param userId  The ID of the user for which the settings will be retrieved.
     * @param boardId The ID of the board for which the settings will be retrieved.
     * @return A ResponseEntity object containing the response with the retrieved user settings.
     */
    @GetMapping(value = "{boardId}")
    public ResponseEntity<Response> getAllUserSettingsInBoard(@RequestAttribute Long userId, @PathVariable Long boardId) {
        ObjectsIdsRequest objectsIdsRequest = ObjectsIdsRequest.boardUserIds(boardId, userId);
        Response response = settingsFacade.getAllUserSettingsInBoard(objectsIdsRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PatchMapping(consumes = "application/json")
    public ResponseEntity<Response> changeUserSettingsInBoard(@RequestBody SettingUpdateRequest settingUpdateRequest) {
        Response response = settingsFacade.changeUserSettingsInBoard(settingUpdateRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
