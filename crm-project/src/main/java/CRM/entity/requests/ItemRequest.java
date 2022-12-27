package CRM.entity.requests;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ItemRequest extends SharedContentRequest{
    private Long statusId;
    private Long typeId;
    private LocalDateTime dueDate;
}
