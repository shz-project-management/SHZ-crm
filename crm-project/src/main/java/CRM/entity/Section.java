package CRM.entity;

import CRM.entity.requests.AttributeRequest;
import CRM.entity.requests.UpdateObjectRequest;
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

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Item> items = new HashSet<>();

    public static Section createSection(AttributeRequest sectionRequest) {
        Section section = new Section();
        section.setName(sectionRequest.getName());
        section.setDescription(sectionRequest.getDescription());
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
