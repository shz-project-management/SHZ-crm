package CRM.entity;

import CRM.utils.enums.Permission;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users_permissions_in_board")
public class UserPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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