package CRM.entity.DTO;

import CRM.entity.NotificationSetting;
import CRM.entity.User;
import CRM.entity.UserSetting;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SettingsDTO {
    private Long settingId;
    private NotificationSetting setting;
    private boolean inApp;
    private boolean inEmail;

    public static List<SettingsDTO> createUserSettingsList(List<UserSetting> userSettings){
        List<SettingsDTO> userSettingsDTO = new ArrayList<>();
        for (UserSetting userSetting : userSettings) {
            SettingsDTO settingsDTO = new SettingsDTO();
            settingsDTO.setSettingId(userSetting.getId());
            settingsDTO.setSetting(userSetting.getSetting());
            settingsDTO.setInApp(userSetting.isInApp());
            settingsDTO.setInEmail(userSetting.isInEmail());
            userSettingsDTO.add(settingsDTO);
        }
        return userSettingsDTO;
    }
}
