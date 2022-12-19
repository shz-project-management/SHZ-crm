package CRM.controller.facades;

import CRM.entity.Comment;
import CRM.entity.DTO.CommentDTO;
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

    /**
     * create
     *
     * @param item - a request object containing information about the item to be created
     * @return a response object containing the new item as data and a message and status about the creation process
     * This function receives an item request object and validates its parameters using the validateCreatedItem function from the Validations class.
     * If the validation is successful, the function calls the create function from the itemService class to create a new item.
     * The new item is then wrapped in a response object, along with a message indicating success and a status of HttpStatus.ACCEPTED and a status code of 201.
     * If an exception is thrown during the validation or item creation process, a response object is returned with a message and status indicating the error that occurred.
     * The specific exceptions that are caught and handled include IllegalArgumentException, AccountNotFoundException, NoSuchElementException, and NullPointerException.
     * If an IllegalArgumentException or AccountNotFoundException is thrown, the response has a status of HttpStatus.BAD_REQUEST and a status code of 400.
     * If a NoSuchElementException or NullPointerException is thrown, the response has a status of HttpStatus.BAD_REQUEST and a status code of 500.
     */
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

    public Response create(CommentRequest comment) {
        try {
            // make sure the params are correct using Validations.validateCreatedComment()
            // catch exception if relevant
            Validations.validateCreatedComment(comment);

            // call commentService with create function to create a new comment
            // return the response with the new comment as a data inside response entity.
            return new Response.Builder()
                    .data(CommentDTO.getSharedContentFromDB(commentService.create(comment)))
                    .message(SuccessMessage.CREATE.toString())
                    .status(HttpStatus.ACCEPTED)
                    .statusCode(201)
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

    /**
     * delete
     *
     * @param ids - a list of IDs for the items to be deleted
     * @param clz - the class of the items to be deleted
     * @return a response object containing a message and status about the deletion process
     * This function receives a list of IDs and a class of items to be deleted.
     * The IDs are validated using the validate function from the Validations class, which checks that they match the regex provided.
     * The correct service to handle the deletion is then determined using the convertFromClassToService function, which takes the class of the items as an argument and returns the appropriate service.
     * The delete function of this service is then called with the list of IDs, and the result is wrapped in a response object along with a message indicating success and a status of HttpStatus.NO_CONTENT and a status code of 204.
     * If an exception is thrown during the validation or deletion process, a response object is returned with a message and status indicating the error that occurred.
     * The specific exceptions that are caught and handled include IllegalArgumentException, NoSuchElementException, and NullPointerException.
     * If an IllegalArgumentException or NoSuchElementException is thrown, the response has a status of HttpStatus.BAD_REQUEST and a status code of 400.
     * If a NullPointerException is thrown, the response has a status of HttpStatus.BAD_REQUEST and a status code of 500.
     */
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

    /**
     * get
     *
     * @param id  - the ID of the item to retrieve
     * @param clz - the class of the item to retrieve
     * @return a response object containing the retrieved item as data and a message and status about the retrieval process
     * This function receives an ID and a class of an item to retrieve.
     * The ID is validated using the validate function from the Validations class, which checks that it matches the regex provided.
     * The correct service to handle the retrieval is then determined using the convertFromClassToService function, which takes the class of the item as an argument and returns the appropriate service.
     * The get function of this service is then called with the ID, and the result is passed through the convertFromServiceOutputToDTOEntity function to convert it to a DTO entity.
     * The resulting DTO entity is then wrapped in a response object along with a message indicating success and a status of HttpStatus.OK and a status code of 200.
     * If an exception is thrown during the validation or retrieval process, a response object is returned with a message and status indicating the error that occurred.
     * The specific exceptions that are caught and handled include IllegalArgumentException, NoSuchElementException, and NullPointerException.
     * If an IllegalArgumentException or NoSuchElementException is thrown, the response has a status of HttpStatus.BAD_REQUEST and a status code of 400.
     * If a NullPointerException is thrown, the response has a status of HttpStatus.BAD_REQUEST and a status code of 500.
     */
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

    /**
     * getAllItemsInBoard
     *
     * @param id - the ID of the board whose items are to be retrieved
     * @return a response object containing a list of the retrieved items as data and a message and status about the retrieval process
     * This function receives the ID of a board and retrieves all the items in that board.
     * The ID is validated using the validate function from the Validations class, which checks that it matches the regex provided.
     * The itemService is then called with the getAllInBoard function, passing in the ID of the board.
     * The returned list of items is converted to a list of DTO entities using the getSharedContentFromDB function, and this list is wrapped in a response object along with a message indicating success and a status of HttpStatus.OK and a status code of 200.
     * If an exception is thrown during the validation or retrieval process, a response object is returned with a message and status indicating the error that occurred.
     * The specific exceptions that are caught and handled include IllegalArgumentException, NoSuchElementException, and NullPointerException.
     * If an IllegalArgumentException or NoSuchElementException is thrown, the response has a status of HttpStatus.BAD_REQUEST and a status code of 400.
     * If a NullPointerException is thrown, the response has a status of HttpStatus.BAD_REQUEST and a status code of 500.
     */
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
        try {
            Validations.validate(boardId, Regex.ID.getRegex());
            return new Response.Builder()
                    .data(commentService.getAllCommentsInBoard(boardId).stream().map(comment -> CommentDTO.getSharedContentFromDB(comment)).collect(Collectors.toList()))
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

    public Response getAllItemsInItem(Long boardId) {
        // validate the id using the Validations.validate function
        // call itemService with the relevant function.
        return null;
    }




    public Response getAllCommentsInStatus(Long statusId) {
        return null;
    }

    public Response getAllCommentsInItem(Long itemId) {
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

    /**
     * convertFromServiceOutputToDTOEntity
     *
     * @param content - the shared content object to convert
     * @param clz     - the class of the shared content object
     * @return a DTO entity representing the shared content object
     * This function receives a shared content object and a class representing the type of shared content.
     * It checks the class of the shared content and converts it to the corresponding DTO entity using the appropriate conversion function.
     * Currently, the only supported class is Item, and for this class, the getSharedContentFromDB function from the ItemDTO class is used for the conversion.
     * If the class is not recognized, an IllegalArgumentException is thrown.
     */
    private SharedContentDTO convertFromServiceOutputToDTOEntity(SharedContent content, Class clz) {
        if (clz.getSimpleName().equals(Item.class.getSimpleName()))
            return ItemDTO.getSharedContentFromDB((Item) content);
//        if (clz.getSimpleName().equals(Comment.class.getSimpleName()))
//               contentDTO = CommentDTO.getSharedContentFromDB((Comment)content);

        throw new IllegalArgumentException("There is no such class in the system!");
    }

}