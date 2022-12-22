package CRM.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;


@ToString
@Entity
@Getter
@Setter
@Table(name = "sections")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Item> items = new HashSet<>();

    public static Section createSection(Attribute attribute) {
        Section section = new Section();
        section.setName(attribute.getName());
        section.setDescription(attribute.getDescription());
        return section;
    }

    public Item getItemById(long itemId){
        for (Item item: items) {
            if(item.getId() == itemId) return item;
        }
        throw new NoSuchElementException("Could not find this item in the db");
    }

    public void insertItem(Item item){
        items.add(item);
    }


}
