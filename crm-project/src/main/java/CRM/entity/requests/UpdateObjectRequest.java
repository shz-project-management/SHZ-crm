package CRM.entity.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UpdateObjectRequest {
    private String fieldName;
    private Object content;
}
