package CRM.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "user_settings_in_board")
@IdClass(UserSettingsInBoard.class)
public class UserSettingsInBoard {
    @Id
    private Long userId;
    @Id
    private Long boardId;
    @Id
    private Long settingId;

    private boolean isActive;
}
