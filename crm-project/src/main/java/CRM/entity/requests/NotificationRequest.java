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
    private Item item;
    private Comment comment;
    private String pastContent;
    private String presentContent;
    private String changedFieldName;

    public static NotificationRequest createAssignRequest(User forUser, Board inBoard, Item item, NotificationSetting notificationType){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.user = forUser;
        notificationRequest.board = inBoard;
        notificationRequest.item = item;
        notificationRequest.notificationType = notificationType;
        return notificationRequest;
    }

    public static NotificationRequest createStatusChangeRequest(User forUser, Board inBoard, Item item, String pastStatus, String presentStatus, NotificationSetting notificationType){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.user = forUser;
        notificationRequest.board = inBoard;
        notificationRequest.item = item;
        notificationRequest.pastContent = pastStatus;
        notificationRequest.presentContent = presentStatus;
        notificationRequest.notificationType = notificationType;
        return notificationRequest;
    }

    public static NotificationRequest createCommentAddedRequest(User forUser, Board inBoard, Item item, Comment addedComment, User commenter, NotificationSetting notificationType){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.user = forUser;
        notificationRequest.board = inBoard;
        notificationRequest.item = item;
        notificationRequest.comment = addedComment;
        notificationRequest.fromUser = commenter;
        notificationRequest.notificationType = notificationType;
        return notificationRequest;
    }

    public static NotificationRequest createDeletedItemRequest(User forUser, Board inBoard, Item item, NotificationSetting notificationType){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.user = forUser;
        notificationRequest.board = inBoard;
        notificationRequest.item = item;
        notificationRequest.notificationType = notificationType;
        return notificationRequest;
    }

    public static NotificationRequest createItemChangeRequest(User forUser, Board inBoard, Item item, String fieldName, String past, String present, NotificationSetting notificationType){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.user = forUser;
        notificationRequest.board = inBoard;
        notificationRequest.item = item;
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
