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

    @Override
    public Comment get(long id) {
        return Validations.doesIdExists(id, commentRepository);
    }

    @Override
    public List<SharedContent> getAllInItem(long itemId) {
        // checkIfExists
        // returns the list of items retrieved

        return null;
    }

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

    public List<Comment> getAllCommentsInBoard(long boardId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        Set<Item> itemsInBoard = board.getItems();
        List<Comment> commentList = new ArrayList<>();
        for (Item item: itemsInBoard){
            commentList.addAll(commentRepository.findAllByParentItem(item));
        }
        return commentList;
    }

    public List<Comment> getAllCommentsInStatus(long statusId) {
        Status status = Validations.doesIdExists(statusId, statusRepository);
        Set<Item> itemsInStatus = itemRepository.findAllByStatus(status);
        List<Comment> commentList = new ArrayList<>();
        for (Item item: itemsInStatus){
            commentList.addAll(commentRepository.findAllByParentItem(item));
        }
        return commentList;
    }
}
