package CRM.entity;

import CRM.entity.requests.NotificationRequest;
import CRM.utils.NotificationSender;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "users_notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    private String name;

    private String description;

    private LocalDateTime notificationDateTime;

    public static Notification createNewNotification(NotificationRequest notificationRequest, UserSetting userSetting){
        Notification notification = new Notification();
        notification.setUser(notificationRequest.getUser());
        notification.setBoard(notificationRequest.getBoard());
        notification.setFromUser(notificationRequest.getFromUser());
        notification.setDescription(NotificationSender.createNotificationDescription(notificationRequest));
        notification.setName(userSetting.getSetting().getName());
        notification.setNotificationDateTime(LocalDateTime.now());
        return notification;
    }
}
