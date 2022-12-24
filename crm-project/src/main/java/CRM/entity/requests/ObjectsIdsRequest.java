package CRM.entity.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ObjectsIdsRequest {
    private Long boardId;
    private Long sectionId;
    private Long ItemId;
    private Long statusId;
    private Long typeId;
    private Long commentId;
    private Long settingId;
    private Long userId;
    private Long parentId;

}
