package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.ItemRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.repository.*;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import CRM.utils.enums.UpdateField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ItemService implements ServiceInterface {

    private static Logger logger = LogManager.getLogger(ItemService.class.getName());

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private SectionRepository sectionRepository;

    /**
     * Creates a new item in the system and stores it in the database.
     *
     * @param itemRequest The request object containing the details of the item to be created.
     * @return The newly created item.
     * @throws AccountNotFoundException if the user ID specified in the request object does not correspond to an existing user.
     * @throws NoSuchElementException   if any of the board ID, type ID, or status ID specified in the request object do not correspond to existing entities.
     */
    public Item create(ItemRequest itemRequest) throws AccountNotFoundException {
        User user;
        Item parentItem;

        if (itemRequest.getParentItemId() == null) parentItem = null;
        else parentItem = Validations.doesIdExists(itemRequest.getParentItemId(), itemRepository);

        try {
            user = Validations.doesIdExists(itemRequest.getUserId(), userRepository);
        } catch (NoSuchElementException e) {
            throw new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
        }

        Section section = Validations.doesIdExists(itemRequest.getSectionId(), sectionRepository);
        Type type = Validations.doesIdExists(itemRequest.getTypeId(), typeRepository);
        Status status = Validations.doesIdExists(itemRequest.getStatusId(), statusRepository);

        Item item = Item.createNewItem(section, status, type, user, itemRequest.getTitle(), itemRequest.getDescription(), parentItem, itemRequest.getImportance());
        return itemRepository.save(item);
    }

    /**
     * Deletes a list of items from the system and their associated comments from the database.
     *
     * @param ids The IDs of the items to be deleted.
     * @return The number of items successfully deleted.
     * @throws NoSuchElementException if any of the IDs does not correspond to an existing item.
     */
    @Override
    public int delete(List<Long> ids) {
        int counter = ids.size();
        for (Long id : ids) {
            try {
                Item item = Validations.doesIdExists(id, itemRepository);
                commentRepository.deleteAllByParentItem(item);
            } catch (NoSuchElementException e) {
                counter--;
            }
        }
        itemRepository.deleteAllById(ids);
        return counter;
    }

    /**
     * Update an item field.
     *
     * @param updateObject the request object containing the updates to be made
     * @param itemId       the id of the item to be updated
     * @return the updated item
     * @throws NoSuchFieldException if the field to be updated does not exist in the item object
     */
    @Override
    public Item update(UpdateObjectRequest updateObject, long itemId) throws NoSuchFieldException {
        Item item = Validations.doesIdExists(itemId, itemRepository);
        if (Validations.checkIfFieldIsCustomObject(updateObject.getFieldName())) {
            fieldIsCustomObjectHelper(updateObject, itemId, item);
        } else {
            fieldIsPrimitiveOrKnownObjectHelper(updateObject, item);
        }
        return itemRepository.save(item);
    }

    /**
     * get
     *
     * @param id - the ID of the item to retrieve
     * @return the retrieved item
     * This function receives the ID of an item to retrieve and uses the doesIdExists function from the Validations class to retrieve the item with that ID from the itemRepository.
     * The retrieved item is then returned.
     */
    @Override
    public Item get(long id) {
        return Validations.doesIdExists(id, itemRepository);
    }

    /**
     * Retrieves all shared content items that are within the specified item.
     *
     * @param itemId the ID of the item to retrieve shared content from
     * @return a list of shared content items within the specified item
     * @throws NoSuchElementException if the item with the specified ID does not exist
     */
    @Override
    public List<SharedContent> getAllInItem(long itemId) {
        // checkIfExists
        Item item = Validations.doesIdExists(itemId, itemRepository);
        // returns the list of items retrieved
        return itemRepository.findAllByParentItem(item).stream().map(i -> (SharedContent) i).collect(Collectors.toList());
    }

    /**
     * getAllInBoard
     * This function receives the ID of a board and retrieves all the items in that board.
     * It uses the doesIdExists function from the Validations class to retrieve the board with the provided ID from the boardRepository.
     * The findAllByBoard function from the itemRepository is then called with the retrieved board, returning a list of all the items in that board.
     *
     * @param sectionId - the ID of the board whose items are to be retrieved
     * @return a list of the items in the specified board
     */
    public List<Item> getAllInSection(long sectionId) {
        Section section = Validations.doesIdExists(sectionId, sectionRepository);
        return itemRepository.findAllBySection(section);
    }

    /**
     * Get the repository to update a field.
     *
     * @param fieldName the field to be updated
     * @return the repository to update the field
     * @throws NoSuchFieldException if the field does not have a corresponding repository
     */
    private JpaRepository getRepoToUpdateField(UpdateField fieldName) throws NoSuchFieldException {
        switch (fieldName) {
            case STATUS:
                return statusRepository;
            case TYPE:
                return typeRepository;
            case PARENT_ITEM:
                return itemRepository;
            default:
                throw new NoSuchFieldException(ExceptionMessage.FIELD_OBJECT_REPO_NOT_EXISTS.toString());
        }
    }

    /**
     * Helper function for updating a custom object field.
     *
     * @param updateObject the request object containing the updates to be made
     * @param itemId       the id of the item being updated
     * @param item         the item object being updated
     * @throws NoSuchFieldException if the field does not exist in the item object
     */
    private void fieldIsCustomObjectHelper(UpdateObjectRequest updateObject, long itemId, Item item) throws NoSuchFieldException {
        Object contentObj = Validations.doesIdExists(Long.valueOf((Integer) updateObject.getContent()), getRepoToUpdateField(updateObject.getFieldName()));
        Validations.checkIfParentItemIsNotTheSameItem(updateObject.getFieldName(), itemId, Long.valueOf((Integer) updateObject.getContent()));
        Validations.setContentToFieldIfFieldExists(item, updateObject.getFieldName(), contentObj);
    }

    /**
     * Helper function for updating a primitive or known object field.
     *
     * @param updateObject the request object containing the updates to be made
     * @param item         the item object being updated
     * @throws NoSuchFieldException if the field does not exist in the item object
     */
    private void fieldIsPrimitiveOrKnownObjectHelper(UpdateObjectRequest updateObject, Item item) throws NoSuchFieldException {
        if (Validations.checkIfFieldIsNonPrimitive(updateObject.getFieldName())) {
            LocalDateTime dueDate = LocalDateTime.now().plusDays(Long.valueOf((Integer) updateObject.getContent()));
            Validations.setContentToFieldIfFieldExists(item, updateObject.getFieldName(), dueDate);
        } else {
            Validations.setContentToFieldIfFieldExists(item, updateObject.getFieldName(), updateObject.getContent());
        }
    }
}
