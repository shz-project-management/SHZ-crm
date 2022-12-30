package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.CommentRequest;
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
import java.util.stream.Collectors;

@Service
public class CommentService implements ServiceInterface {

    private static Logger logger = LogManager.getLogger(CommentService.class.getName());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;

    /**
     * Creates a new Comment object and adds it to the parent Item in the specified Section of the given Board.
     *
     * @param commentRequest a request object containing the userId, boardId, sectionId, and parentItemId of the Comment
     * @return a Set of all Comments in the parent Item
     * @throws AccountNotFoundException if the userId provided does not correspond to an existing User
     * @throws IllegalArgumentException if the parentItemId is null
     */
    public Set<Comment> create(CommentRequest commentRequest , Long userId, Long boardId) throws AccountNotFoundException {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        User user;

        try {
            user = Validations.doesIdExists(userId, userRepository);
        } catch (NoSuchElementException e) {
            throw new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
        }

        if (commentRequest.getParentItemId() == null) {
            throw new IllegalArgumentException(ExceptionMessage.PARENT_ITEM_NOT_FOUND.toString());
        }

        Item parentItem = board.getItemFromSectionById(commentRequest.getParentItemId(), commentRequest.getSectionId());
        Comment comment = Comment.createNewComment(user, commentRequest.getName(), commentRequest.getDescription(), parentItem);
        board.insertCommentToItemInSection(comment, parentItem.getId(), commentRequest.getSectionId());
        boardRepository.save(board);
        return board.getAllCommentsInItem(commentRequest.getSectionId(), parentItem.getId());
    }

    /**
     * Deletes the specified Comments from their parent Items in the given Board.
     *
     * @param ids     a List of the Comment IDs to delete
     * @param boardId the ID of the Board containing the Comments
     * @return the number of Comments removed from Items in the Board
     */
    @Override
    public int delete(List<Long> ids, long boardId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);

        // Find the comments to delete
        List<Comment> commentsToDelete = board.findCommentsByIds(ids);

        // Remove the comments from all items that reference them
        for (Comment comment : commentsToDelete) comment.getParentItem().getComments().remove(comment);

        // Update the board
        int counter = 0;
        for (Section section : board.getSections()) {
            for (Item item : section.getItems()) {
                item.getComments().removeIf(comment -> ids.contains(comment.getId()));
                counter += item.getComments().size();
            }
        }

        // Save the updated board
        boardRepository.save(board);

        return counter;
    }


    /**
     * Updates the specified Comment with the provided field update information.
     *
     * @param updateObject a request object containing the field name and value to update, as well as the ids of the Comment, Item, Section, and Board
     * @return the updated Section containing the Comment
     * @throws NoSuchFieldException if the field name provided is not a valid field of the Comment object
     */
    @Override
    public Section update(UpdateObjectRequest updateObject) throws NoSuchFieldException {
        Board board = Validations.doesIdExists(updateObject.getObjectsIdsRequest().getBoardId(), boardRepository);
        Comment comment = board.getCommentFromItemInSection(updateObject.getObjectsIdsRequest().getUpdateObjId(),
                updateObject.getObjectsIdsRequest().getItemId(),
                updateObject.getObjectsIdsRequest().getSectionId());

        if (Validations.checkIfFieldIsCustomObject(updateObject.getFieldName())) {
            throw new NoSuchFieldException(ExceptionMessage.FIELD_OBJECT_NOT_EXISTS.toString());
        } else {
            Common.fieldIsPrimitiveOrKnownObjectHelper(updateObject, comment);
        }

        boardRepository.save(board);
        return board.getSectionFromBoard(updateObject.getObjectsIdsRequest().getSectionId());
    }

    /**
     * Retrieves the specified Comment from its parent Item in the specified Section of the given Board.
     *
     * @param objectsIdsRequest a request object containing the ids of the Comment, Item, Section, and Board
     * @return the requested Comment
     * @throws NoSuchElementException if the parentItemId is null or if the Comment cannot be found in the parent Item
     */
    @Override
    public SharedContent get(ObjectsIdsRequest objectsIdsRequest) {
        Board board = Validations.doesIdExists(objectsIdsRequest.getBoardId(), boardRepository);
        Section section = Common.getSection(board, objectsIdsRequest.getSectionId());

        if (objectsIdsRequest.getParentId() != null) {
            Item item = Common.getItem(section, objectsIdsRequest.getParentId());
            return Common.getComment(item, objectsIdsRequest.getSearchId());
        }
        throw new NoSuchElementException(ExceptionMessage.PARENT_ITEM_NOT_FOUND.toString());
    }

    /**
     * Retrieves all Comments in the specified Item of the given Board.
     *
     * @param objectsIdsRequest a request object containing the ids of the Item, Section, and Board
     * @return a List of all Comments in the Item
     */
    @Override
    public List<Comment> getAllInItem(ObjectsIdsRequest objectsIdsRequest) {
        Board board = Validations.doesIdExists(objectsIdsRequest.getBoardId(), boardRepository);
        return new ArrayList<>(board.getAllCommentsInItem(objectsIdsRequest.getSectionId(), objectsIdsRequest.getItemId()));
    }

    /**
     * Assigns the specified Comment to the given User.
     *
     * @param objectsIdsRequest a request object containing the ids of the Comment, Item, Section, Board, and User
     * @return a List of all Comments in the Item
     */
    @Override
    public Section assignToUser(ObjectsIdsRequest objectsIdsRequest) {
        return null;
    }

    /**
     * Retrieves all comments in a board.
     *
     * @param boardId the ID of the board to retrieve comments from
     * @return a set of all comments in the board
     * @throws NoSuchElementException if the board does not exist
     */
    public Set<Comment> getAllCommentsInBoard(long boardId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);

        List<Item> items = new ArrayList<>();
        Set<Section> sectionsInBoard = board.getSections();
        sectionsInBoard.forEach(section -> items.addAll(section.getItems()));

        Set<Comment> commentList = new HashSet<>();

        for (Item item : items) {
            commentList.addAll(item.getComments());
        }
        return commentList;
    }


//    //TODO + documentation
//    public Set<Comment> getAllCommentsInSection(ObjectsIdsRequest objectsIdsRequest) {
////        Status status = Validations.doesIdExists(statusId, statusRepository);
////        Set<Item> itemsInStatus = itemRepository.findAllByStatus(status);
//        Set<Comment> commentList = new HashSet<>();
////        for (Item item : itemsInStatus) {
////            commentList.addAll(commentRepository.findAllByParentItem(item));
////        }
//        return commentList;
//    }
}
