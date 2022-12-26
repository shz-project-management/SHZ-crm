package CRM.entity.requests;

import CRM.entity.*;
import CRM.utils.enums.Notifications;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationRequest {
    private User user;
    private Board board;
    private User fromUser;
    private NotificationSetting notificationType;
    private Long itemId;
    private String comment;
    private String pastContent;
    private String presentContent;
    private String changedFieldName;

    public static NotificationRequest createAssignRequest(User forUser, Board inBoard, Long itemId, NotificationSetting notificationType){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.user = forUser;
        notificationRequest.board = inBoard;
        notificationRequest.itemId = itemId;
        notificationRequest.notificationType = notificationType;
        return notificationRequest;
    }

    public static NotificationRequest createStatusChangeRequest(User forUser, Board inBoard, Long itemId, String pastStatus, String presentStatus, NotificationSetting notificationType){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.user = forUser;
        notificationRequest.board = inBoard;
        notificationRequest.itemId = itemId;
        notificationRequest.pastContent = pastStatus;
        notificationRequest.presentContent = presentStatus;
        notificationRequest.notificationType = notificationType;
        return notificationRequest;
    }

    public static NotificationRequest createCommentAddedRequest(User forUser, Board inBoard, Long itemId, String addedComment, User commenter, NotificationSetting notificationType){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.user = forUser;
        notificationRequest.board = inBoard;
        notificationRequest.itemId = itemId;
        notificationRequest.comment = addedComment;
        notificationRequest.fromUser = commenter;
        notificationRequest.notificationType = notificationType;
        return notificationRequest;
    }

    public static NotificationRequest createDeletedItemRequest(Board inBoard, Long itemId, NotificationSetting notificationType){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.board = inBoard;
        notificationRequest.itemId = itemId;
        notificationRequest.fromUser = inBoard.getCreatorUser();
        notificationRequest.notificationType = notificationType;
        return notificationRequest;
    }

    public static NotificationRequest createItemChangeRequest(User forUser, Board inBoard, Long itemId, String fieldName, String past, String present, NotificationSetting notificationType){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.user = forUser;
        notificationRequest.board = inBoard;
        notificationRequest.itemId = itemId;
        notificationRequest.pastContent = past;
        notificationRequest.presentContent = present;
        notificationRequest.changedFieldName = fieldName;
        notificationRequest.notificationType = notificationType;
        return notificationRequest;
    }

    public static NotificationRequest createUserAddedRequest(Board inBoard, User addedUser, NotificationSetting notificationType){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.board = inBoard;
        notificationRequest.fromUser = addedUser;
        notificationRequest.notificationType = notificationType;
        return notificationRequest;
    }
}
