package CRM.service;

import CRM.entity.Comment;
import CRM.entity.Item;
import CRM.entity.SharedContent;
import CRM.entity.requests.ItemRequest;
import CRM.entity.requests.UpdateObjectRequest;

import java.util.List;

public interface ServiceInterface {
    int delete(List<Long> ids);
    SharedContent update(UpdateObjectRequest updateObject, long itemId) throws NoSuchFieldException;
    SharedContent get(long id);
    List<SharedContent> getAllInItem(long itemId);

}
