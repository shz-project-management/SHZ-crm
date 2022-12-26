package CRM.service;

import CRM.entity.NotificationSetting;
import CRM.entity.UserSetting;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.repository.BoardRepository;
import CRM.repository.NotificationSettingRepository;
import CRM.repository.UserRepository;
import CRM.repository.UserSettingRepository;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import CRM.utils.enums.Notifications;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SettingsService {

    private static Logger logger = LogManager.getLogger(SettingsService.class.getName());
    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    UserSettingRepository userSettingRepository;
    @Autowired
    NotificationSettingRepository notificationSettingRepository;

    /**
     * This method is used to retrieve user's notification settings in a specified board.
     *
     * @param objectsIdsRequest contains the user and board ids which we will use to retrieve the settings.
     * @return A Response object containing all the retrieved settings or an error message if the user does not belong to that board.
     * @throws NoSuchElementException   if the user does not belong to that board.
     */
    public List<UserSetting> getAllUserSettingsInBoard(ObjectsIdsRequest objectsIdsRequest) throws AccountNotFoundException {
        if(!Validations.checkIfUserExistsInBoard(objectsIdsRequest.getUserId(), objectsIdsRequest.getBoardId(), userRepository, boardRepository))
            throw new NoSuchElementException(ExceptionMessage.USER_DOES_NOT_EXISTS_IN_BOARD.toString());
        return userSettingRepository.getUserSettingsInBoard(objectsIdsRequest.getUserId(), objectsIdsRequest.getBoardId());
    }

    public Boolean changeUserSettingInBoard(Long userId, Long boardId, Long settingId, Boolean shouldBeActive){
        // make sure there is such a user in board in the db -> checkIfExists
        // if so, change the setting to the new setting. else, throw NoSuchElement exception
        return null;
    }

    public NotificationSetting getNotificationSettingFromDB(String notificationName){
        Optional<NotificationSetting> notificationSetting = notificationSettingRepository.findByName(notificationName);
        return notificationSetting.orElse(null);
    }
}
