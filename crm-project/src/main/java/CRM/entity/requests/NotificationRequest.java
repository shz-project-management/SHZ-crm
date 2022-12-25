package CRM.entity.requests;

import CRM.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NotificationRequest {
    private Long userId;
    private Long boardId;
    private Long fromUserId;
    private Long notificationNumber;
}
