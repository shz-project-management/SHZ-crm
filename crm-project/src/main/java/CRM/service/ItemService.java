package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.ItemRequest;
import CRM.repository.ItemRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ItemService implements ServiceInterface {

    private static Logger logger = LogManager.getLogger(ItemService.class.getName());

    @Autowired
    private ItemRepository itemRepository;


    public Item get(long itemId) {
        // Ask for the repo to find the item, by the given id input -> checkIfExists()
        // Since the repo returns an option, check if this option is not empty
        // If it is empty, throw "FileNotFound"

        // Return the item back to the controller
        return null;
    }

    @Override
    public int delete(long id) {
        // checkIfExists
        // delete it.

        return 0;
    }

    @Override
    public SharedContent update(long id, String field, String content) {
        return null;
    }

    @Override
    public Item create(ItemRequest item) {
        // checkIfExists
        // make sure there is such a field in Item -> use reflection!

        return null;
    }


    @Override
    public List<SharedContent> getAllInItem(long itemId) {
        // checkIfExists
        // returns the list of items retrieved

        return null;
    }

    private Item checkIfExists(long id){
        Optional<Item> item = itemRepository.findById(id);
        if(!item.isPresent()) throw new NoSuchElementException("Couldn't find this item in the database!");

        return item.get();
    }
}
