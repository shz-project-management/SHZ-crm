package CRM.utils;

import CRM.entity.User;
import CRM.entity.UserSetting;
import CRM.entity.requests.NotificationRequest;
import CRM.entity.response.Response;
import CRM.repository.*;
import CRM.service.NotificationService;
import CRM.service.SettingsService;
import CRM.utils.enums.ExceptionMessage;
import com.google.api.client.http.HttpStatusCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    public boolean sendNotification(NotificationRequest notificationRequest) {
        try{
            Validations.checkIfUserExistsInBoard(notificationRequest.getUser().getId(), notificationRequest.getBoard().getId(), userRepository, boardRepository);
        }catch (AccountNotFoundException e){
            return false;
        }
        UserSetting userSettingsInBoard = UserSetting.getRelevantUserSetting(notificationRequest,
                settingsService.getNotificationSettingFromDB(notificationRequest.getNotificationType().getName()));
        if (userSettingsInBoard.isInApp()) {
            notificationService.createInAppNotification(notificationRequest, userSettingsInBoard);
        }
        if (userSettingsInBoard.isInEmail()) {
            System.out.println("Sending an email notification");
        }
        return true;
    }

    public void sendNotificationToManyUsers(NotificationRequest notificationRequest, List<User> usersInBoard) {
        for (User userInBoard: usersInBoard) {
            notificationRequest.setUser(userInBoard);
            sendNotification(notificationRequest);
        }
    }

    public static String createNotificationDescription(NotificationRequest notificationRequest){
        Long type = notificationRequest.getNotificationType().getId();
        switch (type.intValue()){
            case 1:
                return "Item id: " + notificationRequest.getItemId() + " has been assigned to you in board " +
                        notificationRequest.getBoard().getName();
            case 2:
                return "Item id: " + notificationRequest.getItemId() + "& status has been changed from " +
                        notificationRequest.getPastContent() + " to " + notificationRequest.getPresentContent();
            case 3:
                return "New comment added on item id: " + notificationRequest.getItemId() + ":\n " +
                        notificationRequest.getComment() + "\n By: " + notificationRequest.getFromUser().getFullName();
            case 4:
                return "Item: " + notificationRequest.getItemId() + " has been deleted";
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
