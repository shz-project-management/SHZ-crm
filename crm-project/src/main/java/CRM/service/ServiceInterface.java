package CRM.service;

import CRM.entity.SharedContent;
import CRM.entity.requests.UpdateObjectRequest;

import java.util.List;

public interface ServiceInterface {
    int delete(List<Long> ids, long boardId);
//    SharedContent update(UpdateObjectRequest updateObject, long itemId) throws NoSuchFieldException;
    SharedContent get(long sectionId, long boardId, long searchId, Long parentId);
    List<SharedContent> getAllInItem(long itemId, long sectionId, long boardId);
}
