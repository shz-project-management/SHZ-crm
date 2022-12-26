package CRM.utils;

import CRM.entity.Notification;
import CRM.entity.User;
import CRM.entity.UserSetting;
import CRM.entity.requests.NotificationRequest;
import CRM.repository.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationSender {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserSettingRepository userSettingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;

    //TODO: DOCUMENTATION

    public void sendNotification(NotificationRequest notificationRequest) throws AccountNotFoundException {
        Validations.checkIfUserExistsInBoard(notificationRequest.getUser().getId(), notificationRequest.getBoard().getId(), userRepository, boardRepository);
        UserSetting userSettingsInBoard = getUserSettingsInBoard(notificationRequest);
        if (userSettingsInBoard.isInApp()) {
            createInAppNotification(notificationRequest, userSettingsInBoard);
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

    //TODO: DOCUMENTATION
    private void createInAppNotification(NotificationRequest notificationRequest, UserSetting userSetting){
        Notification notification = new Notification(0L, notificationRequest.getUser(), notificationRequest.getBoard(),
                notificationRequest.getFromUser(), userSetting.getSetting().getName(),
                createNotificationDescription(notificationRequest), LocalDateTime.now());
        notificationRepository.save(notification);
    }

    private UserSetting getUserSettingsInBoard(NotificationRequest notificationRequest){
        UserSetting wantedUserSetting = null;
        List<UserSetting> userSettingsInBoard = userSettingRepository.getUserSettingsInBoard(notificationRequest.getUser().getId(), notificationRequest.getBoard().getId());
        for (UserSetting userSetting : userSettingsInBoard) {
            if(Objects.equals(userSetting.getSetting().getNotificationNumber(), notificationRequest.getNotificationNumber()))
                wantedUserSetting = userSetting;
        }
        return wantedUserSetting;
    }

    private String createNotificationDescription(NotificationRequest notificationRequest){
        int notificationNumber = Math.toIntExact(notificationRequest.getNotificationNumber());
        switch (notificationNumber){
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
