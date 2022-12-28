package CRM.service;

import CRM.entity.Section;
import CRM.entity.SharedContent;
import CRM.entity.UserPermission;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.requests.UpdateObjectRequest;

import java.util.List;
import java.util.Set;

public interface ServiceInterface {
    int delete(List<Long> ids, long boardId);
    SharedContent get(ObjectsIdsRequest objectsIdsRequest);
    Section update(UpdateObjectRequest updateObject) throws NoSuchFieldException;
    List<SharedContent> getAllInItem(ObjectsIdsRequest objectsIdsRequest);
    Set<UserPermission> assignToUser(ObjectsIdsRequest objectsIdsRequest);

}
