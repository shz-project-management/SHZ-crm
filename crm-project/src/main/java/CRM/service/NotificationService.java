package CRM.service;

import CRM.entity.Notification;
import CRM.entity.UserSetting;
import CRM.entity.requests.NotificationRequest;
import CRM.repository.BoardRepository;
import CRM.repository.NotificationRepository;
import CRM.repository.UserRepository;
import CRM.repository.UserSettingRepository;
import CRM.utils.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    public Notification create(NotificationRequest notificationRequest) {
        UserSetting wantedUserSetting = null;
        Notification notification = new Notification();
        List<UserSetting> userSettingsInBoard = userSettingRepository.getUserSettingsInBoard(notificationRequest.getUserId(), notificationRequest.getBoardId());
        for (UserSetting userSetting : userSettingsInBoard) {
            if(userSetting.getSetting().getNotificationNumber() == notificationRequest.getNotificationNumber())
                wantedUserSetting = userSetting;
        }
        boolean inApp = wantedUserSetting.isInApp();
        boolean inEmail = wantedUserSetting.isInEmail();
        if (inApp) {
            notification.setNotificationDateTime(LocalDateTime.now());
            notification.setUser(Common.getUser(notificationRequest.getUserId(), userRepository));
            notification.setFromUser(Common.getUser(notificationRequest.getFromUserId(), userRepository));
            notification.setBoard(Common.getBoard(notificationRequest.getBoardId(), boardRepository));
            notification.setName(wantedUserSetting.getSetting().getName());
            notification.setDescription(wantedUserSetting.getSetting().getDescription());
            notificationRepository.save(notification);
        }
        if (inEmail) {
            System.out.println("Sending an email notification");
        }
        return notification;
    }
}
