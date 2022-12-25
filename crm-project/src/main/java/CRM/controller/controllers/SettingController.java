package CRM.controller.controllers;

import CRM.controller.facades.SettingsFacade;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.entity.response.Response;
import CRM.service.BoardService;
import com.sun.mail.iap.ResponseInputStream;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/settings")
@AllArgsConstructor
@CrossOrigin
public class SettingController {

    @Autowired
    private SettingsFacade settingsFacade;


    @GetMapping(value = "get-user-settings-in-board")
    public ResponseEntity<Response> getAllUserSettingsInBoard(@RequestBody ObjectsIdsRequest objectsIdsRequest){
        Response response = settingsFacade.getAllUserSettingsInBoard(objectsIdsRequest.getUserId(),
                objectsIdsRequest.getBoardId());
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PatchMapping(value = "user/{userId}/board/{boardId}")
    public ResponseEntity<Response> changeUserSettingInBoard(@PathVariable Long userId, @PathVariable Long boardId, @RequestParam Long settingId, @RequestParam Boolean shouldBeActive){
        return null;
    }
}
