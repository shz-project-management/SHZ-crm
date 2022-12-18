package CRM.entity.requests;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class UpdateBoardRequest {
    private Long id;
    private String name;
    private String description;
}
