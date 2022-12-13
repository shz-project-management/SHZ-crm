package CRM.controller.controllers;

import CRM.controller.facades.SettingsFacade;
import CRM.entity.response.Response;
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

    // TODO:
    //  ------------------------------------------- //
    //  in each endpoint, call SettingsFacade! //
    //  ------------------------------------------- //

    @GetMapping(value = "user/{userId}/board/{boardId}")
    public ResponseEntity<Response> getAllUserSettingsInBoard(@DestinationVariable Long userId, @DestinationVariable Long boardId){
        return null;
    }

    @PatchMapping(value = "user/{userId}/board/{boardId}")
    public ResponseEntity<Response> changeUserSettingInBoard(@DestinationVariable Long userId, @DestinationVariable Long boardId, @RequestParam Long settingId, @RequestParam Boolean shouldBeActive){
        return null;
    }
}
