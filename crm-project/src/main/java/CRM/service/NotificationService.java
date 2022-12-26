package CRM.service;

import CRM.entity.Board;
import CRM.entity.Notification;
import CRM.entity.User;
import CRM.entity.UserSetting;
import CRM.entity.requests.NotificationRequest;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.repository.BoardRepository;
import CRM.repository.NotificationRepository;
import CRM.repository.UserRepository;
import CRM.repository.UserSettingRepository;
import CRM.utils.Common;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserSettingRepository userSettingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;

    //TODO: Refactor and DOCUMENTATION
    public Notification create(NotificationRequest notificationRequest) {
        UserSetting wantedUserSetting = null;
        Notification notification = new Notification();
        List<UserSetting> userSettingsInBoard = userSettingRepository.getUserSettingsInBoard(notificationRequest.getUserId(), notificationRequest.getBoardId());
        for (UserSetting userSetting : userSettingsInBoard) {
            if(Objects.equals(userSetting.getSetting().getNotificationNumber(), notificationRequest.getNotificationNumber()))
                wantedUserSetting = userSetting;
        }
        boolean inApp = wantedUserSetting.isInApp();
        boolean inEmail = wantedUserSetting.isInEmail();
        if (inApp) {
            User user = Common.getUser(notificationRequest.getUserId(), userRepository);
            User fromUser = Common.getUser(notificationRequest.getFromUserId(), userRepository);
            Board board = Common.getBoard(notificationRequest.getBoardId(), boardRepository);
            notification = createInAppNotification(user, fromUser, board, wantedUserSetting);
        }
        if (inEmail) {
            System.out.println("Sending an email notification");
        }
        return notification;
    }

    //TODO:refactor, DOCUMENTATION
    private Notification createInAppNotification(User user, User fromUser, Board board, UserSetting userSetting){
        Notification notification = new Notification(0L, user, board, fromUser, userSetting.getSetting().getName(),
                userSetting.getSetting().getDescription(), LocalDateTime.now());
        notificationRepository.save(notification);
        return notification;
    }

    //TODO:DOCUMENTATION
    public List<Notification> getAllNotificationsForUserInBoard(ObjectsIdsRequest objectsIdsRequest) throws AccountNotFoundException {
        if(!Validations.checkIfUserExistsInBoard(objectsIdsRequest.getUserId(), objectsIdsRequest.getBoardId(), userRepository, boardRepository))
            throw new NoSuchElementException(ExceptionMessage.USER_DOES_NOT_EXISTS_IN_BOARD.toString());
        return notificationRepository.findByUser_IdAndBoard_Id(objectsIdsRequest.getUserId(), objectsIdsRequest.getBoardId());
    }
}
