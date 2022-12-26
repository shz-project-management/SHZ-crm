package CRM.utils;

import CRM.entity.User;
import CRM.entity.UserSetting;
import CRM.entity.requests.NotificationRequest;
import CRM.repository.*;
import CRM.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Objects;

@Component
public class NotificationSender {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserSettingRepository userSettingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private NotificationService notificationService;

    //TODO: DOCUMENTATION

    public void sendNotification(NotificationRequest notificationRequest) throws AccountNotFoundException {
        Validations.checkIfUserExistsInBoard(notificationRequest.getUser().getId(), notificationRequest.getBoard().getId(), userRepository, boardRepository);
        UserSetting userSettingsInBoard = getUserSettingsInBoard(notificationRequest);
        if (userSettingsInBoard.isInApp()) {
            notificationService.createInAppNotification(notificationRequest, userSettingsInBoard);
        }
        if (userSettingsInBoard.isInEmail()) {
            System.out.println("Sending an email notification");
        }
    }

    public void sendUserAddedNotificationToUsersInBoard(NotificationRequest notificationRequest, List<User> usersInBoard) throws AccountNotFoundException {
        for (User userInBoard: usersInBoard) {
            notificationRequest.setUser(userInBoard);
            sendNotification(notificationRequest);
        }
    }

    private UserSetting getUserSettingsInBoard(NotificationRequest notificationRequest){
        UserSetting wantedUserSetting = null;
        List<UserSetting> userSettingsInBoard = userSettingRepository.getUserSettingsInBoard(notificationRequest.getUser().getId(), notificationRequest.getBoard().getId());
        for (UserSetting userSetting : userSettingsInBoard) {
            if(Objects.equals(userSetting.getSetting().getName(), notificationRequest.getNotificationType().name))
                wantedUserSetting = userSetting;
        }
        return wantedUserSetting;
    }

    public static String createNotificationDescription(NotificationRequest notificationRequest){
        switch (notificationRequest.getNotificationType()){
            case ASSIGNED_TO_ME:
                return "Item: " + notificationRequest.getItem().getName() + " has been assigned to you in board " +
                        notificationRequest.getBoard().getName();
            case STATUS_CHANGED:
                return "Item: " + notificationRequest.getItem().getName() + "& status has been changed from " +
                        notificationRequest.getPastContent() + " to " + notificationRequest.getPresentContent();
            case COMMENT_ADDED:
                return "New comment added on" + notificationRequest.getItem().getName() + ":\n " +
                        notificationRequest.getComment().getDescription();
            case ITEM_DELETED:
                return "Item: " + notificationRequest.getItem().getName() + " has been deleted";
            case ITEM_DATA_CHANGED:
                return "Field " + notificationRequest.getChangedFieldName() + " has been changed " +
                        " from " + notificationRequest.getPresentContent() + " to " + notificationRequest.getPresentContent();
            case USER_ADDED:
                return notificationRequest.getFromUser().getFullName() + " has been added to the board, Welcome!";
            default:
                return null;
        }
    }
}
