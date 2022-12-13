package CRM.controller.facades;

import CRM.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoardFacade {

    @Autowired
    private BoardService boardService;
}
