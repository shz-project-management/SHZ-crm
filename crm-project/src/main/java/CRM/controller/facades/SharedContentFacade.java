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
    private static Logger logger = LogManager.getLogger(SharedContentFacade.class.getName());

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
    public Response<SectionDTO> create(ItemRequest item) throws AccountNotFoundException {
        Validations.validateCreatedItem(item);
        return Response.<SectionDTO>builder()
                .data(SectionDTO.createSectionDTO(itemService.create(item)))
                .message(SuccessMessage.CREATE.toString())
                .status(HttpStatus.ACCEPTED)
                .statusCode(HttpStatusCodes.STATUS_CODE_CREATED)
                .build();
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
    public Response<List<CommentDTO>> create(CommentRequest comment, Long userId, Long boardId) throws AccountNotFoundException {
        Validations.validateCreatedComment(comment, userId, boardId);
        List<CommentDTO> commentDTOS = CommentDTO.getCommentDTOList(commentService.create(comment, userId, boardId));
        sendCommentCreatedNotification(comment, userId, boardId);
        return Response.<List<CommentDTO>>builder()
                .data(commentDTOS)
                .message(SuccessMessage.CREATE.toString())
                .status(HttpStatus.ACCEPTED)
                .statusCode(HttpStatusCodes.STATUS_CODE_CREATED)
                .build();
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
    public Response<Void> delete(List<Long> ids, long boardId, Class<? extends SharedContent> clz) throws AccountNotFoundException {
        List<Long> correctIds = new ArrayList<>();
        Validations.validate(boardId, Regex.ID.getRegex());
        ids.forEach(id -> {
            try {
                Validations.validate(id, Regex.ID.getRegex());
                correctIds.add(id);
            } catch (IllegalArgumentException | NullPointerException e) {
            }
        });
        convertFromClassToService(clz).delete(correctIds, boardId);
        sendItemDeleteNotification(correctIds, boardId);
        return Response.<Void>builder()
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
    public <T extends SharedContent> Response<SectionDTO> update(UpdateObjectRequest updateObject, Class<T> clz) throws NoSuchFieldException, AccountNotFoundException {
        Validations.validateSharedContent(updateObject.getObjectsIdsRequest());
        SectionDTO sectionDTO = SectionDTO.createSectionDTO(convertFromClassToService(clz).update(updateObject));
        if (clz.equals(Item.class)) {
            sendRelevantNotification(updateObject);
        }
        return Response.<SectionDTO>builder()
                .data(sectionDTO)
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
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
    public <T extends SharedContentDTO, E extends SharedContent> Response<T> get(ObjectsIdsRequest objectsIdsRequest, Class<E> clz) {
        Validations.validateSharedContent(objectsIdsRequest);
        return Response.<T>builder()
                .data(convertFromServiceOutputToDTOEntity(convertFromClassToService(clz).get(objectsIdsRequest)))
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
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
    public Response<List<ItemDTO>> getAllItemsInSection(ObjectsIdsRequest objectsIdsRequest) {
        Validations.validateIDs(objectsIdsRequest.getSectionId(), objectsIdsRequest.getBoardId());
        return Response.<List<ItemDTO>>builder()
                .data(ItemDTO.getItemsDTOList(itemService.getAllInSection(objectsIdsRequest)))
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
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
    public <T extends SharedContentDTO, E extends SharedContent> Response<List<T>> getAllInItem(ObjectsIdsRequest objectsIdsRequest, Class<E> clz) {
        Validations.validateIDs(objectsIdsRequest.getItemId(), objectsIdsRequest.getSectionId(), objectsIdsRequest.getBoardId());
        List<E> eList = convertFromClassToService(clz).getAllInItem(objectsIdsRequest);
        List<T> tList = convertFromSharedContentToDTOList(eList);
        return Response.<List<T>>builder()
                .data(tList)
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
    }

    /**
     * Converts a list of SharedContent entities to a list of SharedContentDTOs.
     *
     * @param eList a list of SharedContent entities
     * @param <T> the type of SharedContentDTO to return
     * @param <E> the type of SharedContent entity being converted
     * @return a list of SharedContentDTOs
     */
    private <T extends SharedContentDTO, E extends SharedContent> List<T> convertFromSharedContentToDTOList(List<E> eList){
        List<T> tList = new ArrayList<>();
        for (E element: eList) {
            tList.add(convertFromServiceOutputToDTOEntity(element));
        }
        return tList;
    }

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
    public Response<SectionDTO> assignToUser(ObjectsIdsRequest objIds, Class<? extends SharedContent> clz) throws AccountNotFoundException {
        Validations.validateIDs(objIds.getBoardId(), objIds.getSectionId(),
                objIds.getUpdateObjId(), objIds.getAssignedUserId());
        return Response.<SectionDTO>builder()
                .data(SectionDTO.createSectionDTO((convertFromClassToService(clz).assignToUser(objIds))))
                .message(SuccessMessage.FOUND.toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatusCodes.STATUS_CODE_OK)
                .build();
    }

    /**
     * Converts a given Class object to the corresponding AttributeService object.
     *
     * @param clz the Class object to be converted
     * @return the corresponding AttributeService object, or null if no corresponding AttributeService object is found
     */
    private <E extends SharedContent> ServiceInterface convertFromClassToService(Class<E> clz) {
        logger.info("in SharedContentController -> convertFromClassToService ,item of Class: " + clz);

        if (clz.equals(Item.class)) return itemService;
        if (clz.equals(Comment.class)) return commentService;

        throw new IllegalArgumentException("There is no such class in the system!");
    }

    /**
     * This function receives a shared content object and a class representing the type of shared content.
     * It checks the class of the shared content and converts it to the corresponding DTO entity using the appropriate conversion function.
     * Currently, the only supported class is Item, and for this class, the getSharedContentFromDB function from the ItemDTO class is used for the conversion.
     * If the class is not recognized, an IllegalArgumentException is thrown.
     *
     * @param content - the shared content object to convert
     *                //     * @param clz     - the class of the shared content object
     * @return a DTO entity representing the shared content object
     */
    private <T extends SharedContentDTO, E extends SharedContent> T convertFromServiceOutputToDTOEntity(E content) {
        if (content instanceof Item)
            return (T) ItemDTO.getSharedContentFromDB((Item) content);
        if (content instanceof Comment)
            return (T) CommentDTO.getSharedContentFromDB((Comment) content);

        throw new IllegalArgumentException(ExceptionMessage.NO_SUCH_CLASS.toString());
    }

    /**
     * Sends a notification to relevant users based on an update to an object.
     *
     * @param updateObject the update object request
     * @throws AccountNotFoundException if a user specified in the update object request cannot be found
     */
    private void sendRelevantNotification(UpdateObjectRequest updateObject) throws AccountNotFoundException {
        NotificationRequest request;
        if (updateObject.getFieldName().equals(UpdateField.STATUS))
            request = NotificationRequest.createStatusChangeRequest(userService.get(updateObject.getObjectsIdsRequest().getUserId()),
                    boardService.get(updateObject.getObjectsIdsRequest().getBoardId()),
                    updateObject.getObjectsIdsRequest().getUpdateObjId(), updateObject.getContent(),
                    settingsService.getNotificationSettingFromDB(Notifications.STATUS_CHANGED.name), updateObject.getObjectsIdsRequest().getSectionId());
        else if (updateObject.getFieldName().equals(UpdateField.TYPE)) {
            request = NotificationRequest.createTypeChangeRequest(userService.get(updateObject.getObjectsIdsRequest().getUserId()),
                    boardService.get(updateObject.getObjectsIdsRequest().getBoardId()),
                    updateObject.getObjectsIdsRequest().getUpdateObjId(), updateObject.getContent(),
                    settingsService.getNotificationSettingFromDB(Notifications.TYPE_CHANGED.name), updateObject.getObjectsIdsRequest().getSectionId());
        } else
            request = NotificationRequest.createItemChangeRequest(userService.get(updateObject.getObjectsIdsRequest().getUserId()),
                    boardService.get(updateObject.getObjectsIdsRequest().getBoardId()),
                    updateObject.getObjectsIdsRequest().getUpdateObjId(),
                    updateObject.getFieldName().toString(), updateObject.getContent(),
                    settingsService.getNotificationSettingFromDB(Notifications.ITEM_DATA_CHANGED.name), updateObject.getObjectsIdsRequest().getSectionId());

        notificationSender.sendNotificationToManyUsers(request,
                boardService.get(updateObject.getObjectsIdsRequest().getBoardId()).getBoardUsersSet());
    }

    /**
     * Sends a notification when a new comment is created.
     *
     * @param comment the comment request
     * @param userId the ID of the user who created the comment
     * @param boardId the ID of the board on which the comment was created
     * @throws AccountNotFoundException if the user specified by the user ID cannot be found
     */
    private void sendCommentCreatedNotification(CommentRequest comment, long userId, long boardId) throws AccountNotFoundException {
        notificationSender.sendNotificationToManyUsers(
                NotificationRequest.createCommentAddedRequest(userService.get(userId),
                        boardService.get(boardId), comment.getParentItemId(),
                        comment.getDescription(), userService.get(userId),
                        settingsService.getNotificationSettingFromDB(Notifications.COMMENT_ADDED.name), comment.getSectionId()),
                boardService.get(boardId).getBoardUsersSet());
    }

    /**
     * Sends a notification when an item is deleted.
     *
     * @param correctIds the IDs of the deleted items
     * @param boardId the ID of the board on which the items were deleted
     * @throws AccountNotFoundException if the board specified by the board ID cannot be found
     */
    private void sendItemDeleteNotification(List<Long> correctIds, Long boardId) throws AccountNotFoundException {
        Board board = boardService.get(boardId);
        for (Long id : correctIds) {
            notificationSender.sendNotificationToManyUsers(NotificationRequest.createDeletedItemRequest(board,
                            id, settingsService.getNotificationSettingFromDB(Notifications.ITEM_DELETED.name)),
                    boardService.get(boardId).getBoardUsersSet());
        }
    }
}