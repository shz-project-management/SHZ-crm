package CRM.entity.requests;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class UpdateItemRequest {
    //SharedContent fields
    private Long parentItemId;
    private Long userId;
    private String title;
    private String description;

    //Item fields
    private Long boardId;
    private String section;
    private Long assignedToUserId;
    private LocalDateTime dueDate;
    private Integer importance;
}
