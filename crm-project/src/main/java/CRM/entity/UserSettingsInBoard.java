package CRM.entity;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class UserSettingsInBoard {
    @EmbeddedId
    private UserSettingsInBoardPK id = new UserSettingsInBoardPK();

    @ManyToOne()
    @MapsId("userId")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private User user;

    @ManyToOne()
    @MapsId("boardId")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Board board;

    @ManyToOne()
    @MapsId("settingId")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private NotificationSetting setting;

    @Column(name = "in_app")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private boolean inApp;

    @Column(name = "in_email")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private boolean inEmail;
}
