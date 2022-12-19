package CRM.controller.facades;

import CRM.entity.Comment;
import CRM.entity.DTO.ItemDTO;
import CRM.entity.DTO.UserDTO;
import CRM.entity.Item;
import CRM.entity.requests.ItemRequest;
import CRM.entity.response.Response;
import CRM.service.CommentService;
import CRM.service.ItemService;
import CRM.service.ServiceInterface;
import CRM.utils.Validations;
import CRM.utils.enums.SuccessMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class SharedContentFacade {

    private static Logger logger = LogManager.getLogger(AuthFacade.class.getName());

    @Autowired
    private ItemService itemService;
    @Autowired
    private CommentService commentService;

    public Response create(ItemRequest item){
        try {
            // make sure the params are correct using Validations.validateCreatedItem()
            // catch exception if relevant
            Validations.validateCreatedItem(item);

            // call itemService with create function to create a new item
            // return the response with the new item as a data inside response entity.
            return new Response.Builder()
                    .data(ItemDTO.fromItem(itemService.create(item)))
                    .message(SuccessMessage.CREATE.toString())
                    .status(HttpStatus.ACCEPTED)
                    .statusCode(201)
                    .build();

        } catch (IllegalArgumentException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(400).build();
        } catch (NullPointerException e) {
            return new Response.Builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(500).build();
        }
    }

//    public Response create(CommentRequest comment){
//        // make sure the params are correct using Validations.validateCreatedComment()
//        // catch exception if relevant
//
//        // call commentService with create function to create a new item
//        // return the response with the new item as a data inside response entity.
//        return null;
//    }

    public Response delete(Long id, Class clz){
        // validate the id using the Validations.validate function
        // call the correct service using convertFromClassToService(clz) function
        // with delete function in it.
        return null;
    }

    public Response update(ItemRequest updateItem){
        // validate params using the Validations.validate function
        // call the correct service using convertFromClassToService(clz) function
        // with update function in it.
        return null;
    }

//    public Response update(UpdateCommentRequest updateComment){
//        // validate params using the Validations.validate function
//        // call the correct service using convertFromClassToService(clz) function
//        // with update function in it.
//        return null;
//    }

    public Response get(Long id, Class clz){
        // validate the id using the Validations.validate function
        // call the correct service using convertFromClassToService(clz) function
        // with find function in it.
        return null;
    }

    public Response getAllItemsInBoard(Long id, Class clz){
        // validate the id using the Validations.validate function
        // call the correct service using convertFromClassToService(clz) function
        convertFromClassToService(clz).get(id);
        // with getAllInItem function in it.
        return null;
    }

    public Response getAllItemsInItem(Long boardId){
        // validate the id using the Validations.validate function
        // call itemService with the relevant function.
        return null;
    }

    /**
     * Converts a given Class object to the corresponding AttributeService object.
     *
     * @param c the Class object to be converted
     * @return the corresponding AttributeService object, or null if no corresponding AttributeService object is found
     */
    private ServiceInterface convertFromClassToService(Class c) {
        logger.info("in FacadeFileController -> convertFromClassToService ,item of Class: " + c);

        if (c.equals(Item.class)) return itemService;
        if (c.equals(Comment.class)) return commentService;

        return null;
    }
}