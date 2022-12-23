package CRM.entity;

import CRM.entity.requests.ItemRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.utils.enums.UpdateField;
import lombok.*;

import javax.persistence.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "board_id")
    private Set<Type> types = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "board_id")
    private Set<Status> statuses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL , orphanRemoval = true)
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
        return board;
    }

    //--------------------------------------User--------------------------------------//
    public void addUserPermissionToBoard(UserPermission userPermission) {
        usersPermissions.add(userPermission);
    }

    //--------------------------------------Section--------------------------------------//
    public Section getSectionFromBoard(long sectionId) {
        for (Section section : sections) {
            if (section.getId() == sectionId) return section;
        }
        throw new IllegalArgumentException("Could not find this section in the db!");
    }

    public void addSectionToBoard(Section section){
        sections.add(section);
    }

    public void removeSectionFromBoard(long sectionId){
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


    //--------------------------------------Item--------------------------------------//
    public Item getItemFromSectionById(long itemId, long sectionId) {
        return getSectionFromBoard(sectionId)
                .getItemById(itemId);
    }

    public void insertItemToSection(Item item, long sectionId) {
        getSectionFromBoard(sectionId)
                .insertItem(item);
    }

    public void insertItemToItemInSection(Item item, long itemId, long sectionId) {
        getSectionFromBoard(sectionId)
                .getItemById(itemId)
                .insertItem(item);
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
        throw new NoSuchElementException("Could not find this attribute in the db");
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

    // -------- Helpers: --------- //


    public <T extends Attribute> Set<T> getAttributeSet(Class<T> clz) {
        if (clz == Type.class) return (Set<T>) types;
        if (clz == Status.class) return (Set<T>) statuses;

        throw new IllegalArgumentException("Invalid Attribute class: " + clz);
    }


    private void checkIfAttributeNameAlreadyExists(String name, Class clz) {
        List<Attribute> list = (List<Attribute>) getAttributeSet(clz).stream().collect(Collectors.toList());
        for (Attribute attribute : list) {
            if (attribute.getName().equals(name))
                throw new IllegalArgumentException("This name already exists"); // FIXME:
        }
    }

    public void addUserSettingToBoard(UserSetting userSetting) {
        usersSettings.add(userSetting);
    }
}

