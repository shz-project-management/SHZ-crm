package CRM.service;

import CRM.entity.Board;
import CRM.entity.NotificationSetting;
import CRM.entity.User;
import CRM.entity.UserSetting;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.requests.SettingUpdateRequest;
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
import java.util.*;

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
     * @throws NoSuchElementException if the user does not belong to that board.
     */
    public List<UserSetting> getAllUserSettingsInBoard(ObjectsIdsRequest objectsIdsRequest) throws AccountNotFoundException {
        if (!Validations.checkIfUserExistsInBoard(objectsIdsRequest.getUserId(), objectsIdsRequest.getBoardId(), userRepository, boardRepository))
            throw new NoSuchElementException(ExceptionMessage.USER_DOES_NOT_EXISTS_IN_BOARD.toString());
        return userSettingRepository.getUserSettingsInBoard(objectsIdsRequest.getUserId(), objectsIdsRequest.getBoardId());
    }

    /**
     * Retrieve a notification setting from the database by its name.
     *
     * @param notificationName the name of the notification setting to retrieve
     * @return the notification setting with the specified name, or null if it does not exist
     */
    public NotificationSetting getNotificationSettingFromDB(String notificationName) {
        Optional<NotificationSetting> notificationSetting = notificationSettingRepository.findByName(notificationName);
        return notificationSetting.orElse(null);
    }

    /**
     * Update a user's notification settings in a board.
     *
     * @param settingUpdateRequest the request object containing the settingId, and notification settings information to update
     * @return the updated user settings
     * @throws NoSuchElementException   if setting/setting id does not exist in the database
     */
    public List<UserSetting> changeUserSettingsInBoard(SettingUpdateRequest settingUpdateRequest) {
        Optional<UserSetting> userSettingOptional = userSettingRepository.findById(settingUpdateRequest.getUserSettingId());
        if (!userSettingOptional.isPresent()){
            throw new NoSuchElementException(ExceptionMessage.FIELD_OBJECT_NOT_EXISTS.toString());
        }
        UserSetting userSetting = userSettingOptional.get();
        if (settingUpdateRequest.getInApp() != null) {
            userSetting.setInApp(settingUpdateRequest.getInApp());
        }
        if (settingUpdateRequest.getInEmail() != null) {
            userSetting.setInEmail(settingUpdateRequest.getInEmail());
        }
        userSettingRepository.save(userSetting);
        return userSettingRepository.getUserSettingsInBoard(userSetting.getUser().getId(), settingUpdateRequest.getBoardId());
    }
}
