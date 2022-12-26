package CRM.entity.requests;

import CRM.entity.Board;
import CRM.entity.Comment;
import CRM.entity.Item;
import CRM.entity.User;
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
    private Long notificationNumber;
    private Item item;
    private Comment comment;
    private String pastContent;
    private String presentContent;
    private String changedFieldName;

    public static NotificationRequest createAssignRequest(User forUser, Board inBoard, Item item){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.user = forUser;
        notificationRequest.board = inBoard;
        notificationRequest.item = item;
        notificationRequest.notificationNumber = 1L;
        return notificationRequest;
    }

    public static NotificationRequest createStatusChangeRequest(User forUser, Board inBoard, Item item, String pastStatus, String presentStatus){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.user = forUser;
        notificationRequest.board = inBoard;
        notificationRequest.item = item;
        notificationRequest.pastContent = pastStatus;
        notificationRequest.presentContent = presentStatus;
        notificationRequest.notificationNumber = 2L;
        return notificationRequest;
    }

    public static NotificationRequest createCommentAddedRequest(User forUser, Board inBoard, Item item, Comment addedComment, User commenter){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.user = forUser;
        notificationRequest.board = inBoard;
        notificationRequest.item = item;
        notificationRequest.comment = addedComment;
        notificationRequest.fromUser = commenter;
        notificationRequest.notificationNumber = 3L;
        return notificationRequest;
    }

    public static NotificationRequest createDeletedItemRequest(User forUser, Board inBoard, Item item){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.user = forUser;
        notificationRequest.board = inBoard;
        notificationRequest.item = item;
        notificationRequest.notificationNumber = 4L;
        return notificationRequest;
    }

    public static NotificationRequest createItemChangeRequest(User forUser, Board inBoard, Item item, String fieldName, String past, String present){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.user = forUser;
        notificationRequest.board = inBoard;
        notificationRequest.item = item;
        notificationRequest.pastContent = past;
        notificationRequest.presentContent = present;
        notificationRequest.changedFieldName = fieldName;
        notificationRequest.notificationNumber = 5L;
        return notificationRequest;
    }

    public static NotificationRequest createUserAddedRequest(Board inBoard, User addedUser){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.board = inBoard;
        notificationRequest.fromUser = addedUser;
        notificationRequest.notificationNumber = 6L;
        return notificationRequest;
    }
}
