package CRM.entity.requests;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SharedContentRequest {
    private Long parentItemId;
    private Long sectionId;
    private String name;
    private String description;
    private Long assignToUserId;
    private LocalDateTime creationTime = LocalDateTime.now();
}
