package CRM.service;

import CRM.entity.Comment;
import CRM.entity.SharedContent;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.requests.UpdateObjectRequest;

import java.util.List;

public interface ServiceInterface {
    int delete(List<Long> ids, long boardId);
    SharedContent get(ObjectsIdsRequest objectsIdsRequest, long searchId);
    SharedContent update(UpdateObjectRequest updateObject, long updateObjectId) throws NoSuchFieldException;
    List<SharedContent> getAllInItem(ObjectsIdsRequest objectsIdsRequest);

}
