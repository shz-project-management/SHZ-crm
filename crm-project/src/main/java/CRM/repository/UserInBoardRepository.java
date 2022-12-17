package CRM.repository;

import CRM.entity.Board;
import CRM.entity.User;
import CRM.entity.UserInBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserInBoardRepository extends JpaRepository<UserInBoard, Long> {
    List<UserInBoard> findAllUserByBoard(Board board);

    List<UserInBoard> findAllBoardByUser(User user);

    @Transactional
    @Modifying
    @Query("DELETE UserInBoard uib WHERE uib.board = ?1")
    void deleteAllByBoard(Board board);

    @Query("SELECT uib FROM UserInBoard uib WHERE uib.user = ?1 AND uib.board = ?2")
    Optional<UserInBoard> findByBoardAndUser(User user, Board board);

    @Transactional
    @Modifying
    @Query("DELETE UserInBoard uib WHERE uib.user = ?1 OR uib.board = ?2")
    void deleteAllByUserOrBoard(User user, Board board);
}
