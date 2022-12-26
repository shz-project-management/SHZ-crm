package CRM.entity;

import CRM.entity.requests.ItemRequest;
import CRM.entity.requests.UpdateObjectRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    @Column(name = "assigned_to_user_id")
    private Long assignedToUserId;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "importance")
    private int importance;

    @JsonIgnore
    @OneToMany(mappedBy = "parentItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "parentItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Item> items = new HashSet<>();

    public static Item createNewItem(ItemRequest itemRequest, Board board, User user) {
        Section section = board.getSectionFromBoard(itemRequest.getSectionId());
        Status status = board.getStatusByName("Open");
        Type type = itemRequest.getParentItemId() != null ?
                board.getTypeByName("Sub-Item") :
                board.getTypeByName("Item") ;

        Item parentItem = null;
        if (itemRequest.getParentItemId() != null) {
            parentItem = board.getItemFromSectionById(itemRequest.getParentItemId(), itemRequest.getSectionId());
        }

        Item item = new Item();
        item.setSection(section);
        item.setImportance(1);
        item.setStatus(status);
        item.setType(type);
        item.setUser(user);
        item.setParentItem(parentItem);
        item.setDescription(itemRequest.getDescription());
        item.setName(itemRequest.getName());
        return item;
    }

    public Item updateItem(UpdateObjectRequest objectRequest) {
        return this;
    }

    public void insertComment(Comment comment) {
        comments.add(comment);
    }

    public void insertItem(Item item) {
        items.add(item);
    }

    public Comment getCommentById(long itemId) {
        for (Comment comment : comments) {
            if (comment.getId() == itemId) return comment;
        }
        throw new NoSuchElementException("Could not find this comment in the db");
    }

}
