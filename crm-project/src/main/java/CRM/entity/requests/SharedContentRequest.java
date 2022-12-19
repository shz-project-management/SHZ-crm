package CRM.entity.requests;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SharedContentRequest {
    private Long parentItemId;
    private Long userId;
    private String title;
    private String description;
    private Long assignToUserId;
    private LocalDateTime creationTime = LocalDateTime.now();
}
