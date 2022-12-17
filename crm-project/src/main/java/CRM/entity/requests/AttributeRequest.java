package CRM.entity.requests;

import CRM.entity.Board;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AttributeRequest {
    private Board board;
    private String name;
    private String description;
}
