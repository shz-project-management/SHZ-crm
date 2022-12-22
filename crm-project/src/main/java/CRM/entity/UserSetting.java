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
public class UserSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne()
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private User user;

    @JoinColumn(nullable = false)
    @ManyToOne()
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private NotificationSetting setting;

    @Column(name = "in_app")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private boolean inApp;

    @Column(name = "in_email")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private boolean inEmail;
}
