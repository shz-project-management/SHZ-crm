package CRM.service;

import CRM.entity.Attribute;
import CRM.entity.Status;
import CRM.entity.requests.UpdateObjectRequest;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface AttributeService {

    Attribute create(Attribute attribute);

    boolean delete(long id);

    Attribute get(long id);

    <T> List<T> getAll();

    <T> List<T> getAllInBoard(long boardId);

    Attribute update(UpdateObjectRequest attributeRequest, long id) throws NoSuchFieldException;
}
