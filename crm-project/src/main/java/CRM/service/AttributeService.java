package CRM.service;

import CRM.entity.Attribute;
import CRM.entity.Status;

public interface AttributeService {

    int update(long id, Attribute object);

    Attribute create(Attribute attribute);
}
