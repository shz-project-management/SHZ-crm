package CRM.entity.requests;

import CRM.entity.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ItemRequest {
    //SharedContent fields
    private Long parentItemId;
    private Long userId;
    private String title;
    private String description;

    //Item fields
    private Long boardId;
    private AttributeRequest status;
    private AttributeRequest type;
    private String section;
    private Long assignedToUserId;
    private LocalDateTime dueDate;
    private Integer importance;
}
