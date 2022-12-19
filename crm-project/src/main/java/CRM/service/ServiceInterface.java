package CRM.service;

import CRM.entity.Comment;
import CRM.entity.Item;
import CRM.entity.SharedContent;
import CRM.entity.requests.ItemRequest;

import java.util.List;

public interface ServiceInterface {
    int delete(long id);
    SharedContent update(long id, String field, String content);
    SharedContent get(long id);
    List<SharedContent> getAllInItem(long itemId);

}
