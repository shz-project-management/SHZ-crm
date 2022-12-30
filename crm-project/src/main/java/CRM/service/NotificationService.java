package CRM.service;

import CRM.entity.Notification;
import CRM.entity.UserSetting;
import CRM.entity.requests.NotificationRequest;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.repository.*;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;

    /**
     * Get all notifications for a user in a board.
     *
     * @param objectsIdsRequest the request object containing the user and board IDs to get the notifications for
     * @return the list of notifications for the user in the board
     * @throws AccountNotFoundException if the user or board with the specified IDs do not exist
     * @throws NoSuchElementException   if the user is not a member of the specified board
     */
    public List<Notification> getAllinBoard(ObjectsIdsRequest objectsIdsRequest) throws AccountNotFoundException {
        if (!Validations.checkIfUserExistsInBoard(objectsIdsRequest.getUserId(), objectsIdsRequest.getBoardId(), userRepository, boardRepository))
            throw new NoSuchElementException(ExceptionMessage.USER_DOES_NOT_EXISTS_IN_BOARD.toString());
        return notificationRepository.findByUser_IdAndBoard_Id(objectsIdsRequest.getUserId(), objectsIdsRequest.getBoardId());
    }

    /**
     * Create a new in-app notification for a user.
     *
     * @param notificationRequest the request object containing the notification information to create
     * @param userSetting         the user's notification settings
     */
    public void createInAppNotification(NotificationRequest notificationRequest, UserSetting userSetting) {
        Notification notification = Notification.createNewNotification(notificationRequest, userSetting);
        notificationRepository.save(notification);
    }

    /**
     * Delete a list of notifications.
     *
     * @param notificationsIds the IDs of the notifications to delete
     */
    public List<Notification> delete(List<Long> notificationsIds) {
        Optional<Notification> notificationOptional = notificationRepository.findById(notificationsIds.get(0));
        if (!notificationOptional.isPresent()) {
            throw new NoSuchElementException(ExceptionMessage.NO_SUCH_ID.toString());
        }
        Notification notification = notificationOptional.get();
        notificationRepository.deleteAllById(notificationsIds);
        return notificationRepository.findByUser_IdAndBoard_Id(notification.getUser().getId(), notification.getBoard().getId());
    }
}
