package CRM.entity;

import CRM.entity.requests.ItemRequest;
import CRM.entity.requests.UpdateObjectRequest;
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

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    @Column(name = "assigned_to_user_id")
    private Long assignedToUserId;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "importance")
    private int importance;

    @OneToMany(mappedBy = "parentItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "parentItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Item> items = new HashSet<>();

    public static Item createNewItem(ItemRequest itemRequest, Board board, User user) {
        // FIXME: validate parent
        Item parentItem = null;
        if (itemRequest.getParentItemId() != null)
            parentItem = board.getItemFromSectionById(itemRequest.getParentItemId(), itemRequest.getSectionId());
        Section section = board.getSectionFromBoard(itemRequest.getSectionId());

        Status status = itemRequest.getStatusId() == null ? null : (Status) board.getAttributeById(itemRequest.getStatusId(), Status.class);
        Type type = itemRequest.getTypeId() == null ? null : (Type) board.getAttributeById(itemRequest.getTypeId(), Type.class);

        Item item = new Item();
        item.setSection(section);
        item.setImportance(itemRequest.getImportance());
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
