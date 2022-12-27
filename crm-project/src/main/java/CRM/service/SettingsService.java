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

    public Boolean changeUserSettingInBoard(Long userId, Long boardId, Long settingId, Boolean shouldBeActive) {
        // make sure there is such a user in board in the db -> checkIfExists
        // if so, change the setting to the new setting. else, throw NoSuchElement exception
        return null;
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
     * @param settingUpdateRequest the request object containing the user, board, and notification settings information to update
     * @return the list of updated user settings
     * @throws AccountNotFoundException if the user or board with the specified IDs do not exist
     * @throws NoSuchElementException   if the user is not a member of the specified board
     */
    public List<UserSetting> changeUserSettingsInBoard(SettingUpdateRequest settingUpdateRequest) throws AccountNotFoundException {
        if (!Validations.checkIfUserExistsInBoard(settingUpdateRequest.getUserId(), settingUpdateRequest.getBoardId(), userRepository, boardRepository))
            throw new NoSuchElementException(ExceptionMessage.USER_DOES_NOT_EXISTS_IN_BOARD.toString());

        Board board = Validations.doesIdExists(settingUpdateRequest.getBoardId(), boardRepository);
        User user = Validations.doesIdExists(settingUpdateRequest.getUserId(), userRepository);

        UserSetting userSetting = UserSetting.getRelevantUserSetting(board, user, settingUpdateRequest.getNotificationName());

        if (settingUpdateRequest.getInApp() != null) {
            userSetting.setInApp(settingUpdateRequest.getInApp());
        }
        if (settingUpdateRequest.getInEmail() != null) {
            userSetting.setInEmail(settingUpdateRequest.getInEmail());
        }

        Set<UserSetting> userSettingsInBoard = board.getUsersSettings();
        userSettingsInBoard.add(userSetting);
        board.setUsersSettings(userSettingsInBoard);

        boardRepository.save(board);
        return Arrays.asList(userSetting);
    }
}
