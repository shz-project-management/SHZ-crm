package CRM.controller.facades;

import CRM.entity.Comment;
import CRM.entity.DTO.CommentDTO;
import CRM.entity.DTO.ItemDTO;
import CRM.entity.DTO.SectionDTO;
import CRM.entity.DTO.SharedContentDTO;
import CRM.entity.Item;
import CRM.entity.SharedContent;
import CRM.entity.requests.CommentRequest;
import CRM.entity.requests.ItemRequest;
import CRM.entity.requests.ObjectsIdsRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.entity.response.Response;
import CRM.service.CommentService;
import CRM.service.ItemService;
import CRM.service.ServiceInterface;
import CRM.utils.Validations;
import CRM.utils.enums.ExceptionMessage;
import CRM.utils.enums.Regex;
import CRM.utils.enums.SuccessMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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
     * Creates a new item in the system and stores it in the database.
     *
     * @param item The request object containing the details of the item to be created.
     * @return A response object with the newly created item as data, or an error message if the creation failed.
     * @throws IllegalArgumentException if any field in the request object fails validation.
     * @throws AccountNotFoundException if the user ID specified in the request object does not correspond to an existing user.
     * @throws NoSuchElementException   if the board ID, type ID, or status ID specified in the request object do not correspond to existing entities.
     * @throws NullPointerException     if the parent item ID is null.
     */
    public Response create(ItemRequest item) {
        try {
            // make sure the params are correct using Validations.validateCreatedItem()
            // catch exception if relevant
            Validations.validateCreatedItem(item);

            // call itemService with create function to create a new item
            // return the response with the new item as a data inside response entity.
            return new Response.Builder()
                    .data(SectionDTO.createSectionDTO(itemService.create(item)))
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

    /**
     * Creates a new comment in the system and stores it in the database.
     *
     * @param comment The request object containing the details of the comment to be created.
     * @return A response object with the newly created comment as data, or an error message if the creation failed.
     * @throws IllegalArgumentException if any field in the request object fails validation.
     * @throws AccountNotFoundException if the user ID specified in the request object does not correspond to an existing user.
     * @throws NoSuchElementException   if the parent item ID specified in the request object does not correspond to an existing item.
     * @throws NullPointerException     if the title is null.
     */
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

    /**
     * Deletes a list of items or comments from the system and their associated comments from the database.
     *
     * @param ids The IDs of the items or comments to be deleted.
     * @param clz The class of the objects to be deleted (either Item or Comment).
     * @return A response object with the number of items or comments deleted as data, or an error message if the deletion failed.
     * @throws IllegalArgumentException if any of the IDs fails validation.
     * @throws NoSuchElementException   if any of the IDs do not correspond to existing items or comments.
     * @throws NullPointerException     if the list of IDs is null.
     */
    public Response delete(List<Long> ids, long boardId, Class clz) {
        List<Long> correctIds = new ArrayList<>();
        // validate the id using the Validations.validate function
        Validations.validate(boardId, Regex.ID.getRegex());
        ids.forEach(id -> {
            try {
                Validations.validate(id, Regex.ID.getRegex());
                correctIds.add(id);
            } catch (IllegalArgumentException | NullPointerException e) {
            }
        });
        // call the correct service using convertFromClassToService(clz) function with delete function in it
        return new Response.Builder()
                .data(convertFromClassToService(clz).delete(correctIds, boardId))
                .message(SuccessMessage.DELETED.toString())
                .status(HttpStatus.NO_CONTENT)
                .statusCode(204)
                .build();
    }


    //TODO + Documentation
    public Response update(UpdateObjectRequest updateObject, Long updateObjectId, Class clz) {
        // validate params using the Validations.validate function
        // call the correct service using convertFromClassToService(clz) function
        // with update function in it.
        try {
            // validate the id using the Validations.validate function
            Validations.validateSharedContent(updateObject.getObjectsIdsRequest().getBoardId(),
                    updateObject.getObjectsIdsRequest().getSectionId(),
                    updateObjectId, null);


            // call the correct service using convertFromClassToService(clz) function with find function in it
            return new Response.Builder()
                    .data(convertFromServiceOutputToDTOEntity(convertFromClassToService(clz).update(updateObject ,updateObjectId), clz))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .build();

        } catch (IllegalArgumentException | NoSuchFieldException | NoSuchElementException e) {
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
     * Returns an object with the given id and class type.
     *
     * @param searchId the id of the object
     * @param clz      the class type of the object
     * @return a {@link Response} object with the object and a status message. If the object with the given
     * id and class type does not exist, a BAD_REQUEST status with a message indicating the error is
     * returned. If the id is not valid, a BAD_REQUEST status with a message indicating the error is
     * returned. If a NullPointerException is thrown, a BAD_REQUEST status with a message indicating the
     * error is returned.
     */
    public Response get(ObjectsIdsRequest objectsIdsRequest, Long searchId ,Class clz) {
        try {
            // validate the id using the Validations.validate function
            Validations.validateSharedContent(objectsIdsRequest.getBoardId(),
                    objectsIdsRequest.getSectionId(),
                    searchId,
                    objectsIdsRequest.getParentId());

            // call the correct service using convertFromClassToService(clz) function with find function in it
            return new Response.Builder()
                    .data(convertFromServiceOutputToDTOEntity(convertFromClassToService(clz).get(objectsIdsRequest, searchId), clz))
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

    //TODO + DTO + Documentation
    public Response getAllItemsInSection(ObjectsIdsRequest objectsIdsRequest) {
        try {
            // validate the id using the Validations.validate function
            Validations.validate(objectsIdsRequest.getSectionId(), Regex.ID.getRegex());
            Validations.validate(objectsIdsRequest.getBoardId(), Regex.ID.getRegex());

            // call the correct service using convertFromClassToService(clz) function
            // with getAllInItem function in it.
            //FIXME- itemDTO list
            return new Response.Builder()
                    .data(itemService.getAllInSection(objectsIdsRequest).stream().map(ItemDTO::getSharedContentFromDB).collect(Collectors.toList()))
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
     * Returns a list of all comments in a board with the given id.
     *
     * @param boardId the id of the board
     * @return a {@link Response} object with a list of {@link CommentDTO} objects and a status message.
     * If the board with the given id does not exist, a BAD_REQUEST status with a message indicating
     * the error is returned. If the id is not valid, a BAD_REQUEST status with a message indicating
     * the error is returned. If a NullPointerException is thrown, a BAD_REQUEST status with a message
     * indicating the error is returned.
     */
    public Response getAllCommentsInBoard(Long boardId) {
        try {
            Validations.validate(boardId, Regex.ID.getRegex());
            //FIXME- commentDTO list
            return new Response.Builder()
                    .data(commentService.getAllCommentsInBoard(boardId).stream().map(CommentDTO::getSharedContentFromDB).collect(Collectors.toList()))
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

    public Response getAllInItem(ObjectsIdsRequest objectsIdsRequest, Class clz) {
        try {
            Validations.validate(objectsIdsRequest.getItemId(), Regex.ID.getRegex());
            Validations.validate(objectsIdsRequest.getSectionId(), Regex.ID.getRegex());
            Validations.validate(objectsIdsRequest.getBoardId(), Regex.ID.getRegex());
            return new Response.Builder()
                    .data(convertFromClassToService(clz).getAllInItem(objectsIdsRequest).stream().map(entity -> convertFromServiceOutputToDTOEntity(entity, clz)).collect(Collectors.toList()))
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

    //TODO + DTO list + documentation
    public Response getAllCommentsInSection(ObjectsIdsRequest objectsIdsRequest) {
        try {
            Validations.validateIDs(objectsIdsRequest.getBoardId(), objectsIdsRequest.getSectionId());
            return new Response.Builder()
                    .data(commentService.getAllCommentsInSection(objectsIdsRequest).stream().map(CommentDTO::getSharedContentFromDB).collect(Collectors.toList()))
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
     * Converts a given Class object to the corresponding AttributeService object.
     *
     * @param c the Class object to be converted
     * @return the corresponding AttributeService object, or null if no corresponding AttributeService object is found
     */
    ServiceInterface convertFromClassToService(Class c) {
        logger.info("in FacadeFileController -> convertFromClassToService ,item of Class: " + c);

        if (c.equals(Item.class)) return itemService;
        if (c.equals(Comment.class)) return commentService;

        throw new IllegalArgumentException("There is no such class in the system!");
    }

    /**
     * This function receives a shared content object and a class representing the type of shared content.
     * It checks the class of the shared content and converts it to the corresponding DTO entity using the appropriate conversion function.
     * Currently, the only supported class is Item, and for this class, the getSharedContentFromDB function from the ItemDTO class is used for the conversion.
     * If the class is not recognized, an IllegalArgumentException is thrown.
     *
     * @param content - the shared content object to convert
     * @param clz     - the class of the shared content object
     * @return a DTO entity representing the shared content object
     */
    private SharedContentDTO convertFromServiceOutputToDTOEntity(SharedContent content, Class clz) {
        if (clz.getSimpleName().equals(Item.class.getSimpleName()))
            return ItemDTO.getSharedContentFromDB((Item) content);
        if (clz.getSimpleName().equals(Comment.class.getSimpleName()))
            return CommentDTO.getSharedContentFromDB((Comment) content);

        throw new IllegalArgumentException(ExceptionMessage.NO_SUCH_CLASS.toString());
    }
}