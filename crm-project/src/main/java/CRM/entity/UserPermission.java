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

    public static UserPermission newUserPermission(User user, Permission permission) {
        UserPermission userPermission = new UserPermission();
        userPermission.setId(0L);
        userPermission.setUser(user);
        userPermission.setPermission(permission);
        return userPermission;
    }
}