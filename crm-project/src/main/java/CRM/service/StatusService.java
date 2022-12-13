package CRM.service;

import CRM.entity.Attribute;
import CRM.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusService implements AttributeService {

    @Autowired
    private StatusRepository statusRepository;

    @Override
    public int update(long id, Attribute object) {
        return 0;
    }
}
