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
public class UserInBoard {
    @EmbeddedId
    private UserInBoardPK id = new UserInBoardPK();

    @ManyToOne()
    @MapsId("userId")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private User user;

    @ManyToOne()
    @MapsId("boardId")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Board board;

    @Enumerated
    @Column(name = "permission")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Permission permission;

    public static UserInBoard adminUserInBoard(User creatorUser, Board dbBoard) {
        UserInBoard userInBoard = new UserInBoard();
        userInBoard.setBoard(dbBoard);
        userInBoard.setUser(creatorUser);
        userInBoard.setPermission(Permission.ADMIN);
        return userInBoard;
    }
}