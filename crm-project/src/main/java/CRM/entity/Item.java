package CRM.entity;

import CRM.entity.requests.UpdateObjectRequest;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "items")
public class Item extends SharedContent {
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type type;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    private Long assignedToUserId;
    private LocalDateTime dueDate;
    private int importance;

    @OneToMany(mappedBy = "parentItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "parentItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Item> items;

    // FIXME: Is it ok? Should it get less params?
    public static Item createNewItem(Section section, Status status, Type type, User user, String name, String description, Item parentItem, int importance){
        Item item = new Item();
        item.setSection(section);
        item.setImportance(importance);
        item.setStatus(status);
        item.setType(type);
        item.setUser(user);
        item.setParentItem(parentItem);
        item.setDescription(description);
        item.setName(name);
        return item;
    }

    public Item updateItem(UpdateObjectRequest objectRequest){
        return this;
    }

    public void insertComment(Comment comment){
        comments.add(comment);
    }

    public void insertItem(Item item){
        items.add(item);
    }

    public Comment getCommentById(long itemId){
        for (Comment comment: comments) {
            if(comment.getId() == itemId) return comment;
        }
        throw new NoSuchElementException("Could not find this comment in the db");
    }

}
