package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.ItemRequest;
import CRM.repository.ItemRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService implements ServiceInterface {

    private static Logger logger = LogManager.getLogger(ItemService.class.getName());

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public int delete(long id) {
        // checkIfExists
        // delete it.

        return 0;
    }

    @Override
    public Item update(long id, String field, String content) {
        // checkIfExists
        // make sure there is such a field in Item -> use reflection!

        return null;
    }

    @Override
    public Item get(long id) {
        // checkIfExists
        // return the received item

        return null;
    }

    @Override
    public List<SharedContent> getAllInItem(long itemId) {
        // checkIfExists
        // returns the list of items retrieved

        return null;
    }
}
