package CRM.entity.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SettingUpdateRequest {
    Long userSettingId;
    Long boardId;
    Boolean inApp;
    Boolean inEmail;
}
