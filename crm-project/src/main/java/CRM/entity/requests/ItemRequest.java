package CRM.entity.requests;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ItemRequest extends SharedContentRequest{

    private Long boardId;
    private Long statusId;
    private Long typeId;
    private String section;
    private Long assignedToUserId;
    private LocalDateTime dueDate;
    private Integer importance;
}
