package CRM.entity.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SettingUpdateRequest {
    Long userId;
    Long boardId;
    String notificationName;
    Boolean inApp;
    Boolean inEmail;
}
