package CRM.entity;

import CRM.utils.enums.Permission;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_permission")
public class UserPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private User user;

    @Enumerated
    @Column(name = "permission")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Permission permission;

//    public static UserInBoard adminUserInBoard(User user, Board board) {
//        UserInBoard userInBoard = new UserInBoard();
//        userInBoard.setUser(user);
//        userInBoard.setPermission(Permission.ADMIN);
//        return userInBoard;
//    }
//
//    public static UserInBoard userInBoardUser(User user, Board board) {
//        UserInBoard userInBoard = new UserInBoard();
//        userInBoard.setUser(user);
//        userInBoard.setPermission(Permission.USER);
//        return userInBoard;
//    }
//
//    public static UserInBoard userInBoardUserChoosePermission(User creatorUser, Board board, Permission permission){
//        UserInBoard userInBoard = new UserInBoard();
//        userInBoard.setUser(creatorUser);
//        userInBoard.setPermission(permission);
//        return userInBoard;
//    }
}