package CRM.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
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

    public static Board createBoard(User user, String name, String description) {
        Board board = new Board();
        board.setCreatorUser(user);
        board.setName(name);
        board.setDescription(description);
        return board;
    }
}

