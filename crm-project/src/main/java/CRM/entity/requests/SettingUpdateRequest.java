package CRM.entity.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SettingUpdateRequest {
    Long userSettingId;
    Boolean inApp;
    Boolean inEmail;
}
