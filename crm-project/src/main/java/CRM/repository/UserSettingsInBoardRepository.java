package CRM.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSettingsInBoardRepository extends JpaRepository<CRM.entity.UserSettingsInBoard, Long> {
}
