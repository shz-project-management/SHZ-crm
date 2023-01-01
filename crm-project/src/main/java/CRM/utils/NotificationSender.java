package CRM.utils;

import CRM.entity.*;
import CRM.entity.requests.NotificationRequest;
import CRM.repository.*;
import CRM.service.NotificationService;
import CRM.service.SettingsService;
import CRM.utils.email.EmailUtil;
import CRM.utils.enums.Permission;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Set;

@Component
public class NotificationSender {
    private static Logger logger = LogManager.getLogger(NotificationSender.class.getName());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SettingsService settingsService;

    /**
     * Sends a notification to the user specified in the notification request.
     * The notification can be either in-app or email, depending on the user's settings.
     *
     * @param notificationRequest the notification request object containing the details of the notification
     * @return true if the notification was sent successfully, false if the user does not exist in the specified board
     * @throws MailSendException if there was an error sending the email notification
     */
    public void sendNotification(NotificationRequest notificationRequest) throws AccountNotFoundException {
        User user = notificationRequest.getUser();
        Board board = notificationRequest.getBoard();
        Validations.checkIfUserExistsInBoard(user.getId(), board.getId(), userRepository, boardRepository);

        NotificationSetting notificationSetting = settingsService.getNotificationSettingFromDB(notificationRequest.getNotificationType().getName());
        if (notificationSetting == null) return;

        String notificationSettingName = notificationSetting.getName();
        UserSetting userSettingsInBoard = UserSetting.getRelevantUserSetting(board, user.getId(), notificationSettingName);

        if (userSettingsInBoard.isInApp()) {
            notificationService.createInAppNotification(notificationRequest, userSettingsInBoard);
        }
        if (userSettingsInBoard.isInEmail()) {
            EmailUtil.send(notificationRequest.getUser().getEmail(), createNotificationDescription(notificationRequest), notificationRequest.getNotificationType().getName());
        }
    }

    /**
     * Sends a notification to a set of users.
     *
     * @param notificationRequest the notification request object containing the details of the notification
     * @param usersInBoard        the set of users to whom the notification should be sent
     */
    public void sendNotificationToManyUsers(NotificationRequest notificationRequest, Set<User> usersInBoard) throws AccountNotFoundException {
        for (User userInBoard : usersInBoard) {
            notificationRequest.setUser(userInBoard);
            sendNotification(notificationRequest);
        }
    }

    public static String createNotificationDescription(NotificationRequest notificationRequest) {
        Long kind = notificationRequest.getNotificationType().getId();
        Board board = notificationRequest.getBoard();
        Item item = null;
        Long attributeId = null;
        if (kind != 5 && kind != 7)
            item = notificationRequest.getBoard().getItemById(notificationRequest.getItemId(), notificationRequest.getSectionId());
        switch (kind.intValue()) {
            case 1:
                return "Item id: " + notificationRequest.getItemId() + " has been assigned to you in board " + board.getName();
            case 2:
                attributeId = notificationRequest.getPresentContent() != null ? Long.valueOf((Integer) notificationRequest.getPresentContent()) : null;
                Status status = board.getAttributeById(attributeId, Status.class);
                return "The item's '" + item.getName() + "' status has been changed to '" + status.getName() + "'";
            case 3:
                attributeId = notificationRequest.getPresentContent() != null ? Long.valueOf((Integer) notificationRequest.getPresentContent()) : null;
                Type type = board.getAttributeById(attributeId, Type.class);
                return "The item's '" + item.getName() + "' type has been changed to " + type.getName();
            case 4:
                return "New comment added on item '" + item.getName() + "':\n " + notificationRequest.getComment() + "\n By " + notificationRequest.getFromUser().getFullName();
            case 5:
                return "Item: " + notificationRequest.getItemId() + " has been deleted";
            case 6:
                return "The item's '" + item.getName() + "' Field " + notificationRequest.getChangedFieldName() + " has been changed " + " to '" + notificationRequest.getPresentContent() + "'";
            case 7:
                return getAddUserNotificationDescription(notificationRequest, kind);

            default:
                return null;
        }
    }

    private static String getAddUserNotificationDescription(NotificationRequest notificationRequest, Long type) {
        Board board = notificationRequest.getBoard();
        UserPermission userPermission = board.getUserPermissionById(notificationRequest.getFromUser().getId(), board.getUsersPermissions());
        if (notificationRequest.getFromUser() == notificationRequest.getUser())
            switch (userPermission.getPermission()) {
                case USER:
                    notificationRequest.setNotificationType(new NotificationSetting(type, "You are invited to a board"));
                    return "You are invited to the board '" + notificationRequest.getBoard().getName() + "'. Welcome!";
                case LEADER:
                    notificationRequest.setNotificationType(new NotificationSetting(type, "You have been promoted to leader"));
                    return "You have been promoted to leader in board '" + notificationRequest.getBoard().getName() + "'. Good luck!";
                case UNAUTHORIZED:
                    notificationRequest.setNotificationType(new NotificationSetting(type, "You have been removed from board"));
                    return "You have been removed from board '" + notificationRequest.getBoard().getName() + "'";
            }
        else if (userPermission.getPermission() == Permission.USER)
            return notificationRequest.getFromUser().getFullName() + " has been added to the board. Welcome!";

        return notificationRequest.getFromUser().getFullName() + "'s permissions has been changed in this board";
    }
}
