package CRM.entity.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ObjectsIdsRequest {
    private Long boardId;
    private Long sectionId;
    private Long itemId;
    private Long statusId;
    private Long typeId;
    private Long commentId;
    private Long settingId;
    private Long userId;
    private Long parentId;
    private Long permissionId;

}
