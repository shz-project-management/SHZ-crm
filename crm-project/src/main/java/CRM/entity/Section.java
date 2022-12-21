package CRM.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;


@ToString
@Entity
@Getter
@Setter
@Table(name = "section")
public class Section extends Attribute {

    @OneToMany(mappedBy = "section", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Item> items;

    public static Section createSection(Attribute attribute) {
        Section section = new Section();
        section.setName(attribute.getName());
        section.setDescription(attribute.getDescription());
        section.setBoard(attribute.getBoard());
        return section;
    }
}
