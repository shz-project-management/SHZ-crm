package CRM.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
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
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type type;

    private String section;
    private Long assignedToUserId;
    private LocalDateTime dueDate;
    private int importance;

    @OneToMany(mappedBy = "parentItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "parentItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Item> items;
}
