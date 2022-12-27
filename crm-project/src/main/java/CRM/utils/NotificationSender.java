package CRM.utils;

import CRM.entity.User;
import CRM.entity.UserSetting;
import CRM.entity.requests.NotificationRequest;
import CRM.entity.response.Response;
import CRM.repository.*;
import CRM.service.NotificationService;
import CRM.service.SettingsService;
import CRM.utils.email.EmailUtil;
import CRM.utils.enums.ExceptionMessage;
import com.google.api.client.http.HttpStatusCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
        UserSetting userSettingsInBoard = UserSetting.getRelevantUserSetting(notificationRequest.getBoard(), notificationRequest.getUser().getId(),
                settingsService.getNotificationSettingFromDB(notificationRequest.getNotificationType().getName()).getName());
        if (userSettingsInBoard.isInApp()) {
            notificationService.createInAppNotification(notificationRequest, userSettingsInBoard);
        }
        if (userSettingsInBoard.isInEmail()) {
            try{
                System.out.println("Sending an email notification");
                EmailUtil.send(notificationRequest.getUser().getEmail(), createNotificationDescription(notificationRequest), notificationRequest.getNotificationType().getName());
            }catch (MessagingException | IOException exception){
                throw new MailSendException(ExceptionMessage.EMAIL_SENDING_FAILED.toString());
            }
        }
        return true;
    }

    public void sendNotificationToManyUsers(NotificationRequest notificationRequest, Set<User> usersInBoard) {
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
                return "Item id: " + notificationRequest.getItemId() + " Status has been changed to " +
                        notificationRequest.getPresentContent() + " in item: " + notificationRequest.getItemId();
            case 3:
                return "Item id: " + notificationRequest.getItemId() + " Type has been changed to " +
                        notificationRequest.getPresentContent() + " in item: " + notificationRequest.getItemId();
            case 4:
                return "New comment added on item id: " + notificationRequest.getItemId() + ":\n " +
                        notificationRequest.getComment() + "\n By: " + notificationRequest.getFromUser().getFullName();
            case 5:
                return "Item: " + notificationRequest.getItemId() + " has been deleted";
            case 6:
                return "Field " + notificationRequest.getChangedFieldName() + " has been changed " +
                        " to " + notificationRequest.getPresentContent() + " in item: " + notificationRequest.getItemId();
            case 7:
                return notificationRequest.getFromUser().getFullName() + " has been added to the board, Welcome!";
            default:
                return null;
        }
    }
}
