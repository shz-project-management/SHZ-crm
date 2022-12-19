package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.CommentRequest;
import CRM.entity.requests.ItemRequest;
import CRM.repository.CommentRepository;
import CRM.repository.ItemRepository;
import CRM.repository.UserRepository;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CommentService implements ServiceInterface {

    private static Logger logger = LogManager.getLogger(CommentService.class.getName());

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public int delete(List<Long> ids) {
        return 0;
    }

    @Override
    public Comment update(long id, String field, String content) {
        // checkIfExists
        // make sure there is such a field in Item -> use reflection!

        return null;
    }

    @Override
    public Comment get(long id) {
        // checkIfExists
        // return the received item

        return null;
    }

    @Override
    public List<SharedContent> getAllInItem(long itemId) {
        // checkIfExists
        // returns the list of items retrieved

        return null;
    }

    public Comment create(CommentRequest commentRequest) throws AccountNotFoundException {
        User user;
        Item parentItem = Validations.doesIdExists(commentRequest.getParentItemId(), itemRepository);

        try {
            user = Validations.doesIdExists(commentRequest.getUserId(), userRepository);
        } catch (NoSuchElementException e) {
            throw new AccountNotFoundException(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString());
        }

        Comment comment = Comment.createNewComment(user, commentRequest.getTitle(), commentRequest.getDescription(), parentItem);
        return commentRepository.save(comment);
    }

    public List<Comment> getAllCommentsInBoard(long boardId) {
        return null;
    }

    public List<Comment> getAllCommentsInStatus(long statusId) {
        return null;
    }
}
