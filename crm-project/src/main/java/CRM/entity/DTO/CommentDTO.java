package CRM.entity.DTO;

import CRM.entity.Comment;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO extends SharedContentDTO {

    private Long assignedToUserId;

    public static CommentDTO getSharedContentFromDB(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setParentItem(ItemDTO.getParentItem(comment.getParentItem()));
        commentDTO.setUser(UserDTO.createUserDTO(comment.getUser()));
        commentDTO.setTitle(comment.getName());
        commentDTO.setDescription(comment.getDescription());
        commentDTO.setCreationDate(comment.getCreationDate());

        return commentDTO;
    }
}