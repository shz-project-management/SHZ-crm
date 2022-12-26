package CRM.entity.requests;

import CRM.utils.enums.Regex;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    private Long searchId;

    public static ObjectsIdsRequest searchBoardSectionParentIds(Long searchItemId, Long boardId, Long sectionId, Long parentId){
        ObjectsIdsRequest objReq = boardSectionIds(boardId, sectionId);
        objReq.setSearchId(searchItemId);
        objReq.setParentId(parentId);
        return objReq;
    }

    public static ObjectsIdsRequest boardSectionIds(Long boardId, Long sectionId){
        ObjectsIdsRequest objReq = new ObjectsIdsRequest();
        objReq.setBoardId(boardId);
        objReq.setSectionId(sectionId);
        return objReq;
    }

    public static ObjectsIdsRequest boardSectionItemIds(Long boardId, Long sectionId, Long itemId){
        ObjectsIdsRequest objReq = boardSectionIds(boardId, sectionId);
        objReq.setItemId(itemId);
        return objReq;
    }

    public static ObjectsIdsRequest boardUserIds(Long boardId, Long userId){
        ObjectsIdsRequest objReq = new ObjectsIdsRequest();
        objReq.setBoardId(boardId);
        objReq.setUserId(userId);
        return objReq;
    }
}
