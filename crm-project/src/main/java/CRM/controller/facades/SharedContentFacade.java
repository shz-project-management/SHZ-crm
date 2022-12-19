package CRM.controller.facades;

import CRM.entity.Comment;
import CRM.entity.DTO.ItemDTO;
import CRM.entity.DTO.SharedContentDTO;
import CRM.entity.DTO.UserDTO;
import CRM.entity.Item;
import CRM.entity.SharedContent;
import CRM.entity.requests.CommentRequest;
import CRM.entity.requests.ItemRequest;
import CRM.entity.response.Response;
import CRM.service.CommentService;
import CRM.service.ItemService;
import CRM.service.ServiceInterface;
import CRM.utils.Validations;
import CRM.utils.enums.Regex;
import CRM.utils.enums.SuccessMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
public class SharedContentFacade {

    private static Logger logger = LogManager.getLogger(AuthFacade.class.getName());

    @Autowired
    private ItemService itemService;
    @Autowired
    private CommentService commentService;

    public Response create(ItemRequest item) {
        try {
            // make sure the params are correct using Validations.validateCreatedItem()
            // catch exception if relevant
            Validations.validateCreatedItem(item);

            // call itemService with create function to create a new item
            // return the response with the new item as a data inside response entity.
            return new Response.Builder()
                    .data(ItemDTO.getSharedContentFromDB(itemService.create(item)))
                    .message(SuccessMessage.CREATE.toString())
                    .status(HttpStatus.ACCEPTED)
                    .statusCode(201)
                    .build();

        } catch (IllegalArgumentException | AccountNotFoundException | NoSuchElementException e) {
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

    public Response create(CommentRequest comment){
        // make sure the params are correct using Validations.validateCreatedComment()
        // catch exception if relevant

        // call commentService with create function to create a new item
        // return the response with the new item as a data inside response entity.
        return null;
    }

    public Response delete(List<Long> ids, Class clz) {
        try {
            // validate the id using the Validations.validate function
            ids.forEach(id -> Validations.validate(id, Regex.ID.getRegex()));

            // call the correct service using convertFromClassToService(clz) function with delete function in it
            return new Response.Builder()
                    .data(convertFromClassToService(clz).delete(ids))
                    .message(SuccessMessage.DELETED.toString())
                    .status(HttpStatus.NO_CONTENT)
                    .statusCode(204)
                    .build();
        } catch (IllegalArgumentException | NoSuchElementException e) {
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

    public Response update(ItemRequest updateItem) {
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

    public Response get(Long id, Class clz) {
        try {
            // validate the id using the Validations.validate function
            Validations.validate(id, Regex.ID.getRegex());

            // call the correct service using convertFromClassToService(clz) function with find function in it
            return new Response.Builder()
                    .data(convertFromServiceOutputToDTOEntity(convertFromClassToService(clz).get(id), clz))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .build();

        } catch (IllegalArgumentException | NoSuchElementException e) {
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

    public Response getAllItemsInBoard(Long id) {
        try {
            // validate the id using the Validations.validate function
            Validations.validate(id, Regex.ID.getRegex());

            // call the correct service using convertFromClassToService(clz) function
            // with getAllInItem function in it.
            return new Response.Builder()
                    .data(itemService.getAllInBoard(id).stream().map(item -> ItemDTO.getSharedContentFromDB(item)).collect(Collectors.toList()))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .build();

        } catch (IllegalArgumentException | NoSuchElementException e) {
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

    public Response getAllCommentsInBoard(Long boardId) {
        return null;
    }

    public Response getAllItemsInItem(Long boardId) {
        // validate the id using the Validations.validate function
        // call itemService with the relevant function.
        return null;
    }




    public Response getAllCommentsInStatus(Long statusId) {
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

        throw new IllegalArgumentException("There is no such class in the system!");
    }

    private SharedContentDTO convertFromServiceOutputToDTOEntity(SharedContent content, Class clz) {
        if (clz.getSimpleName().equals(Item.class.getSimpleName()))
            return ItemDTO.getSharedContentFromDB((Item) content);
//        if (clz.getSimpleName().equals(Comment.class.getSimpleName()))
//               contentDTO = CommentDTO.getSharedContentFromDB((Comment)content);

        throw new IllegalArgumentException("There is no such class in the system!");
    }

}