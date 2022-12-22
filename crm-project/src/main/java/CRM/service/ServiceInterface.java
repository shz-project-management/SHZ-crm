package CRM.service;

import CRM.entity.SharedContent;
import CRM.entity.requests.UpdateObjectRequest;

import java.util.List;

public interface ServiceInterface {
    int delete(List<Long> ids);
    SharedContent update(UpdateObjectRequest updateObject, long itemId) throws NoSuchFieldException;
    SharedContent get(long id);
    List<SharedContent> getAllInItem(long itemId);

}
