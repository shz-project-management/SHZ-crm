package CRM.entity;

import lombok.*;

import javax.persistence.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "comments")
public class Comment extends SharedContent {
    private Long assignedToUserId;

    public static Comment createNewComment(User user, String name, String description, Item parentItem){
        Comment Comment = new Comment();
        Comment.setUser(user);
        Comment.setParentItem(parentItem);
        Comment.setDescription(description);
        Comment.setName(name);
        return Comment;
    }
}
