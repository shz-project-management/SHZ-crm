package CRM.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "items")
public class Item extends SharedContent {
    private Long boardId;
    private String status;
    private String type;
    private String section;
    private List<Long> assignedTo;
    private LocalDateTime dueDate;
    private int importance;
    private List<Comment> comments;
}
