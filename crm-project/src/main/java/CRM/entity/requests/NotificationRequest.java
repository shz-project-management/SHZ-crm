package CRM.entity.requests;

import CRM.entity.*;
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
    private Object presentContent;
    private String changedFieldName;
    private Long sectionId;

    public static NotificationRequest createAssignRequest(User forUser, Board inBoard, Long itemId, NotificationSetting notificationType){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.user = forUser;
        notificationRequest.board = inBoard;
        notificationRequest.itemId = itemId;
        notificationRequest.notificationType = notificationType;
        return notificationRequest;
    }

    public static NotificationRequest createStatusChangeRequest(User forUser, Board inBoard, Long itemId, Object presentStatus, NotificationSetting notificationType, Long sectionId){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.user = forUser;
        notificationRequest.board = inBoard;
        notificationRequest.itemId = itemId;
        notificationRequest.presentContent = presentStatus;
        notificationRequest.notificationType = notificationType;
        notificationRequest.sectionId = sectionId;
        return notificationRequest;
    }

    public static NotificationRequest createTypeChangeRequest(User forUser, Board inBoard, Long itemId, Object presentStatus, NotificationSetting notificationType, Long sectionId){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.user = forUser;
        notificationRequest.board = inBoard;
        notificationRequest.itemId = itemId;
        notificationRequest.presentContent = presentStatus;
        notificationRequest.notificationType = notificationType;
        notificationRequest.sectionId = sectionId;
        return notificationRequest;
    }

    public static NotificationRequest createCommentAddedRequest(User forUser, Board inBoard, Long itemId, String addedComment, User commenter, NotificationSetting notificationType, Long sectionId){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.user = forUser;
        notificationRequest.board = inBoard;
        notificationRequest.itemId = itemId;
        notificationRequest.comment = addedComment;
        notificationRequest.fromUser = commenter;
        notificationRequest.notificationType = notificationType;
        notificationRequest.sectionId = sectionId;
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

    public static NotificationRequest createItemChangeRequest(User forUser, Board inBoard, Long itemId, String fieldName, Object present, NotificationSetting notificationType, Long sectionId){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.user = forUser;
        notificationRequest.board = inBoard;
        notificationRequest.itemId = itemId;
        notificationRequest.presentContent = present;
        notificationRequest.changedFieldName = fieldName;
        notificationRequest.notificationType = notificationType;
        notificationRequest.sectionId = sectionId;
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
