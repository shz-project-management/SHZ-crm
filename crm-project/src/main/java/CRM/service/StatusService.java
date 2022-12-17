package CRM.service;

import CRM.entity.Attribute;
import CRM.entity.Status;
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

    @Override
    public Status create(Attribute status) {
        return statusRepository.save((Status) status);
    }
}
