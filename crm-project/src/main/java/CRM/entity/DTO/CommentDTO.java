package CRM.entity.DTO;

import CRM.entity.Comment;
import CRM.entity.Item;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO extends SharedContentDTO {

    private Long assignedToUserId;

    public static CommentDTO getSharedContentFromDB(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setParentItem(ItemDTO.getSharedContentFromDB(comment.getParentItem()));
        commentDTO.setUser(UserDTO.getUserFromDB(comment.getUser()));
        commentDTO.setTitle(comment.getName());
        commentDTO.setDescription(comment.getDescription());
        commentDTO.setCreationDate(comment.getCreationDate());

        return commentDTO;
    }
}