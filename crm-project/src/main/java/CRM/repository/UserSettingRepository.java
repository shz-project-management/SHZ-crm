package CRM.repository;

import CRM.entity.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSettingRepository extends JpaRepository<UserSetting, Long> {
    @Query(value = "SELECT * from users_settings_in_board WHERE user_id = ?1 AND board_id = ?2", nativeQuery = true)
    List<UserSetting> getUserSettingsInBoard(Long userId, Long boardId);
}
