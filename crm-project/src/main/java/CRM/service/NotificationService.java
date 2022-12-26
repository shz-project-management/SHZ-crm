package CRM.service;

import CRM.entity.Board;
import CRM.entity.Notification;
import CRM.entity.User;
import CRM.entity.UserSetting;
import CRM.entity.requests.NotificationRequest;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.repository.*;
import CRM.utils.Common;
import CRM.utils.NotificationSender;
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
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private NotificationSettingRepository notificationSettingRepository;

    //TODO:DOCUMENTATION
    public List<Notification> getAllNotificationsForUserInBoard(ObjectsIdsRequest objectsIdsRequest) throws AccountNotFoundException {
        if(!Validations.checkIfUserExistsInBoard(objectsIdsRequest.getUserId(), objectsIdsRequest.getBoardId(), userRepository, boardRepository))
            throw new NoSuchElementException(ExceptionMessage.USER_DOES_NOT_EXISTS_IN_BOARD.toString());
        return notificationRepository.findByUser_IdAndBoard_Id(objectsIdsRequest.getUserId(), objectsIdsRequest.getBoardId());
    }

    //TODO: DOCUMENTATION
    public void createInAppNotification(NotificationRequest notificationRequest, UserSetting userSetting){
        Notification notification = Notification.createNewNotification(notificationRequest, userSetting);
        notificationRepository.save(notification);
    }
}
