package CRM.repository;

import CRM.entity.Board;
import CRM.entity.Item;
import CRM.entity.User;
import CRM.entity.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b from Board b WHERE b.creatorUser = ?1")
    List<Board> findAllByUser(User user);

    @Transactional
    @Modifying
    @Query("DELETE Board b WHERE b.creatorUser = ?1")
    void deleteAllByUser(User user);

    @Transactional
    @Modifying
    void removeByCreatorUser(User user);

}