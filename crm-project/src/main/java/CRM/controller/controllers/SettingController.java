package CRM.controller.controllers;

import CRM.controller.facades.SettingsFacade;
import CRM.entity.requests.ObjectsIdsRequest;
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
    @GetMapping(value = "get-user-settings-in-board/{boardId}")
    public ResponseEntity<Response> getAllUserSettingsInBoard(@RequestParam Long userId, @PathVariable Long boardId) {
        ObjectsIdsRequest objectsIdsRequest = ObjectsIdsRequest.boardUserIds(userId, boardId);
        Response response = settingsFacade.getAllUserSettingsInBoard(objectsIdsRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PatchMapping(value = "user/{userId}/board/{boardId}")
    public ResponseEntity<Response> changeUserSettingInBoard(@PathVariable Long userId, @PathVariable Long boardId, @RequestParam Long settingId, @RequestParam Boolean shouldBeActive) {
        return null;
    }
}
