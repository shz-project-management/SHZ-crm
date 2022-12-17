package CRM.controller.facades;

import CRM.service.BoardService;
import CRM.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatusFacade {

    @Autowired
    private StatusService statusService;
}
