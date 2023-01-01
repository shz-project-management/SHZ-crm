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
import java.util.*;

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
    public Section create(ItemRequest itemRequest) throws AccountNotFoundException {
        // find the board from the db
        Board board = Validations.doesIdExists(itemRequest.getBoardId(), boardRepository);

        // get the user and make sure he is legit
        User user;
        try {
            user = Validations.doesIdExists(itemRequest.getUserId(), userRepository);
        } catch (NoSuchElementException e) {
            throw new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
        }

        Item item = Item.createNewItem(itemRequest, board, user);

        // add the item to the items list in the board entity
        board.insertItemToSection(item);

        // save the board in the db
        boardRepository.save(board);

//      return boardRepository.findItemInBoardByItem(item);
        Section section = board.getSectionFromBoard(itemRequest.getSectionId());
        return section;
    }

    /**
     * Delete a list of items from a board.
     *
     * @param ids     the IDs of the items to delete
     * @param boardId the ID of the board containing the items
     * @return the number of items deleted
     */
    @Override
    public int delete(List<Long> ids, long boardId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        List<Section> sections = new ArrayList<>(board.getSections());
        int counter = 0;

        for (Section section : sections) {
            for (Iterator<Item> iterator = section.getItems().iterator(); iterator.hasNext(); ) {
                Item item = iterator.next();
                if (ids.contains(item.getId()) || (item.getParentItem() != null && ids.contains(item.getParentItem().getId()))) {
                    item.getItems().clear();
                    iterator.remove();
                    counter++;
                }
            }
        }

        boardRepository.save(board);
        return counter;
    }

    /**
     * Get a shared content object (item or label) from a board.
     *
     * @param objectsIdsRequest the request object containing the board, section, and shared content IDs to get
     * @return the shared content object
     */
    @Override
    public SharedContent get(ObjectsIdsRequest objectsIdsRequest) {
        Board board = Validations.doesIdExists(objectsIdsRequest.getBoardId(), boardRepository);
        Section section = Common.getSection(board, objectsIdsRequest.getSectionId());
        return Common.getItem(section, objectsIdsRequest.getSearchId());
    }


    /**
     * Update a shared content object (item or label) in a board.
     *
     * @param updateObject the request object containing the updated information for the shared content object
     * @return the updated shared content object
     * @throws NoSuchFieldException if the field to update does not exist in the shared content object
     */
    @Override
    public Section update(UpdateObjectRequest updateObject) throws NoSuchFieldException {
        Board board = Validations.doesIdExists(updateObject.getObjectsIdsRequest().getBoardId(), boardRepository);
        Item item = board.getItemFromSectionById(updateObject.getObjectsIdsRequest().getUpdateObjId(),
                updateObject.getObjectsIdsRequest().getSectionId());

        if (Validations.checkIfFieldIsCustomObject(updateObject.getFieldName())) {
            fieldIsCustomObjectHelper(updateObject, item, board, updateObject.getObjectsIdsRequest().getUpdateObjId());
        } else {
            Common.fieldIsPrimitiveOrKnownObjectHelper(updateObject, item);
        }
        boardRepository.save(board);
        return board.getSectionFromBoard(updateObject.getObjectsIdsRequest().getSectionId());
    }

    /**
     * Get all shared content objects (items or labels) within a shared content object (item or label).
     *
     * @param objectsIdsRequest the request object containing the board, section, and shared content IDs to get the objects within
     * @return the list of shared content objects within the specified object
     */
    @Override
    public List<Item> getAllInItem(ObjectsIdsRequest objectsIdsRequest) {
        Board board = Validations.doesIdExists(objectsIdsRequest.getBoardId(), boardRepository);
        return new ArrayList<>(board.getSectionFromBoard(objectsIdsRequest.getSectionId()).getItems());
    }

    /**
     * Get all shared content objects (items or labels) within a section.
     *
     * @param objectsIdsRequest the request object containing the board and section IDs to get the objects within
     * @return the set of shared content objects within the specified section
     */
    public Set<Item> getAllInSection(ObjectsIdsRequest objectsIdsRequest) {
        Board board = Validations.doesIdExists(objectsIdsRequest.getBoardId(), boardRepository);
        return board.getSectionFromBoard(objectsIdsRequest.getSectionId()).getItems();
    }

    /**
     * Assign a user to an item in the board.
     *
     * @param objectsIdsRequest the request containing the IDs of the board, the item, and the user
     * @return the updated set of user permissions for the board
     */
    @Override
    public Section assignToUser(ObjectsIdsRequest objectsIdsRequest) {
        Board board = Validations.doesIdExists(objectsIdsRequest.getBoardId(), boardRepository);
        User user = Validations.doesIdExists(objectsIdsRequest.getAssignedUserId(), userRepository);
        board.assignUserToItem(objectsIdsRequest.getUpdateObjId(), objectsIdsRequest.getSectionId(), user);
        boardRepository.save(board);
        return board.getSectionFromBoard(objectsIdsRequest.getSectionId());
    }

    /**
     * Helper function for updating a custom object field.
     *
     * @param updateObject the request object containing the updates to be made
     * @param itemId       the id of the item being updated
     * @param item         the item to update
     * @throws NoSuchFieldException if the field does not exist in the item object
     */
    private void fieldIsCustomObjectHelper(UpdateObjectRequest updateObject, Item item, Board board, long itemId) throws NoSuchFieldException {
        Object objClass = Common.getObjectOfField(updateObject.getFieldName());
        if (objClass == null) {
            throw new NoSuchFieldException(ExceptionMessage.FIELD_OBJECT_NOT_EXISTS.toString());
        }

        Object contentObj = board.getObjectByItsClass(((Integer) updateObject.getContent()), objClass,
                updateObject.getObjectsIdsRequest().getSectionId());
        if (contentObj == null) {
            throw new IllegalArgumentException(ExceptionMessage.NO_SUCH_ID.toString());
        }

        Validations.checkIfParentItemIsNotTheSameItem(updateObject.getFieldName(), itemId, Long.valueOf((Integer) updateObject.getContent()));
        Validations.setContentToFieldIfFieldExists(item, updateObject.getFieldName(), contentObj);
    }
}
