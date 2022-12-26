package CRM.entity;

import CRM.entity.requests.NotificationRequest;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users_settings_in_board")
public class UserSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "notification_id")
    private NotificationSetting setting;

    @Column(name = "in_app")
    private boolean inApp;

    @Column(name = "in_email")
    private boolean inEmail;

    public static UserSetting createUserSetting(User user, NotificationSetting notificationSetting){
        UserSetting userSetting = new UserSetting();
        userSetting.setInApp(true);
        userSetting.setInEmail(true);
        userSetting.setUser(user);
        userSetting.setSetting(notificationSetting);
        return userSetting;
    }

    public static UserSetting getRelevantUserSetting(Board board, User user, String wantedSettingName){
        UserSetting wantedUserSetting = null;
        Set<UserSetting> userSettingsInBoard = board.getUsersSettings();
        for (UserSetting userSetting : userSettingsInBoard) {
            if(Objects.equals(user.getId(), userSetting.getUser().getId()) &&
                    Objects.equals(userSetting.getSetting().getName(), wantedSettingName))
                wantedUserSetting = userSetting;
        }
        return wantedUserSetting;
    }
}