package CRM.entity.requests;

import CRM.entity.Board;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AttributeRequest {
    private Long boardId;
    private String name;
    private String description;
}
