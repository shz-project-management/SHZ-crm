package CRM.controller.controllers;

import CRM.controller.facades.BoardFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BoardController {

    @Autowired
    private BoardFacade boardFacade;
}