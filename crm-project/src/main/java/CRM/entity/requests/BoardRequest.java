package CRM.entity.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BoardRequest {
    private Long creatorUserId;
    private String name;
    private String description;
}
