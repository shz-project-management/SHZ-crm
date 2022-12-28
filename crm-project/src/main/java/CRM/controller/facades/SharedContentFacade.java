package CRM.controller.facades;

import CRM.entity.*;
import CRM.entity.DTO.*;
import CRM.entity.requests.*;
import CRM.entity.response.Response;
import CRM.service.*;
import CRM.utils.NotificationSender;
import CRM.utils.Validations;
import CRM.utils.enums.*;
import com.google.api.client.http.HttpStatusCodes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SharedContentFacade {

    private static Logger logger = LogManager.getLogger(AuthFacade.class.getName());

    @Autowired
    private ItemService itemService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private UserService userService;
    @Autowired
    private SettingsService settingsService;
    @Autowired
    private NotificationSender notificationSender;

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
            return Response.builder()
                    .data(SectionDTO.createSectionDTO(itemService.create(item)))
                    .message(SuccessMessage.CREATE.toString())
                    .status(HttpStatus.ACCEPTED)
                    .statusCode(HttpStatusCodes.STATUS_CODE_CREATED)
                    .build();

        } catch (IllegalArgumentException | AccountNotFoundException | NoSuchElementException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                    .build();
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
    public Response create(CommentRequest comment, Long userId, Long boardId) {
        try {
            // make sure the params are correct using Validations.validateCreatedComment()
            // catch exception if relevant
            Validations.validateCreatedComment(comment, userId, boardId);

            // call commentService with create function to create a new comment
            // return the response with the new comment as a data inside response entity.
            List<CommentDTO> commentDTOS = CommentDTO.getCommentDTOList(commentService.create(comment, userId, boardId));
            sendCommentCreatedNotification(comment, userId, boardId);
            return Response.builder()
                    .data(commentDTOS)
                    .message(SuccessMessage.CREATE.toString())
                    .status(HttpStatus.ACCEPTED)
                    .statusCode(HttpStatusCodes.STATUS_CODE_CREATED)
                    .build();

        } catch (IllegalArgumentException | AccountNotFoundException | NoSuchElementException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * Method to delete objects from a board.
     *
     * @param ids     a list of ids to delete
     * @param boardId the id of the board
     * @param clz     the class of the objects to delete
     * @return a response object with a data field equal to the number of deleted objects, a message field
     * indicating success or failure, and a status field indicating the status of the request
     * @throws IllegalArgumentException if the ids or boardId are invalid
     * @throws NullPointerException     if the clz parameter is null
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
        int deleteData = convertFromClassToService(clz).delete(correctIds, boardId);
        sendItemDeleteNotification(correctIds, boardId);
        return Response.builder()
                .data(deleteData)
                .message(SuccessMessage.DELETED.toString())
                .status(HttpStatus.NO_CONTENT)
                .statusCode(HttpStatusCodes.STATUS_CODE_NO_CONTENT)
                .build();
    }


    /**
     * Method to update objects in a board.
     *
     * @param updateObject an update request object containing the ids and fields to update
     * @param clz          the class of the objects to update
     * @return a response object with a data field equal to the updated object, a message field indicating
     * success or failure, and a status field indicating the status of the request
     * @throws IllegalArgumentException if the updateObject or its contained ids are invalid
     * @throws NoSuchFieldException     if the field to update does not exist for the given class
     * @throws NoSuchElementException   if the object to update does not exist
     * @throws AccountNotFoundException if the user associated with the update request cannot be found
     * @throws NullPointerException     if the clz parameter is null
     */
    public Response update(UpdateObjectRequest updateObject, Class clz) {
        // validate params using the Validations.validate function
        // call the correct service using convertFromClassToService(clz) function
        // with update function in it.
        try {
            //updateObject.getObjectsIdsRequest().getUpdateObjId();
            // validate the id using the Validations.validate function
            Validations.validateSharedContent(updateObject.getObjectsIdsRequest());
            // call the correct service using convertFromClassToService(clz) function with find function in it
            SectionDTO sectionDTO = SectionDTO.createSectionDTO(convertFromClassToService(clz).update(updateObject));
            if (clz.equals(Item.class)) {
                sendRelevantNotification(updateObject);
            }
            return Response.builder()
                    .data(sectionDTO)
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                    .build();

        } catch (IllegalArgumentException | NoSuchFieldException | NoSuchElementException |
                AccountNotFoundException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                    .build();
        }
    }


    /**
     * Method to get an object from a board.
     *
     * @param objectsIdsRequest an object containing the id and board id of the object to retrieve
     * @param clz               the class of the object to retrieve
     * @return a response object with a data field equal to the retrieved object, a message field indicating
     * success or failure, and a status field indicating the status of the request
     * @throws IllegalArgumentException if the objectsIdsRequest or its contained ids are invalid
     * @throws NoSuchElementException   if the object to retrieve does not exist
     * @throws AccountNotFoundException if the user associated with the request cannot be found
     * @throws NullPointerException     if the clz parameter is null
     */
    public Response get(ObjectsIdsRequest objectsIdsRequest, Class clz) {
        try {
            // validate the id using the Validations.validate function
            Validations.validateSharedContent(objectsIdsRequest);

            // call the correct service using convertFromClassToService(clz) function with find function in it
            return Response.builder()
                    .data(convertFromServiceOutputToDTOEntity(convertFromClassToService(clz).get(objectsIdsRequest), clz))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                    .build();

        } catch (IllegalArgumentException | NoSuchElementException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * Method to retrieve all items in a section.
     *
     * @param objectsIdsRequest an object containing the id of the section and board
     * @return a response object with a data field equal to a list of item DTOs, a message field indicating
     * @throws IllegalArgumentException if the objectsIdsRequest or its contained ids are invalid
     * @throws NoSuchElementException   if the section or board associated with the request cannot be found
     * @throws NullPointerException     if the objectsIdsRequest parameter is null
     */
    public Response getAllItemsInSection(ObjectsIdsRequest objectsIdsRequest) {
        try {
            // validate the id using the Validations.validate function
            Validations.validateIDs(objectsIdsRequest.getSectionId(), objectsIdsRequest.getBoardId());

            // call the correct service using convertFromClassToService(clz) function
            // with getAllInItem function in it.
            return Response.builder()
                    .data(ItemDTO.getItemsDTOList(itemService.getAllInSection(objectsIdsRequest)))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                    .build();

        } catch (IllegalArgumentException | NoSuchElementException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                    .build();
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

            return Response.builder()
                    .data(CommentDTO.getCommentDTOList(commentService.getAllCommentsInBoard(boardId)))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                    .build();

        } catch (IllegalArgumentException | NoSuchElementException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * Returns a response with a list of items of the specified class in the item with the given IDs.
     *
     * @param objectsIdsRequest an object containing the IDs of the item, section, and board
     * @param clz               the class of the items to retrieve
     * @return a response with the list of items, a status code and message
     * @throws IllegalArgumentException if any of the IDs is invalid
     * @throws NoSuchElementException   if any of the IDs does not correspond to an existing element
     * @throws NullPointerException     if an error occurs while processing the request
     */
    public Response getAllInItem(ObjectsIdsRequest objectsIdsRequest, Class clz) {
        try {
            Validations.validateIDs(objectsIdsRequest.getItemId(), objectsIdsRequest.getSectionId(), objectsIdsRequest.getBoardId());

            return Response.builder()
                    .data(convertFromClassToService(clz).getAllInItem(objectsIdsRequest).stream().map(entity -> convertFromServiceOutputToDTOEntity(entity, clz)).collect(Collectors.toList()))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                    .build();

        } catch (IllegalArgumentException | NoSuchElementException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                    .build();
        }
    }

//    //TODO + DTO list + documentation
//    public Response getAllCommentsInSection(ObjectsIdsRequest objectsIdsRequest) {
//        try {
//            Validations.validateIDs(objectsIdsRequest.getBoardId(), objectsIdsRequest.getSectionId());
//
//            return Response.builder()
//                    .data(CommentDTO.getCommentDTOList(commentService.getAllCommentsInSection(objectsIdsRequest)))
//                    .message(SuccessMessage.FOUND.toString())
//                    .status(HttpStatus.OK)
//                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
//                    .build();
//
//        } catch (IllegalArgumentException | NoSuchElementException e) {
//            return Response.builder()
//                    .message(e.getMessage())
//                    .status(HttpStatus.BAD_REQUEST)
//                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
//                    .build();
//        } catch (NullPointerException e) {
//            return Response.builder()
//                    .message(e.getMessage())
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
//                    .build();
//        }
//    }

    /**
     * Assigns a board, section, or item to a user.
     *
     * @param objIds an {@link ObjectsIdsRequest} object containing the IDs of the board, section, item, and user
     * @param clz    the class of the object being assigned
     * @return a {@link Response} object with the assigned object and a message indicating success
     * @throws IllegalArgumentException if any of the provided IDs are invalid or do not correspond to an existing object
     * @throws NoSuchElementException   if any of the provided IDs do not correspond to an existing object
     * @throws NullPointerException     if any of the provided IDs are null
     */
    public Response assignToUser(ObjectsIdsRequest objIds, Class clz) {
        try {
            Validations.validateIDs(objIds.getBoardId(), objIds.getSectionId(), objIds.getUpdateObjId());
            Validations.validate(objIds.getEmail(), Regex.EMAIL.getRegex());

            return Response.builder()
                    .data(SectionDTO.createSectionWithUserDTO((convertFromClassToService(clz).assignToUser(objIds)), userService.get(objIds.getEmail())))
                    .message(SuccessMessage.FOUND.toString())
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                    .build();

        } catch (IllegalArgumentException | AccountNotFoundException | NoSuchElementException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                    .build();
        } catch (NullPointerException e) {
            return Response.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                    .build();
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

    private void sendRelevantNotification(UpdateObjectRequest updateObject) throws AccountNotFoundException {
        NotificationRequest request;
        if (updateObject.getFieldName().equals(UpdateField.STATUS))
            request = NotificationRequest.createStatusChangeRequest(userService.get(updateObject.getObjectsIdsRequest().getUserId()),
                    boardService.get(updateObject.getObjectsIdsRequest().getBoardId()),
                    updateObject.getObjectsIdsRequest().getUpdateObjId(), updateObject.getContent(),
                    settingsService.getNotificationSettingFromDB(Notifications.STATUS_CHANGED.name));
        else if (updateObject.getFieldName().equals(UpdateField.TYPE)) {
            request = NotificationRequest.createTypeChangeRequest(userService.get(updateObject.getObjectsIdsRequest().getUserId()),
                    boardService.get(updateObject.getObjectsIdsRequest().getBoardId()),
                    updateObject.getObjectsIdsRequest().getUpdateObjId(), updateObject.getContent(),
                    settingsService.getNotificationSettingFromDB(Notifications.TYPE_CHANGED.name));
        }
        else request = NotificationRequest.createItemChangeRequest(userService.get(updateObject.getObjectsIdsRequest().getUserId()),
                    boardService.get(updateObject.getObjectsIdsRequest().getBoardId()),
                    updateObject.getObjectsIdsRequest().getUpdateObjId(),
                    updateObject.getFieldName().toString(), updateObject.getContent(),
                    settingsService.getNotificationSettingFromDB(Notifications.ITEM_DATA_CHANGED.name));

        notificationSender.sendNotificationToManyUsers(request,
                boardService.get(updateObject.getObjectsIdsRequest().getBoardId()).getBoardUsersSet());
    }

    private void sendCommentCreatedNotification(CommentRequest comment, long userId, long boardId) throws AccountNotFoundException {
        notificationSender.sendNotificationToManyUsers(
                NotificationRequest.createCommentAddedRequest(userService.get(userId),
                        boardService.get(boardId), comment.getParentItemId(),
                        comment.getDescription(), userService.get(userId),
                        settingsService.getNotificationSettingFromDB(Notifications.COMMENT_ADDED.name)),
                boardService.get(boardId).getBoardUsersSet());
    }

    private void sendItemDeleteNotification(List<Long> correctIds, Long boardId){
        Board board = boardService.get(boardId);
        for (Long id : correctIds) {
            notificationSender.sendNotificationToManyUsers(NotificationRequest.createDeletedItemRequest(board,
                            id, settingsService.getNotificationSettingFromDB(Notifications.ITEM_DELETED.name)),
                    boardService.get(boardId).getBoardUsersSet());
        }
    }
}