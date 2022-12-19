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

    private AttributeDTO status;
    private AttributeDTO type;
    private String section;
    private Long assignedToUserId;
    private LocalDateTime dueDate;
    private Integer importance;
    private Long boardId;

    public static CommentDTO getSharedContentFromDB(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();

        if (comment.getParentItem() != null)
            commentDTO.setParentItem(ItemDTO.getSharedContentFromDB(comment.getParentItem()));
        commentDTO.setUser(UserDTO.getUserFromDB(comment.getUser()));
        commentDTO.setTitle(comment.getTitle());
        commentDTO.setDescription(commentDTO.getDescription());

        return commentDTO;
    }
}