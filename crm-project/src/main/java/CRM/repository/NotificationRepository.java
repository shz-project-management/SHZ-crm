package CRM.repository;

import CRM.entity.Board;
import CRM.entity.Notification;
import CRM.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Transactional
    @Modifying
    long deleteByBoard(Board board);
    @Transactional
    @Modifying
    long deleteByUser(User user);
    List<Notification> findByUser_IdAndBoard_Id(@NonNull Long userId, @NonNull Long boardId);
}
