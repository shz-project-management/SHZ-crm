package CRM.entity.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BoardRequest {
    private Long creatorUserId;
    private String name;
    private String description;
}
