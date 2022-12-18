package CRM.entity.requests;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class UpdateBoardRequest {
    private String boardName;
    private String description;
}
