package CRM.entity.DTO;

import CRM.entity.Comment;
import CRM.entity.Item;
import lombok.*;

import java.util.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO extends SharedContentDTO {

    private Long id;
    private Long assignedToUserId;

    public static CommentDTO getSharedContentFromDB(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setParentItem(ItemDTO.getParentItem(comment.getParentItem()));
        commentDTO.setUser(UserDTO.createUserDTO(comment.getUser()));
        commentDTO.setTitle(comment.getName());
        commentDTO.setDescription(comment.getDescription());
        commentDTO.setCreationDate(comment.getCreationDate());
        commentDTO.setId(comment.getId());

        return commentDTO;
    }

    public static List<CommentDTO> getCommentDTOList(Set<Comment> comments) {
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (Comment comment : comments) {
            commentDTOList.add(getSharedContentFromDB(comment));
        }

        commentDTOList.sort(Comparator.comparingLong(CommentDTO::getId));

        return commentDTOList;
    }
}