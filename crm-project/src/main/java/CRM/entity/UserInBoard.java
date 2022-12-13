package CRM.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "users_in_board")
@IdClass(UserInBoard.class)
public class UserInBoard {
    @Id
    private Long userId;
    @Id
    private Long boardId;
}
