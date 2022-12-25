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
     * This method is used to handle HTTP GET requests to the specified URL (settings/get-user-settings-in-board).
     * The method takes the ids of the user and board and uses them to retrieve all the notifications settings of that user in this specific board.
     *
     * @param objectsIdsRequest The ids of the user and the board where the user settings are to be retrieved from.
     * @return A ResponseEntity object containing the Response object with the retrieved settings and the HTTP status code.
     */
    @GetMapping(value = "get-user-settings-in-board")
    public ResponseEntity<Response> getAllUserSettingsInBoard(@RequestBody ObjectsIdsRequest objectsIdsRequest){
        Response response = settingsFacade.getAllUserSettingsInBoard(objectsIdsRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PatchMapping(value = "user/{userId}/board/{boardId}")
    public ResponseEntity<Response> changeUserSettingInBoard(@PathVariable Long userId, @PathVariable Long boardId, @RequestParam Long settingId, @RequestParam Boolean shouldBeActive){
        return null;
    }
}
