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
@Table(name = "settings")
@IdClass(Setting.class)
public class Setting {
    @Id
    private Long id;
    private String title;
    private String description;
}
