package CRM.controller.facades;

import CRM.entity.Comment;
import CRM.entity.Item;
import CRM.entity.response.Response;
import CRM.service.CommentService;
import CRM.service.ItemService;
import CRM.service.ServiceInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SharedContentFacade {

    private static Logger logger = LogManager.getLogger(AuthFacade.class.getName());

    @Autowired
    private ItemService itemService;
    @Autowired
    private CommentService commentService;

    public Response create(Item item){
        // make sure the params are correct using Validations.validateCreatedItem()
        // catch exception if relevant

        // call itemService with create function to create a new item
        // return the response with the new item as a data inside response entity.
        return null;
    }

    public Response create(Comment comment){
        // make sure the params are correct using Validations.validateCreatedComment()
        // catch exception if relevant

        // call commentService with create function to create a new item
        // return the response with the new item as a data inside response entity.
        return null;
    }

    public Response delete(Long id, Class<C> clz){
        // validate the id using the Validations.validate function
        // call the correct service using convertFromClassToService(clz) function
        // with delete function in it.
        return null;
    }

    public Response update(Long id, String field, String content, Class<C> clz){
        // validate params using the Validations.validate function
        // call the correct service using convertFromClassToService(clz) function
        // with update function in it.
        return null;
    }

    public Response get(Long id, Class<C> clz){
        // validate the id using the Validations.validate function
        // call the correct service using convertFromClassToService(clz) function
        // with find function in it.
        return null;
    }

    public Response getAllInItem(Long itemId, Class<C> clz){
        // validate the id using the Validations.validate function
        // call the correct service using convertFromClassToService(clz) function
        // with getAllInItem function in it.
        return null;
    }

    public Response getAllItemsInBoard(Long boardId){
        // validate the id using the Validations.validate function
        // call itemService with the relevant function.
        return null;
    }

    /**
     * This function gets an item as a parameter and extracts its class in order to
     * return the correct service we need to select the action we need.
     *
     * @param c - class of folder/document
     * @return the service we need to use according to what file it is.
     */
    private ServiceInterface convertFromClassToService(Class c) {
        logger.info("in FacadeFileController -> convertFromClassToService ,item of Class: " + c);

        if (c.equals(Item.class)) return itemService;
        if (c.equals(Comment.class)) return commentService;

        return null;
    }
}