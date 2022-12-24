package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.ItemRequest;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.repository.*;
import CRM.utils.Common;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemService implements ServiceInterface {

    private static Logger logger = LogManager.getLogger(ItemService.class.getName());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;


    /**
     * Creates a new item in the system and stores it in the database.
     *
     * @param itemRequest The request object containing the details of the item to be created.
     * @return The newly created item.
     * @throws AccountNotFoundException if the user ID specified in the request object does not correspond to an existing user.
     * @throws NoSuchElementException   if any of the board ID, type ID, or status ID specified in the request object do not correspond to existing entities.
     */
    public Item create(ItemRequest itemRequest) throws AccountNotFoundException {
        // find the board from the db
        Board board = Common.getBoard(itemRequest.getBoardId(), boardRepository);

        // get the user and make sure he is legit
        User user;
        try {
            user = Common.getUser(itemRequest.getUserId(), userRepository);
        } catch (NoSuchElementException e) {
            throw new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
        }

        // check if this element has a parent
        // FIXME: validate parent
        Item parentItem = null;
        if (itemRequest.getParentItemId() != null)
            parentItem = board.getItemFromSectionById(itemRequest.getParentItemId(), itemRequest.getSectionId());

        // collect sections, statuses and types because it is necesito for the item
        Section section = board.getSectionFromBoard(itemRequest.getSectionId());
        Status status = (Status) board.getAttributeById(itemRequest.getStatusId(), Status.class);
        Type type = (Type) board.getAttributeById(itemRequest.getTypeId(), Type.class);

        // build the item
        Item item = Item.createNewItem(section, status, type, user, itemRequest.getName(), itemRequest.getDescription(), parentItem, itemRequest.getImportance());

        // add the item to the items list in the board entity
        board.insertItemToSection(item, itemRequest.getSectionId());
        // save the board in the db
        boardRepository.save(board);

//      return boardRepository.findItemInBoardByItem(item);
        return item;
    }

    //TODO Documentation
    @Override
    public int delete(List<Long> ids, long boardId) {
        Board board = Common.getBoard(boardId, boardRepository);
        List<Section> sections = new ArrayList<>(board.getSections());
        int counter = 0;

        for (Section section : sections) {
            for (Iterator<Item> iterator = section.getItems().iterator(); iterator.hasNext(); ) {
                Item item = iterator.next();
                if (ids.contains(item.getId())) {
                    iterator.remove();
                    counter++;
                }
            }
        }

        boardRepository.save(board);
        return counter;
    }

    //TODO Documentation
    @Override
    public SharedContent get(ObjectsIdsRequest objectsIdsRequest, long searchId)  {
        Board board = Common.getBoard(objectsIdsRequest.getBoardId(), boardRepository);
        Section section = Common.getSection(board, objectsIdsRequest.getSectionId());
        return Common.getItem(section, searchId);
    }


    //TODO + Documentation
    @Override
    public Comment update(UpdateObjectRequest updateObject, long commentId) throws NoSuchFieldException {
        Board board = Validations.doesIdExists(updateObject.getObjectsIdsRequest().getBoardId(), boardRepository);

//        if (Validations.checkIfFieldIsCustomObject(updateObject.getFieldName())) {
//            fieldIsCustomObjectHelper(updateObject, itemId, item);
//        } else {
//            fieldIsPrimitiveOrKnownObjectHelper(updateObject, item);
//        }
//        return itemRepository.save(item);
        return null;
    }

    //TODO Documentation
    @Override
    public List<SharedContent> getAllInItem(ObjectsIdsRequest objectsIdsRequest) {
        //long itemId, long sectionId, long boardId
        Board board = Common.getBoard(objectsIdsRequest.getBoardId(), boardRepository); // but instead of itemId, put board id

        return new ArrayList<>(board.getSectionFromBoard(objectsIdsRequest.getSectionId()).getItems());
    }

    //TODO Documentation
    public List<Item> getAllInSection(ObjectsIdsRequest objectsIdsRequest) {
        Board board = Validations.doesIdExists(objectsIdsRequest.getBoardId(), boardRepository);

        return new ArrayList<>(board.getSectionFromBoard(objectsIdsRequest.getSectionId()).getItems());
    }


    /**
     * Helper function for updating a custom object field.
     *
     * @param updateObject the request object containing the updates to be made
     * @param itemId       the id of the item being updated
     * @param item         the item object being updated
     * @throws NoSuchFieldException if the field does not exist in the item object
     */
//    private void fieldIsCustomObjectHelper(UpdateObjectRequest updateObject, long itemId, Item item) throws NoSuchFieldException {
//        Object contentObj = Validations.doesIdExists(Long.valueOf((Integer) updateObject.getContent()), getRepoToUpdateField(updateObject.getFieldName()));
//        Validations.checkIfParentItemIsNotTheSameItem(updateObject.getFieldName(), itemId, Long.valueOf((Integer) updateObject.getContent()));
//        Validations.setContentToFieldIfFieldExists(item, updateObject.getFieldName(), contentObj);
//    }

    /**
     * Helper function for updating a primitive or known object field.
     *
     * @param updateObject the request object containing the updates to be made
     * @param item         the item object being updated
     * @throws NoSuchFieldException if the field does not exist in the item object
     */
//    private void fieldIsPrimitiveOrKnownObjectHelper(UpdateObjectRequest updateObject, Item item) throws NoSuchFieldException {
//        if (Validations.checkIfFieldIsNonPrimitive(updateObject.getFieldName())) {
//            LocalDateTime dueDate = LocalDateTime.now().plusDays(Long.valueOf((Integer) updateObject.getContent()));
//            Validations.setContentToFieldIfFieldExists(item, updateObject.getFieldName(), dueDate);
//        } else {
//            Validations.setContentToFieldIfFieldExists(item, updateObject.getFieldName(), updateObject.getContent());
//        }
//    }
}
