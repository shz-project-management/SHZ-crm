package CRM.entity.DTO;

import CRM.entity.Board;
import CRM.entity.Notification;
import CRM.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NotificationDTO {
    private Long id;

    private String name;

    private String description;

    private LocalDateTime notificationDateTime;

    public static NotificationDTO createNotificationDTO(Notification notification)    {
        return new NotificationDTO(notification.getId(), notification.getName(), notification.getDescription(), notification.getNotificationDateTime());
    }

    public static List<NotificationDTO> createNotificationsDTOList(List<Notification> notificationList) {
        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        for (Notification notification : notificationList) {
            notificationDTOList.add(createNotificationDTO(notification));
        }
        return notificationDTOList;
    }
}
