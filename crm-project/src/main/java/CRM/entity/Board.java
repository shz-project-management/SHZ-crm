package CRM.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToOne
    @JoinColumn(name = "creator_user_id")
    private User creatorUser;

    private String name;
    private String description;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Type> types = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Status> statuses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Section> sections = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserPermission> usersPermissions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserSetting> usersSettings = new HashSet<>();

    public void addUserPermissionToBoard(UserPermission userPermission) {
        usersPermissions.add(userPermission);
    }
    public void addUserSettingToBoard(UserSetting userSetting) {
        usersSettings.add(userSetting);
    }
    public void addStatusToBoard(Status status){
        statuses.add(status);
    }
    public void addTypeToBoard(Type type){
        types.add(type);
    }
    public void addSectionToBoard(Section section){
        sections.add(section);
    }

    public Item getItemById(long id){
        // ... find the item using his id
        return new Item(); // but not a new one, return the real item.
    }

    public void removeAttribute(long attributeId, Class clz){
        // .. loop through the attribute set (by the clz)
        // remove this attribute from the set
    }

    public void getAttribute(long attributeId, Class clz){
        // .. get the attribute from the relevant set
    }

    public void addAttribute(Attribute attribute, Class clz){
        // check which class this is
        // add the attribute (casted) to the relevant Set (status, section, type)
    }

    public List<User> getAllUsersInBoard(){
        // create an empty list of users
        // loop through UsersPermission set and add every user to the list of users
        // return the users list
        return null;
    }

    public static Board createBoard(User user, String name, String description) {
        Board board = new Board();
        board.setCreatorUser(user);
        board.setName(name);
        board.setDescription(description);
        return board;
    }
}

