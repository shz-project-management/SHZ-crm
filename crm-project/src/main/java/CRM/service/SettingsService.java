package CRM.service;

import CRM.entity.NotificationSetting;
import CRM.repository.UserInBoardRepository;
import CRM.repository.UserSettingsInBoardRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingsService {

    private static Logger logger = LogManager.getLogger(SettingsService.class.getName());

    @Autowired
    private UserSettingsInBoardRepository userSettingsInBoardRepository;
    @Autowired
    private UserInBoardRepository userInBoardRepository;

    public List<NotificationSetting> getAllUserSettingsInBoard(Long userId, Long boardID){
        // make sure there is such a user in board in the db -> checkIfExists
        // if so, return all the settings for this user. else, throw NoSuchElement exception
        return null;
    }

    public Boolean changeUserSettingInBoard(Long userId, Long boardId, Long settingId, Boolean shouldBeActive){
        // make sure there is such a user in board in the db -> checkIfExists
        // if so, change the setting to the new setting. else, throw NoSuchElement exception
        return null;
    }

    private void checkIfExists(long userId, long boardId){
        // make sure there is such a combination in the db
    }
}
