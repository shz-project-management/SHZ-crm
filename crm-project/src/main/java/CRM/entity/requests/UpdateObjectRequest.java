package CRM.entity.requests;

import CRM.utils.enums.UpdateField;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateObjectRequest {
    private UpdateField fieldName;
    private Object content;
    private ObjectsIdsRequest objectsIdsRequest;
}
