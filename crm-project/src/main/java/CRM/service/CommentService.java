package CRM.service;

import CRM.controller.facades.AuthFacade;
import CRM.entity.Comment;
import CRM.entity.SharedContent;
import CRM.repository.CommentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CommentService implements ServiceInterface {

    private static Logger logger = LogManager.getLogger(AuthFacade.class.getName());

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public int delete(long id) {
        // checkIfExists
        // delete it.

        return 0;
    }

    @Override
    public SharedContent update(long id, String field, String content) {
        // checkIfExists
        // make sure there is such a field in Item -> use reflection!

        return null;
    }

    @Override
    public SharedContent get(long id) {
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

    public Comment create(Comment comment){
        // I don't think there are any validations needed to be here, since we
        // don't have any unique or primary values in an item.

        // save the item in the database -> commentRepository
        return null;
    }

    private Comment checkIfExists(long id){
        Optional<Comment> comment = commentRepository.findById(id);
        if(!comment.isPresent()) throw new NoSuchElementException("Couldn't find this comment in the database!");

        return comment.get();
    }
}
