package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.ItemRequest;
import CRM.repository.*;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;

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

    /**
     * create
     *
     * @param itemRequest - a request object containing information about the item to be created
     * @return the newly created item
     * @throws AccountNotFoundException if the user ID provided in the request does not correspond to an existing user
     *                                  This function receives an item request object and creates a new item based on the information provided in the request.
     *                                  It first initializes variables for the user, parent item, board, type, and status that the new item will have.
     *                                  If the parent item ID provided in the request is null, the parent item variable is set to null. Otherwise, the doesIdExists function from the Validations class is used to retrieve the parent item with the provided ID from the itemRepository.
     *                                  The doesIdExists function is also used to retrieve the user, board, type, and status from the corresponding repositories using the IDs provided in the request.
     *                                  If the user ID does not correspond to an existing user, an AccountNotFoundException is thrown.
     *                                  The createNewItem function from the Item class is then called to create a new item with the retrieved user, board, type, status, and the other information provided in the request.
     *                                  The new item is then saved to the repository and returned.
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

        Board board = Validations.doesIdExists(itemRequest.getBoardId(), boardRepository);
        Type type = Validations.doesIdExists(itemRequest.getTypeId(), typeRepository);
        Status status = Validations.doesIdExists(itemRequest.getStatusId(), statusRepository);

        Item item = Item.createNewItem(board, status, type, user, itemRequest.getTitle(), itemRequest.getDescription(), parentItem, itemRequest.getImportance());
        return itemRepository.save(item);
    }

    /**
     * delete
     *
     * @param ids - a list of IDs for the items to be deleted
     * @return the number of items successfully deleted
     * This function receives a list of IDs for items to be deleted and attempts to delete them.
     * It first initializes a counter to the number of IDs in the list.
     * It then iterates through the list of IDs and tries to retrieve the item with each ID using the doesIdExists function from the Validations class.
     * If an item with the ID is not found, the ID is removed from the list and the counter is decremented.
     * After the iteration is complete, the deleteAllById function from the itemRepository is called with the list of IDs, deleting all the items with those IDs.
     * The function then returns the counter, which is the number of items successfully deleted.
     */
    @Override
    public int delete(List<Long> ids) {
        int counter = ids.size();
        for (Long id : ids) {
            try {
                Item item = Validations.doesIdExists(id, itemRepository);
            } catch (NoSuchElementException e) {
                ids.remove(id);
                counter--;
            }
        }
        itemRepository.deleteAllById(ids);
        return counter;
    }

    @Override
    public Item update(long id, String field, String content) {
        // checkIfExists
        // make sure there is such a field in Item -> use reflection!

        return null;
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

    @Override
    public List<SharedContent> getAllInItem(long itemId) {
        // checkIfExists
        // returns the list of items retrieved

        return null;
    }

    /**
     * getAllInBoard
     *
     * @param boardId - the ID of the board whose items are to be retrieved
     * @return a list of the items in the specified board
     * This function receives the ID of a board and retrieves all the items in that board.
     * It uses the doesIdExists function from the Validations class to retrieve the board with the provided ID from the boardRepository.
     * The findAllByBoard function from the itemRepository is then called with the retrieved board, returning a list of all the items in that board.
     */
    public List<Item> getAllInBoard(long boardId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        return itemRepository.findAllByBoard(board);
    }
}
