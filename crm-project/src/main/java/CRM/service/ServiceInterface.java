package CRM.service;

import CRM.entity.Comment;
import CRM.entity.Item;
import CRM.entity.SharedContent;
import CRM.entity.requests.UpdateObjectRequest;

import java.util.List;

public interface ServiceInterface {
    int delete(List<Long> ids, long boardId);
    SharedContent get(long sectionId, long boardId, long searchId,  Long parentItem);
    SharedContent update(UpdateObjectRequest updateObject, long boardId, long sectionId, long updateObjectId) throws NoSuchFieldException;
    List<SharedContent> getAllInItem(long itemId, long sectionId, long boardId);

}
