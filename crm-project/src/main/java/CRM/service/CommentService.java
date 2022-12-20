package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.CommentRequest;
import CRM.entity.requests.ItemRequest;
import CRM.repository.*;
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
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private StatusRepository statusRepository;

    /**
     * Creates a new comment based on the information provided in the comment request.
     *
     * @param commentRequest the comment request containing the information for the new comment
     * @return the created comment
     * @throws AccountNotFoundException if the user specified in the comment request does not exist
     */
    public Comment create(CommentRequest commentRequest) throws AccountNotFoundException {
        Item parentItem = Validations.doesIdExists(commentRequest.getParentItemId(), itemRepository);
        User user;
        try {
            user = Validations.doesIdExists(commentRequest.getUserId(), userRepository);
        } catch (NoSuchElementException e) {
            throw new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
        }

        Comment comment = Comment.createNewComment(user, commentRequest.getTitle(), commentRequest.getDescription(), parentItem);
        return commentRepository.save(comment);
    }

    /**
     * Deletes the comments with the specified IDs.
     *
     * @param ids the IDs of the comments to delete
     * @return the number of comments that were successfully deleted
     */
    @Override
    public int delete(List<Long> ids) {
        int counter = ids.size();
        for (Long id : ids) {
            try {
                Validations.doesIdExists(id, commentRepository);
            } catch (NoSuchElementException e) {
                ids.remove(id);
                counter--;
            }
        }
        commentRepository.deleteAllById(ids);
        return counter;
    }

    @Override
    public Comment update(long id, String field, String content) {
        // checkIfExists
        // make sure there is such a field in Item -> use reflection!

        return null;
    }

    /**
     * Retrieves the comment with the specified ID.
     *
     * @param id the ID of the comment to retrieve
     * @return the comment with the specified ID
     * @throws NoSuchElementException if a comment with the given ID does not exist
     */
    @Override
    public Comment get(long id) {
        return Validations.doesIdExists(id, commentRepository);
    }

    /**
     * Retrieves a list of shared content entities associated with a given item.
     *
     * @param itemId The ID of the item to retrieve the shared content entities for.
     * @return A list of shared content entities for the given item.
     * @throws NoSuchElementException if the item with the given ID does not exist.
     */
    @Override
    public List<SharedContent> getAllInItem(long itemId) {
        Item item = Validations.doesIdExists(itemId, itemRepository);
        return commentRepository.findAllByParentItem(item).stream().map(comment -> (SharedContent)comment).collect(Collectors.toList());
    }

    /**
     * Retrieves all comments in the board with the specified ID.
     *
     * @param boardId the ID of the board
     * @return a list of comments in the board
     */
    public List<Comment> getAllCommentsInBoard(long boardId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        Set<Item> itemsInBoard = board.getItems();
        List<Comment> commentList = new ArrayList<>();
        for (Item item : itemsInBoard) {
            commentList.addAll(commentRepository.findAllByParentItem(item));
        }
        return commentList;
    }

    /**
     * Retrieves all comments in the status with the specified ID.
     *
     * @param statusId the ID of the status
     * @return a list of comments in the status
     */
    public List<Comment> getAllCommentsInStatus(long statusId) {
        Status status = Validations.doesIdExists(statusId, statusRepository);
        Set<Item> itemsInStatus = itemRepository.findAllByStatus(status);
        List<Comment> commentList = new ArrayList<>();
        for (Item item : itemsInStatus) {
            commentList.addAll(commentRepository.findAllByParentItem(item));
        }
        return commentList;
    }
}
