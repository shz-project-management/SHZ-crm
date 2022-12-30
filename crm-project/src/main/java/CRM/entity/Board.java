package CRM.entity;

import CRM.entity.requests.UpdateObjectRequest;
import CRM.utils.enums.ExceptionMessage;
import CRM.utils.enums.Permission;
import lombok.*;

import javax.naming.NoPermissionException;
import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "boards")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_user_id")
    private User creatorUser;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "board_id")
    private Set<Type> types = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "board_id")
    private Set<Status> statuses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "board_id")
    private Set<Section> sections = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "board_id")
    private Set<UserPermission> usersPermissions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "board_id")
    private Set<UserSetting> usersSettings = new HashSet<>();


    public static Board createBoard(User user, String name, String description) {
        Board board = new Board();
        board.setCreatorUser(user);
        board.setName(name);
        board.setDescription(description);
        board.setStatuses(Status.defaultStatuses());
        board.setTypes(Type.defaultTypes());
        return board;
    }

    //--------------------------------------User--------------------------------------//

    /**
     * Adds the given user permission to the board.
     *
     * @param userPermission the user permission to add
     */
    public void addUserPermissionToBoard(UserPermission userPermission) {
        usersPermissions.add(userPermission);
    }

    /**
     * Retrieves the user permission for the user with the given ID from the given set of user permissions.
     *
     * @param userId             the ID of the user to retrieve the permission for
     * @param userPermissionsSet the set of user permissions to search
     * @return the user permission for the user with the given ID, or null if not found
     */
    public UserPermission getUserPermissionById(Long userId, Set<UserPermission> userPermissionsSet) {
        for (UserPermission userInBoard : userPermissionsSet) {
            if (userInBoard.getUser().getId().equals(userId)) {
                return userInBoard;
            }
        }
        return null;
    }

    /**
     * Retrieves a list of all users in the given board, including the creator user and users with permissions.
     *
     * @param board              the board to retrieve the users for
     * @param userPermissionsSet the set of user permissions for the board
     * @return a list of all users in the given board
     */
    public List<User> getAllUsersInBoard(Board board, Set<UserPermission> userPermissionsSet) {
        List<User> users = new ArrayList<>();
        users.add(board.getCreatorUser());
        for (UserPermission addUSer : userPermissionsSet) {
            users.add(addUSer.getUser());
        }
        return users;
    }

    //--------------------------------------Section--------------------------------------//

    /**
     * Retrieves the section with the given ID from the board.
     *
     * @param sectionId the ID of the section to retrieve
     * @return the section with the given ID from the board
     * @throws IllegalArgumentException if the section with the given ID is not found in the board
     */
    public Section getSectionFromBoard(long sectionId) {
        for (Section section : sections) {
            if (section.getId() == sectionId) return section;
        }
        throw new IllegalArgumentException("Could not find this section in the db!"); // FIXME: return null
    }

    /**
     * Adds the given section to the board.
     *
     * @param section the section to add to the board
     */
    public void addSectionToBoard(Section section) {
        sections.add(section);
    }

    /**
     * Removes the section with the given ID from the board.
     *
     * @param sectionId the ID of the section to remove from the board
     */
    public void removeSectionFromBoard(long sectionId) {
        sections.removeIf(section -> section.getId() == sectionId);
    }

    //--------------------------------------Comment--------------------------------------//

    /**
     * Retrieves the comment with the given ID from the item with the given ID in the section with the given ID.
     *
     * @param commentId the ID of the comment to retrieve
     * @param itemId    the ID of the item containing the comment
     * @param sectionId the ID of the section containing the item
     * @return the comment with the given ID from the item with the given ID in the section with the given ID
     */
    public Comment getCommentFromItemInSection(long commentId, long itemId, long sectionId) {
        return getSectionFromBoard(sectionId)
                .getItemById(itemId)
                .getCommentById(commentId);
    }

    /**
     * Inserts the given comment into the item with the given ID in the section with the given ID.
     *
     * @param comment   the comment to insert
     * @param itemId    the ID of the item to insert the comment into
     * @param sectionId the ID of the section containing the item
     */
    public void insertCommentToItemInSection(Comment comment, long itemId, long sectionId) {
        getSectionFromBoard(sectionId)
                .getItemById(itemId)
                .insertComment(comment);
    }

    /**
     * Retrieves all comments in the item with the given ID from the section with the given ID, sorted by ID.
     *
     * @param sectionId the ID of the section containing the item
     * @param itemId    the ID of the item containing the comments
     * @return a sorted set of all comments in the item with the given ID from the section with the given ID
     */
    public Set<Comment> getAllCommentsInItem(long sectionId, long itemId) {
        Set<Comment> commentsSet = new HashSet<>(getSectionFromBoard(sectionId)
                .getItemById(itemId)
                .getComments());

        return commentsSet.stream()
                .sorted((o1, o2) -> {
                    // Assuming that the objects have a getId() method that returns their ID as a Long
                    Long id1 = o1.getId();
                    Long id2 = o2.getId();
                    return id1.compareTo(id2);
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }


    //--------------------------------------Item--------------------------------------//

    /**
     * Retrieves the item with the given ID from the section with the given ID.
     *
     * @param itemId    the ID of the item to retrieve
     * @param sectionId the ID of the section containing the item
     * @return the item with the given ID from the section with the given ID
     */
    public Item getItemFromSectionById(long itemId, long sectionId) {
        return getSectionFromBoard(sectionId)
                .getItemById(itemId);
    }

    /**
     * Inserts the given item into the board. If the item has a parent item, it is inserted as a child of the parent item.
     * Otherwise, it is inserted directly into the section.
     *
     * @param item the item to insert
     */
    public void insertItemToSection(Item item) {
        Section section = getSectionFromBoard(item.getSection().getId());
        if (item.getParentItem() == null) section.insertItem(item);
        else getItemFromSectionById(item.getParentItem().getId(), item.getSection().getId()).insertItem(item);
    }

    /**
     * Updates the item with the given ID in the section with the given ID.
     *
     * @param objectRequest the update request object
     * @param itemId        the ID of the item to update
     * @param sectionId     the ID of the section containing the item
     * @return the updated item
     */
    public Item updateItem(UpdateObjectRequest objectRequest, long itemId, long sectionId) {
        return getItemFromSectionById(itemId, sectionId)
                .updateItem(objectRequest);
    }

    //--------------------------------------Attributes--------------------------------------//

    /**
     * Retrieves an attribute with the given ID and class type.
     *
     * @param id  the ID of the attribute to retrieve
     * @param clz the class type of the attribute to retrieve
     * @param <T> the type of the attribute to retrieve, which must extend Attribute
     * @return the attribute with the given ID and class type
     * @throws NoSuchElementException if the attribute with the given ID and class type could not be found in the database
     */
    public <T extends Attribute> T getAttributeById(long id, Class<T> clz) {
        Set<T> attributes = getAttributeSet(clz);
        for (T attribute : attributes) {
            if (attribute.getId() == id) return attribute;
        }
        throw new NoSuchElementException("Could not find this attribute in the db"); // FIXME: return null
    }

    /**
     * Adds the given attribute to the board.
     *
     * @param attribute the attribute to add
     * @param clz       the class type of the attribute, which must extend Attribute
     * @param <T>       the type of the attribute to add, which must extend Attribute
     * @throws IllegalArgumentException if an attribute with the same name and class type already exists on the board
     */
    public <T extends Attribute> void addAttributeToBoard(T attribute, Class<T> clz) {
        checkIfAttributeNameAlreadyExists(attribute.getName(), clz);
        getAttributeSet(clz).add(attribute);
    }

    /**
     * Removes the attribute with the given ID and class type from the board.
     *
     * @param attributeId the ID of the attribute to remove
     * @param clz         the class type of the attribute to remove, which must extend Attribute
     * @throws NoSuchElementException if the attribute with the given ID and class type could not be found in the database
     */
    public void removeAttribute(long attributeId, Class<? extends Attribute> clz) {
        getAttributeSet(clz).remove(getAttributeById(attributeId, clz));
    }

    /**
     * Retrieves a list of all attributes on the board with the given class type.
     *
     * @param clz the class type of the attributes to retrieve, which must extend Attribute
     * @param <T> the type of the attributes to retrieve, which must extend Attribute
     * @return a list of all attributes on the board with the given class type
     */
    public <T extends Attribute> List<T> getAllAttributeInBoard(Class<T> clz) {
        return new ArrayList<>(getAttributeSet(clz));
    }

    //--------------------------------------User--------------------------------------//

    /**
     * Retrieves a list of all users with permissions on the board.
     *
     * @return a list of all users with permissions on the board
     */
    public List<User> getAllUsersInBoard() {
        return usersPermissions
                .stream().map(UserPermission::getUser).collect(Collectors.toList());
    }

    /**
     * Determines whether the board includes the user with the given ID.
     *
     * @param userId the ID of the user to check
     * @return true if the board includes the user with the given ID, false otherwise
     */
    public Boolean doesBoardIncludeUser(long userId) {
        for (UserSetting userSetting : usersSettings) {
            if (userSetting.getUser().getId() == userId) return true;
        }
        return false;
    }

    /**
     * Retrieves a set of all users with permissions on the board.
     *
     * @return a set of all users with permissions on the board
     */
    public Set<User> getBoardUsersSet() {
        Set<User> userSet = new HashSet<>();
        for (UserSetting userSetting : usersSettings) {
            userSet.add(userSetting.getUser());
        }
        return userSet;
    }

    //--------------------------------------Settings--------------------------------------//

    /**
     * Removes the user settings for the user with the given permission from the board.
     *
     * @param userPermissionInBoard the user permission for the user whose settings should be removed from the board
     */
    public void removeSettingsByUserPermission(UserPermission userPermissionInBoard) {
        usersSettings.removeIf(user -> user.getUser().equals(userPermissionInBoard.getUser()));
    }

    // -------- Helpers: --------- //

    /**
     * Retrieves the attribute set for the given class type.
     *
     * @param clz the class type of the attribute set to retrieve, which must extend Attribute
     * @param <T> the type of the attribute set to retrieve, which must extend Attribute
     * @return the attribute set for the given class type
     * @throws IllegalArgumentException if the class type is invalid
     */
    public <T extends Attribute> Set<T> getAttributeSet(Class<T> clz) {
        if (clz == Type.class) return (Set<T>) types;
        if (clz == Status.class) return (Set<T>) statuses;

        throw new IllegalArgumentException("Invalid Attribute class: " + clz); // FIXME: return null
    }

    /**
     * Checks if an attribute with the given name and class type already exists on the board.
     *
     * @param name the name of the attribute to check
     * @param clz  the class type of the attribute to check, which must extend Attribute
     * @throws IllegalArgumentException if an attribute with the given name and class type already exists on the board
     */
    private void checkIfAttributeNameAlreadyExists(String name, Class<? extends Attribute> clz) {
        List<Attribute> list = new ArrayList<>(getAttributeSet(clz));
        for (Attribute attribute : list) {
            if (attribute.getName().equals(name))
                throw new IllegalArgumentException("This name already exists"); // FIXME: return null
        }
    }

    /**
     * Adds the given user setting to the board.
     *
     * @param userSetting the user setting to add
     */
    public void addUserSettingToBoard(UserSetting userSetting) {
        usersSettings.add(userSetting);
    }

    /**
     * Removes the attribute with the given ID and class type from all items on the board.
     * If the class type is Status, the status of each item will be set to null.
     * If the class type is Type, the type of each item will be set to null.
     *
     * @param attributeId the ID of the attribute to remove
     * @param clz         the class type of the attribute to remove, which must extend Attribute
     */
    public <T extends Attribute> void removeAttributeFromItems(long attributeId, Class<T> clz) {
        getSections().forEach(section ->
                section.getItems().forEach(item -> {
                    if (clz == Status.class) {
                        if (item.getStatus() == null) return;
                        if (item.getStatus().getId().equals(attributeId)) {
                            item.setStatus(null);
                        }
                    } else {
                        if (item.getType() == null) return;
                        if (item.getType().getId().equals(attributeId)) {
                            item.setType(null);
                        }
                    }
                }));
    }

    /**
     * Retrieves the type with the given name.
     *
     * @param typeName the name of the type to retrieve
     * @return the type with the given name, or null if no such type exists
     */
    public Type getType(String typeName) {
        return types.stream()
                .filter(type -> type.getName().equals(typeName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the status with the given name.
     *
     * @param statusName the name of the status to retrieve
     * @return the status with the given name, or null if no such status exists
     */
    public Status getStatus(String statusName) {
        return statuses.stream()
                .filter(status -> status.getName().equals(statusName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the status with the given ID.
     *
     * @param statusId the ID of the status to retrieve
     * @return the status with the given ID, or null if no such status exists
     */
    public Status getStatus(long statusId) {
        return statuses.stream()
                .filter(status -> status.getId().equals(statusId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the type with the given ID.
     *
     * @param typeId the ID of the type to retrieve
     * @return the type with the given ID, or null if no such type exists
     */
    public Type getType(long typeId) {
        return types.stream()
                .filter(type -> type.getId().equals(typeId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the section with the given ID.
     *
     * @param sectionId the ID of the section to retrieve
     * @return the section with the given ID, or null if no such section exists
     */
    public Section getSection(long sectionId) {
        return sections.stream()
                .filter(section -> section.getId().equals(sectionId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the permission level for the user with the given ID.
     *
     * @param userId the ID of the user to retrieve the permission level for
     * @return the permission level for the user with the given ID, as an integer ordinal
     * @throws NoPermissionException if the user has no permission on the board
     */
    public Integer getUserPermissionIntegerByUserId(Long userId) throws NoPermissionException {
        if (creatorUser.getId().equals(userId)) {
            return Permission.ADMIN.ordinal();
        }
        Optional<UserPermission> userPerm = getUsersPermissions().stream().filter(userPermission -> userPermission.getUser().getId().equals(userId)).findFirst();
        if (userPerm.isPresent()) {
            return userPerm.get().getPermission().ordinal();
        }
        throw new NoPermissionException(ExceptionMessage.PERMISSION_FAILED.toString());
    }

    /**
     * Retrieves the permission level for the user with the given ID, including the "admin" level.
     *
     * @param userId the ID of the user to retrieve the permission level for
     * @return the permission level for the user with the given ID, as a Permission enum value
     * @throws NoPermissionException if the user has no permission on the board
     */
    public Permission getUserPermissionWithAdminByUserId(Long userId) throws NoPermissionException {
        if (creatorUser.getId().equals(userId)) {
            return Permission.ADMIN;
        }
        Optional<UserPermission> userPerm = getUsersPermissions().stream().filter(userPermission -> userPermission.getUser().getId().equals(userId)).findFirst();
        if (userPerm.isPresent()) {
            return userPerm.get().getPermission();
        }
        throw new NoPermissionException(ExceptionMessage.PERMISSION_FAILED.toString());
    }

    /**
     * Retrieves the item with the given ID from the section with the given ID.
     *
     * @param itemId    the ID of the item to retrieve
     * @param sectionId the ID of the section containing the item
     * @return the item with the given ID from the section with the given ID
     */
    public Item getItemById(long itemId, long sectionId) {
        return getItemFromSectionById(itemId, sectionId);
    }

    /**
     * Retrieves the comments with the given IDs.
     *
     * @param ids the IDs of the comments to retrieve
     * @return the comments with the given IDs
     */
    public List<Comment> findCommentsByIds(List<Long> ids) {
        // Filter the sections by id and flatten the sections into a stream of items
        Stream<Item> items = getSections().stream().flatMap(section -> section.getItems().stream());

        // Filter the items by id and flatten the items into a stream of comments
        Stream<Comment> comments = items.flatMap(item -> item.getComments().stream())
                .filter(comment -> ids.contains(comment.getId()));

        // Collect the matching comments into a list
        return comments.collect(Collectors.toList());
    }

    /**
     * Retrieves an object of the given class with the given ID.
     *
     * @param content   the ID of the object to retrieve
     * @param objClass  the class of the object to retrieve
     * @param sectionId the ID of the section containing the item (if objClass is Item.class)
     * @return the object with the given ID, or null if no such object exists
     */
    public Object getObjectByItsClass(Integer content, Class objClass, Long sectionId) {
        if (objClass == Status.class) {
            return getStatus(content);
        } else if (objClass == Type.class) {
            return getType(content);
        } else if (objClass == Section.class) {
            return getSection(content);
        } else if (objClass == Item.class) {
            return getItemById(content, sectionId);
        } else {
            return null;
        }
    }

    /**
     * Assigns the user with the given email address to the item with the given ID in the section with the given ID.
     *
     * @param updateObjId the ID of the item to assign the user to
     * @param sectionId   the ID of the section containing the item
     * @param userEmail   the email address of the user to assign to the item
     */
    public void assignUserToItem(Long updateObjId, Long sectionId, String userEmail) {
        getItemFromSectionById(updateObjId, sectionId).setAssignedToUserId(userEmail);
    }
}

