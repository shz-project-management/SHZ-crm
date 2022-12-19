package CRM.service;

import CRM.entity.Comment;
import CRM.entity.SharedContent;
import CRM.entity.requests.CommentRequest;
import CRM.entity.requests.ItemRequest;
import CRM.repository.CommentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CommentService implements ServiceInterface {

    private static Logger logger = LogManager.getLogger(CommentService.class.getName());

    @Autowired
    private CommentRepository commentRepository;

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

    public Comment create(CommentRequest comment){
        // I don't think there are any validations needed to be here, since we
        // don't have any unique or primary values in an item.

        // save the item in the database -> commentRepository
        return null;
    }

    public List<Comment> getAllCommentsInBoard(long boardId) {
        return null;
    }

    public List<Comment> getAllCommentsInStatus(long statusId) {
        return null;
    }
}
