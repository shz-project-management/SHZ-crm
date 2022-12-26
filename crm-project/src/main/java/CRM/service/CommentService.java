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
     * Creates a new comment based on the information provided in the comment request.
     *
     * @param commentRequest the comment request containing the information for the new comment
     * @return the created comment
     * @throws AccountNotFoundException if the user specified in the comment request does not exist
     */
    //TODO documentation
    public Set<Comment> create(CommentRequest commentRequest) throws AccountNotFoundException {
        Board board = Validations.doesIdExists(commentRequest.getBoardId(), boardRepository);
        User user;

        try {
            user = Validations.doesIdExists(commentRequest.getUserId(), userRepository);
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
     * Deletes the comments with the specified IDs.
     *
     * @param ids the IDs of the comments to delete
     * @return the number of comments that were successfully deleted
     */
    //TODO documentation
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


    //TODO Documentation
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

    //TODO documentation
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

    //TODO + documentation
    @Override
    public List<SharedContent> getAllInItem(ObjectsIdsRequest objectsIdsRequest) {
        Board board = Validations.doesIdExists(objectsIdsRequest.getBoardId(), boardRepository);
        return new ArrayList<>(board.getAllCommentsInItem(objectsIdsRequest.getSectionId(), objectsIdsRequest.getItemId()));
    }

    //TODO + documentation
    @Override
    public List<SharedContent> assignToUser(ObjectsIdsRequest objectsIdsRequest){
        return null;
    }

    /**
     * Retrieves all comments in the board with the specified ID.
     *
     * @param boardId the ID of the board
     * @return a list of comments in the board
     */
    //TODO documentation
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


    //TODO + documentation
    public Set<Comment> getAllCommentsInSection(ObjectsIdsRequest objectsIdsRequest) {
//        Status status = Validations.doesIdExists(statusId, statusRepository);
//        Set<Item> itemsInStatus = itemRepository.findAllByStatus(status);
        Set<Comment> commentList = new HashSet<>();
//        for (Item item : itemsInStatus) {
//            commentList.addAll(commentRepository.findAllByParentItem(item));
//        }
        return commentList;
    }
}
