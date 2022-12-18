package CRM.service;

import CRM.entity.Attribute;
import CRM.entity.Status;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface AttributeService {

    int update(long id, Attribute object);

    Attribute create(Attribute attribute);

    boolean delete(Long id) throws AccountNotFoundException;

    Attribute get(Long id) throws AccountNotFoundException;

    <T> List<T> getAll();
}
