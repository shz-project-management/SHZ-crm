package CRM.service;

import CRM.entity.SharedContent;

import java.io.FileNotFoundException;
import java.util.List;

public interface ServiceInterface {

    int delete(long id);
    SharedContent update(long id, String field, String content);
    SharedContent get(long id);
    List<SharedContent> getAllInItem(long itemId);

}
