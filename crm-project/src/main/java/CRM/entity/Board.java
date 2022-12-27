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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
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
    public void addUserPermissionToBoard(UserPermission userPermission) {
        usersPermissions.add(userPermission);
    }

    public UserPermission getUserPermissionById(Board board, Long userId, Set<UserPermission> userPermissionsSet) {
        for (UserPermission userInBoard : userPermissionsSet) {
            if (userInBoard.getUser().getId().equals(userId)) {
                return userInBoard;
            }
        }
        return null;
    }

    public List<User> getAllUsersInBoard(Board board, Set<UserPermission> userPermissionsSet) {
        List<User> users = new ArrayList<>();
        users.add(board.getCreatorUser());
        for (UserPermission addUSer : userPermissionsSet) {
            users.add(addUSer.getUser());
        }
        return users;
    }

    //--------------------------------------Section--------------------------------------//
    public Section getSectionFromBoard(long sectionId) {
        for (Section section : sections) {
            if (section.getId() == sectionId) return section;
        }
        throw new IllegalArgumentException("Could not find this section in the db!"); // FIXME: return null
    }

    public void addSectionToBoard(Section section) {
        sections.add(section);
    }

    public void removeSectionFromBoard(long sectionId) {
        sections.removeIf(section -> section.getId() == sectionId);
    }

    //--------------------------------------Comment--------------------------------------//
    public Comment getCommentFromItemInSection(long commentId, long itemId, long sectionId) {
        return getSectionFromBoard(sectionId)
                .getItemById(itemId)
                .getCommentById(commentId);
    }

    public void insertCommentToItemInSection(Comment comment, long itemId, long sectionId) {
        getSectionFromBoard(sectionId)
                .getItemById(itemId)
                .insertComment(comment);
    }

    public Set<Comment> getAllCommentsInItem(long sectionId, long itemId) {
        return new HashSet<>(getSectionFromBoard(sectionId)
                .getItemById(itemId)
                .getComments());
    }


    //--------------------------------------Item--------------------------------------//
    public Item getItemFromSectionById(long itemId, long sectionId) {
        return getSectionFromBoard(sectionId)
                .getItemById(itemId);
    }

    public void insertItemToSection(Item item) {
        Section section = getSectionFromBoard(item.getSection().getId());
        if (item.getParentItem() == null) section.insertItem(item);
        else getItemFromSectionById(item.getParentItem().getId(), item.getSection().getId()).insertItem(item);
    }

    public Item updateItem(UpdateObjectRequest objectRequest, long itemId, long sectionId) {
        return getItemFromSectionById(itemId, sectionId)
                .updateItem(objectRequest);
    }

    //--------------------------------------Attributes--------------------------------------//
    public Attribute getAttributeById(long id, Class clz) {
        Set<Attribute> attributes = getAttributeSet(clz);
        for (Attribute attribute : attributes) {
            if (attribute.getId() == id) return attribute;
        }
        throw new NoSuchElementException("Could not find this attribute in the db"); // FIXME: return null
    }

    public void addAttributeToBoard(Attribute attribute, Class clz) {
        checkIfAttributeNameAlreadyExists(attribute.getName(), clz);
        getAttributeSet(clz).add(attribute);
    }

    public void removeAttribute(long attributeId, Class clz) {
        getAttributeSet(clz).remove(getAttributeById(attributeId, clz));
    }

    public void updateAttribute(UpdateObjectRequest attributeRequest, long attributeId, Class clz) {
        // check which class this is
        // update the attribute (casted) field to the relevant Set (status, section, type)
    }

    public List<Attribute> getAllAttributeInBoard(Class clz) {
        return (List<Attribute>) getAttributeSet(clz)
                .stream().collect(Collectors.toList());
    }

    //--------------------------------------User--------------------------------------//
    public List<User> getAllUsersInBoard() {
        return usersPermissions
                .stream().map(UserPermission::getUser).collect(Collectors.toList());
    }


    //--------------------------------------Settings--------------------------------------//
    public void removeSettingsByUserPermission(UserPermission userPermissionInBoard) {
        usersSettings.removeIf(user -> user.getUser().equals(userPermissionInBoard.getUser()));
    }

    // -------- Helpers: --------- //


    public <T extends Attribute> Set<T> getAttributeSet(Class<T> clz) {
        if (clz == Type.class) return (Set<T>) types;
        if (clz == Status.class) return (Set<T>) statuses;

        throw new IllegalArgumentException("Invalid Attribute class: " + clz); // FIXME: return null
    }


    private void checkIfAttributeNameAlreadyExists(String name, Class clz) {
        List<Attribute> list = (List<Attribute>) getAttributeSet(clz).stream().collect(Collectors.toList());
        for (Attribute attribute : list) {
            if (attribute.getName().equals(name))
                throw new IllegalArgumentException("This name already exists"); // FIXME: return null
        }
    }

    public void addUserSettingToBoard(UserSetting userSetting) {
        usersSettings.add(userSetting);
    }

    public Type getTypeByName(String typeName) {
        return types.stream()
                .filter(type -> type.getName().equals(typeName))
                .findFirst()
                .orElse(null);
    }

    public Status getStatusByName(String statusName) {
        return statuses.stream()
                .filter(status -> status.getName().equals(statusName))
                .findFirst()
                .orElse(null);
    }

    public Status getStatusById(long statusId) {
        return statuses.stream()
                .filter(status -> status.getId().equals(statusId))
                .findFirst()
                .orElse(null);
    }

    public Type getTypeById(long typeId) {
        return types.stream()
                .filter(type -> type.getId().equals(typeId))
                .findFirst()
                .orElse(null);
    }

    public Section getSectionById(long sectionId) {
        return sections.stream()
                .filter(section -> section.getId().equals(sectionId))
                .findFirst()
                .orElse(null);
    }

    public Integer getUserPermissionIntegerByUserId(Long userId) throws NoPermissionException {
        if(creatorUser.getId().equals(userId)){
            return Permission.ADMIN.ordinal();
        }
        Optional<UserPermission> userPerm = getUsersPermissions().stream().filter(userPermission -> userPermission.getUser().getId().equals(userId)).findFirst();
        if(userPerm.isPresent()){
            return userPerm.get().getPermission().ordinal();
        }
        throw new NoPermissionException(ExceptionMessage.PERMISSION_FAILED.toString());
    }

    public Permission getUserPermissionWithoutAdminByUserId(Long userId) throws NoPermissionException {
        if(creatorUser.getId().equals(userId)){
            return Permission.ADMIN;
        }
        Optional<UserPermission> userPerm = getUsersPermissions().stream().filter(userPermission -> userPermission.getUser().getId().equals(userId)).findFirst();
        if(userPerm.isPresent()){
            return userPerm.get().getPermission();
        }
        throw new NoPermissionException(ExceptionMessage.PERMISSION_FAILED.toString());
    }

    public Item getItemById(long itemId, long sectionId) {
        return getItemFromSectionById(itemId, sectionId);
    }

    public List<Comment> findCommentsByIds(List<Long> ids) {
        // Filter the sections by id and flatten the sections into a stream of items
        Stream<Item> items = getSections().stream().flatMap(section -> section.getItems().stream());

        // Filter the items by id and flatten the items into a stream of comments
        Stream<Comment> comments = items.flatMap(item -> item.getComments().stream())
                .filter(comment -> ids.contains(comment.getId()));

        // Collect the matching comments into a list
        return comments.collect(Collectors.toList());
    }


    public Object getObjectByItsClass(Integer content, Class objClass, Long sectionId) {
        if (objClass == Status.class) {
            return getStatusById(content);
        } else if (objClass == Type.class) {
            return getTypeById(content);
        } else if (objClass == Section.class) {
            return getSectionById(content);
        } else if (objClass == Item.class) {
            return getItemById(content, sectionId);
        } else {
            return null;
        }
    }
}

