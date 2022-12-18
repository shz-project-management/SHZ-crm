package CRM.entity.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BoardRequest {
    //FIXME: fieldName and content will replace the actual fields such as: "name" and "description"
    private Long creatorUserId;
    private Long boardId;
    private String name;
    private String description;
}
