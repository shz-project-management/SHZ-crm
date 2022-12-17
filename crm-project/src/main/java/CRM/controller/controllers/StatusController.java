package CRM.controller.controllers;

import CRM.controller.facades.BoardFacade;
import CRM.controller.facades.StatusFacade;
import CRM.entity.requests.BoardRequest;
import CRM.entity.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/status")
@AllArgsConstructor
@CrossOrigin
public class StatusController {

    @Autowired
    private StatusFacade statusFacade;

}
