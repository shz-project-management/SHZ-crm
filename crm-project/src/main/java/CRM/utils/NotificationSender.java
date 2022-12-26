package CRM.utils;

import CRM.entity.User;
import CRM.entity.UserSetting;
import CRM.entity.requests.NotificationRequest;
import CRM.repository.*;
import CRM.service.NotificationService;
import CRM.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@Component
public class NotificationSender {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SettingsService settingsService;

    //TODO: DOCUMENTATION

    public void sendNotification(NotificationRequest notificationRequest) throws AccountNotFoundException {
        Validations.checkIfUserExistsInBoard(notificationRequest.getUser().getId(), notificationRequest.getBoard().getId(), userRepository, boardRepository);
        UserSetting userSettingsInBoard = UserSetting.getRelevantUserSetting(notificationRequest,
                settingsService.getNotificationSettingFromDB(notificationRequest.getNotificationType()));//getUserSettingsInBoard(notificationRequest);
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

    public static String createNotificationDescription(NotificationRequest notificationRequest){
        Long type = notificationRequest.getNotificationType().getId();
        switch (type.intValue()){
            case 1:
                return "Item: " + notificationRequest.getItem().getName() + " has been assigned to you in board " +
                        notificationRequest.getBoard().getName();
            case 2:
                return "Item: " + notificationRequest.getItem().getName() + "& status has been changed from " +
                        notificationRequest.getPastContent() + " to " + notificationRequest.getPresentContent();
            case 3:
                return "New comment added on" + notificationRequest.getItem().getName() + ":\n " +
                        notificationRequest.getComment().getDescription();
            case 4:
                return "Item: " + notificationRequest.getItem().getName() + " has been deleted";
            case 5:
                return "Field " + notificationRequest.getChangedFieldName() + " has been changed " +
                        " from " + notificationRequest.getPresentContent() + " to " + notificationRequest.getPresentContent();
            case 6:
                return notificationRequest.getFromUser().getFullName() + " has been added to the board, Welcome!";
            default:
                return null;
        }
    }
}
