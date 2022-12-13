package CRM.controller.facades;

import CRM.response.Response;
import CRM.service.SettingsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SettingsFacade {

    private static Logger logger = LogManager.getLogger(SettingsFacade.class.getName());

    @Autowired
    private SettingsService settingsService;

    public Response getAllUserSettingsInBoard(Long userId, Long boardID){
        // validate the parameters using Validations.validate function
        // call settingsService and ask for all the settings for this user in a specific board
        return null;
    }

    public Response changeUserSettingInBoard(Long userId, Long boardId, Long settingId, Boolean shouldBeActive){
        // validate the parameters using Validations.validate function
        // call settingsService and ask to change the setting for the user in the board
        return null;
    }
}
