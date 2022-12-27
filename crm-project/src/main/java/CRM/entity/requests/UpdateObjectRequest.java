package CRM.entity.requests;

import CRM.utils.enums.UpdateField;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateObjectRequest {
    private UpdateField fieldName;
    private Object content;
    private ObjectsIdsRequest objectsIdsRequest;
}
