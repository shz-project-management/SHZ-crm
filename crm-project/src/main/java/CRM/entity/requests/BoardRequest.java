package CRM.entity.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BoardRequest {
    private Long creatorUserId;
    private String name;
    private String description;
}
