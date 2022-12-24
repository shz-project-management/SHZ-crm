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
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CommentService implements ServiceInterface {

    private static Logger logger = LogManager.getLogger(CommentService.class.getName());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;


    /**
     * Creates a new comment based on the information provided in the comment request.
     *
     * @param commentRequest the comment request containing the information for the new comment
     * @return the created comment
     * @throws AccountNotFoundException if the user specified in the comment request does not exist
     */
    public Comment create(CommentRequest commentRequest) throws AccountNotFoundException {
//        Item parentItem = Validations.doesIdExists(commentRequest.getParentItemId(), itemRepository);
        Board board = Common.getBoard(commentRequest.getBoardId(), boardRepository);

        User user;
        try {
            user = Validations.doesIdExists(commentRequest.getUserId(), userRepository);
        } catch (NoSuchElementException e) {
            throw new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
        }
        Item parentItem = null;
        if (commentRequest.getParentItemId() != null)
            parentItem = board.getItemFromSectionById(commentRequest.getParentItemId(), commentRequest.getSectionId());
        else{
            throw new IllegalArgumentException(ExceptionMessage.PARENT_ITEM_NOT_FOUND.toString());
        }

        // build the item
        Comment comment = Comment.createNewComment(user, commentRequest.getName(), commentRequest.getDescription(), parentItem);

        // add the item to the items list in the board entity
        board.insertCommentToItemInSection(comment, parentItem.getId(), commentRequest.getSectionId());
        // save the board in the db
        boardRepository.save(board);
        return comment;
    }

    /**
     * Deletes the comments with the specified IDs.
     *
     * @param ids the IDs of the comments to delete
     * @return the number of comments that were successfully deleted
     */
    @Override
    public int delete(List<Long> ids, long boardId) {
        Board board = Common.getBoard(boardId, boardRepository);
        List<Section> sections = new ArrayList<>(board.getSections());
        int counter = 0;

        for (Section section : sections) {
            for (Item item : section.getItems()) {
                for (Iterator<Comment> commentIterator = item.getComments().iterator(); commentIterator.hasNext(); ) {
                    Comment comment = commentIterator.next();
                    if (ids.contains(comment.getId())) {
                        commentIterator.remove();
                        counter++;
                    }
                }
            }
        }

        boardRepository.save(board);

        return counter;
    }

    /**
     * Updates the specified comment with the new field value provided in the updateObject parameter.
     *
     * @param updateObject the object containing the field names and new field values to update
     * @param commentId    the id of the comment to update
     * @return the updated Comment object
     * @throws NoSuchFieldException if the field to update is not a primitive or a known object
     */
    @Override
    public Comment update(UpdateObjectRequest updateObject, long commentId) throws NoSuchFieldException {
        Board board = Common.getBoard(updateObject.getObjectsIdsRequest().getBoardId(), boardRepository);
//        if (Validations.checkIfFieldIsCustomObject(updateObject.getFieldName())) {
//            throw new NoSuchFieldException(ExceptionMessage.FIELD_OBJECT_NOT_EXISTS.toString());
//        } else {
//            fieldIsPrimitiveOrKnownObjectHelper(updateObject, comment);
//        }
//        return commentRepository.save(comment);
        return null;
    }

    @Override
    public SharedContent get(ObjectsIdsRequest objectsIdsRequest, long searchId) {
        Board board = Common.getBoard(objectsIdsRequest.getBoardId(), boardRepository);
        Section section = Common.getSection(board, objectsIdsRequest.getSectionId());
        if(objectsIdsRequest.getParentId() != null) {
            Item item = Common.getItem(section, objectsIdsRequest.getParentId());
            return Common.getComment(item, searchId);
        }
        throw new NoSuchElementException(ExceptionMessage.PARENT_ITEM_NOT_FOUND.toString());
    }

    @Override
    public List<SharedContent> getAllInItem(ObjectsIdsRequest objectsIdsRequest) {
        //@PathVariable Long boardId, @PathVariable Long sectionId, @PathVariable Long itemId
//        Item item = Validations.doesIdExists(objectsIdsRequest.getItemId(), itemRepository);
//        return commentRepository.findAllByParentItem(item).stream().map(comment -> (SharedContent) comment).collect(Collectors.toList());
        return null;
    }

    /**
     * Retrieves all comments in the board with the specified ID.
     *
     * @param boardId the ID of the board
     * @return a list of comments in the board
     */
    public List<Comment> getAllCommentsInBoard(long boardId) {
        Board board = Common.getBoard(boardId, boardRepository);

        List<Item> items = new ArrayList<>();
        Set<Section> sectionsInBoard = board.getSections();
        sectionsInBoard.forEach(section -> section.getItems().forEach(item -> items.add(item)));

        List<Comment> commentList = new ArrayList<>();

//        for (Item item : items) {
//            commentList.addAll(commentRepository.findAllByParentItem(item));
//        }
        return commentList;
    }


    public List<Comment> getAllCommentsInSection(ObjectsIdsRequest objectsIdsRequest) {
//        Status status = Validations.doesIdExists(statusId, statusRepository);
//        Set<Item> itemsInStatus = itemRepository.findAllByStatus(status);
        List<Comment> commentList = new ArrayList<>();
//        for (Item item : itemsInStatus) {
//            commentList.addAll(commentRepository.findAllByParentItem(item));
//        }
        return commentList;
    }

    /**
     * Helper function for updating a primitive or known object field.
     *
     * @param updateObject the request object containing the updates to be made
     * @param comment      the item object being updated
     * @throws NoSuchFieldException if the field does not exist in the item object
     */
//    private void fieldIsPrimitiveOrKnownObjectHelper(UpdateObjectRequest updateObject, Comment comment) throws NoSuchFieldException {
//        if (Validations.checkIfFieldIsNonPrimitive(updateObject.getFieldName())) {
//            LocalDateTime dueDate = LocalDateTime.now().plusDays(Long.valueOf((Integer) updateObject.getContent()));
//            Validations.setContentToFieldIfFieldExists(comment, updateObject.getFieldName(), dueDate);
//        } else {
//            Validations.setContentToFieldIfFieldExists(comment, updateObject.getFieldName(), updateObject.getContent());
//        }
//    }
}
