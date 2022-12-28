package CRM.entity.requests;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemRequest extends SharedContentRequest{
    private Long statusId;
    private Long typeId;
    private Long boardId;
    private Long userId;
    private LocalDateTime dueDate;
}
